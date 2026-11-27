
package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.auth.utils.CustomAuthenticationFailureHandler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(classes = DisabledAccountFilterSecurityTest.TestConfig.class)
@WebAppConfiguration
@TestPropertySource(properties = {
        "app.frontend-url=http://a.localhost:5173"
})
class DisabledAccountFilterSecurityTest {

    private static final String ORCID = "0009-0005-1075-0774";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

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

    @RestController
    static class TestController {

        @GetMapping("/test/filter")
        public String testEndpoint() {
            return "OK";
        }
    }

    // Verifies that an authenticated user with an enabled local account
    // is allowed to continue using the application.
    @Test
    void shouldAllowRequestForEnabledAccount() throws Exception {
        UserEntity user = new UserEntity();
        user.setEnabled(true);

        UserAuthenticationEntity authentication = new UserAuthenticationEntity();
        authentication.setUser(user);

        when(userAuthenticationRepository.findByProviderAndProviderSubjectId(
                AuthenticationProvider.ORCID,
                ORCID)).thenReturn(Optional.of(authentication));

        mockMvc.perform(
                get("/test/filter")
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin()
                                .idToken(token -> token.subject(ORCID))))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));

        verify(userAuthenticationRepository, atLeastOnce())
                .findByProviderAndProviderSubjectId(
                        AuthenticationProvider.ORCID,
                        ORCID);
    }

    // Verifies that a disabled local account is logged out,
    // its session is invalidated, and HTTP 403 is returned.
    @Test
    void shouldRejectRequestAndInvalidateSessionForDisabledAccount() throws Exception {
        UserEntity user = new UserEntity();
        user.setEnabled(false);

        UserAuthenticationEntity authentication = new UserAuthenticationEntity();
        authentication.setUser(user);

        when(userAuthenticationRepository.findByProviderAndProviderSubjectId(
                AuthenticationProvider.ORCID,
                ORCID)).thenReturn(Optional.of(authentication));

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(
                get("/test/filter")
                        .session(session)
                        .with(SecurityMockMvcRequestPostProcessors.oidcLogin()
                                .idToken(token -> token.subject(ORCID))))
                .andExpect(status().isForbidden());

        assertTrue(session.isInvalid());

        verify(userAuthenticationRepository, atLeastOnce())
                .findByProviderAndProviderSubjectId(
                        AuthenticationProvider.ORCID,
                        ORCID);
    }
}
