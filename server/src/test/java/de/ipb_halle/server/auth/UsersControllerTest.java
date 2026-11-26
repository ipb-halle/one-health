
package de.ipb_halle.server.auth;

import de.ipb_halle.server.api.UsersController;
import de.ipb_halle.server.auth.utils.CustomAuthenticationFailureHandler;
import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserCreditRepository;
import de.ipb_halle.server.postgre.repositories.UserRepository;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(classes = UsersControllerTest.TestConfig.class)
@WebAppConfiguration
@TestPropertySource(properties = {
                "app.frontend-url=http://a.localhost:5173"
})
class UsersControllerTest {

        private static final String ORCID = "0009-0005-1075-0774";

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserAuthenticationRepository userAuthenticationRepository;

        @Autowired
        private UserDeletionService userDeletionService;

        @BeforeEach
        void setUp() {
                // Spring caches the application context, so reset shared Mockito beans.
                reset(userAuthenticationRepository, userDeletionService);
        }

        // Verifies successful account deletion, session invalidation,
        // and clearing of authentication and CSRF cookies.
        @Test
        void shouldDeleteAccountAndInvalidateSession() throws Exception {
                mockEnabledAccount();

                when(userDeletionService.deleteUserByOrcid(ORCID))
                                .thenReturn(true);

                MockHttpSession session = new MockHttpSession();

                MvcResult result = mockMvc.perform(
                                delete("/api/users/me")
                                                .session(session)
                                                .with(oidcLogin()
                                                                .idToken(token -> token.subject(ORCID)))
                                                .with(csrf()))
                                .andExpect(status().isNoContent())
                                .andReturn();

                assertTrue(session.isInvalid());

                Cookie[] cookies = result.getResponse().getCookies();

                assertTrue(Arrays.stream(cookies).anyMatch(cookie -> "JSESSIONID".equals(cookie.getName())
                                && cookie.getMaxAge() == 0));

                assertTrue(Arrays.stream(cookies).anyMatch(cookie -> "XSRF-TOKEN".equals(cookie.getName())
                                && cookie.getMaxAge() == 0));

                verify(userDeletionService).deleteUserByOrcid(ORCID);
        }

        // Verifies that a missing local account returns HTTP 404.
        @Test
        void shouldReturnNotFoundWhenAccountDoesNotExist() throws Exception {
                mockEnabledAccount();

                when(userDeletionService.deleteUserByOrcid(ORCID))
                                .thenReturn(false);

                mockMvc.perform(
                                delete("/api/users/me")
                                                .with(oidcLogin()
                                                                .idToken(token -> token.subject(ORCID)))
                                                .with(csrf()))
                                .andExpect(status().isNotFound());

                verify(userDeletionService).deleteUserByOrcid(ORCID);
        }

        // Verifies that unauthenticated requests cannot delete accounts.
        @Test
        void shouldRejectUnauthenticatedDeletionRequest() throws Exception {
                mockMvc.perform(
                                delete("/api/users/me")
                                                .with(csrf()))
                                .andExpect(status().isUnauthorized());

                verifyNoInteractions(userDeletionService);
        }

        private void mockEnabledAccount() {
                UserEntity user = new UserEntity();
                user.setEnabled(true);

                UserAuthenticationEntity authentication = new UserAuthenticationEntity();

                authentication.setUser(user);

                when(userAuthenticationRepository.findByProviderAndProviderSubjectId(
                                AuthenticationProvider.ORCID,
                                ORCID))
                                .thenReturn(Optional.of(authentication));
        }

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
                UserRepository userRepository() {
                        return mock(UserRepository.class);
                }

                @Bean
                UserCreditRepository userCreditRepository() {
                        return mock(UserCreditRepository.class);
                }

                @Bean
                UserDeletionService userDeletionService() {
                        return mock(UserDeletionService.class);
                }

                @Bean
                ClientRegistrationRepository clientRegistrationRepository() {
                        return mock(ClientRegistrationRepository.class);
                }

                @Bean
                UsersController usersController(
                                UserRepository userRepository,
                                UserAuthenticationRepository userAuthenticationRepository,
                                UserCreditRepository userCreditRepository,
                                UserDeletionService userDeletionService) {

                        return new UsersController(
                                        userRepository,
                                        userAuthenticationRepository,
                                        userCreditRepository,
                                        userDeletionService);
                }

                @Bean
                MockMvc mockMvc(WebApplicationContext context) {
                        return MockMvcBuilders
                                        .webAppContextSetup(context)
                                        .apply(springSecurity())
                                        .build();
                }
        }
}
