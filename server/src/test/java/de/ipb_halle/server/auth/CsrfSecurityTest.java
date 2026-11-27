
package de.ipb_halle.server.auth;

import de.ipb_halle.server.auth.utils.CustomAuthenticationFailureHandler;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

/**
 * Integration tests for the application's CSRF protection.
 *
 * These tests load the real SecurityConfig and its CSRF filters,
 * while mocking external dependencies that are not relevant here.
 */
@SpringJUnitConfig(classes = CsrfSecurityTest.TestConfig.class)
@WebAppConfiguration
@TestPropertySource(properties = {
        "app.frontend-url=http://a.localhost:5173"
})
class CsrfSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Minimal Spring test configuration.
     *
     * The production SecurityConfig is imported so the tests exercise
     * the actual CookieCsrfTokenRepository, CsrfCookieFilter and logout
     * configuration.
     */
    @Configuration
    @EnableWebMvc
    @EnableWebSecurity
    @Import({
            SecurityConfig.class,
            CustomAuthenticationFailureHandler.class
    })
    static class TestConfig {

        @Bean
        N1hOidcUserService n1hOidcUserService() {
            return mock(N1hOidcUserService.class);
        }

        @Bean
        UserAuthenticationRepository userAuthenticationRepository() {
            return mock(UserAuthenticationRepository.class);
        }

        @Bean
        ClientRegistrationRepository clientRegistrationRepository() {
            return mock(ClientRegistrationRepository.class);
        }

        @Bean
        TestController testController() {
            return new TestController();
        }

        @Bean
        MockMvc mockMvc(WebApplicationContext context) {
            return MockMvcBuilders
                    .webAppContextSetup(context)
                    .apply(springSecurity())
                    .build();
        }
    }

    /**
     * Test-only endpoints used to verify CSRF behavior.
     *
     * GET is deliberately provided so the CSRF cookie can be issued
     * before a subsequent POST request.
     */
    @RestController
    static class TestController {

        @GetMapping("/test/csrf")
        String getEndpoint() {
            return "OK";
        }

        @PostMapping("/test/csrf")
        String postEndpoint() {
            return "OK";
        }
    }

    /**
     * Verifies that a normal GET request causes the configured
     * CookieCsrfTokenRepository to issue an XSRF-TOKEN cookie.
     */
    @Test
    void shouldIssueCsrfCookieOnGetRequest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/test/csrf"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"))
                .andReturn()
                .getResponse();

        Cookie csrfCookie = response.getCookie("XSRF-TOKEN");

        assertNotNull(csrfCookie, "GET request should issue an XSRF-TOKEN cookie");
        assertFalse(csrfCookie.getValue().isBlank(),
                "The XSRF-TOKEN cookie should contain a token");
    }

    /**
     * Verifies that an unsafe POST request without a CSRF token
     * is rejected with HTTP 403 before reaching the controller.
     */
    @Test
    void shouldRejectPostWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/test/csrf"))
                .andExpect(status().isForbidden());
    }

    /**
     * Verifies the complete cookie/header workflow used by the frontend:
     *
     * 1. GET obtains the XSRF-TOKEN cookie.
     * 2. POST sends that cookie back to the server.
     * 3. POST also sends the cookie value in the X-XSRF-TOKEN header.
     *
     * The request should be accepted because the cookie and header match.
     */
    @Test
    void shouldAllowPostWithCsrfCookieAndHeader() throws Exception {
        MockHttpServletResponse getResponse = mockMvc.perform(get("/test/csrf"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Cookie csrfCookie = getResponse.getCookie("XSRF-TOKEN");

        assertNotNull(csrfCookie, "GET request should issue an XSRF-TOKEN cookie");
        assertFalse(csrfCookie.getValue().isBlank(),
                "The XSRF-TOKEN cookie should contain a token");

        mockMvc.perform(post("/test/csrf")
                .cookie(csrfCookie)
                .header("X-XSRF-TOKEN", csrfCookie.getValue()))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    /**
     * Verifies that the Spring Security logout endpoint is also protected
     * against CSRF attacks.
     *
     * A POST request to logout without a valid CSRF token must be rejected.
     */
    @Test
    void shouldRejectLogoutWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isForbidden());
    }

    /**
     * Verifies successful logout using the real CSRF cookie/header workflow.
     *
     * Checks that Spring Security invalidates the session and clears both the
     * session and CSRF cookies.
     */
    @Test
    void shouldInvalidateSessionAndClearCookiesOnLogout() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("test-session", "active");

        // Obtain the CSRF cookie by performing a GET request with the session
        MockHttpServletResponse getResponse = mockMvc.perform(
                get("/test/csrf").session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Cookie csrfCookie = getResponse.getCookie("XSRF-TOKEN");

        assertNotNull(csrfCookie, "GET request should issue an XSRF-TOKEN cookie");

        // Logout using the same cookie and its matching header.
        MockHttpServletResponse logoutResponse = mockMvc.perform(
                post("/api/auth/logout")
                        .session(session)
                        .cookie(csrfCookie)
                        .header("X-XSRF-TOKEN", csrfCookie.getValue()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        // Verify that the HTTP session was invalidated.

        assertTrue(session.isInvalid(), "Logout should invalidate the HTTP session");

        // Verify that both cookies are cleared by the logout response.
        Cookie[] cookies = logoutResponse.getCookies();

        assertTrue(Arrays.stream(cookies).anyMatch(
                cookie -> "JSESSIONID".equals(cookie.getName()) && cookie.getMaxAge() == 0),
                "Logout should clear the JSESSIONID cookie");
        assertTrue(
                Arrays.stream(cookies)
                        .anyMatch(cookie -> "XSRF-TOKEN".equals(cookie.getName()) && cookie.getMaxAge() == 0),
                "Logout should clear the XSRF-TOKEN cookie");
    }
}
