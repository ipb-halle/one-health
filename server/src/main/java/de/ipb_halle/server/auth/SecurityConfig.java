package de.ipb_halle.server.auth;

import de.ipb_halle.server.auth.utils.CustomAuthenticationFailureHandler;

import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

        private final N1hOidcUserService n1hOidcUserService;
        private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

        @Value("${app.frontend-url}")
        private String frontendUrl;

        public SecurityConfig(
                        N1hOidcUserService n1hOidcUserService,
                        CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
                this.n1hOidcUserService = n1hOidcUserService;
                this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        }

        @Bean
        OAuth2AuthorizationRequestResolver authorizationRequestResolver(
                        ClientRegistrationRepository clientRegistrationRepository) {

                DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
                                clientRegistrationRepository,
                                "/api/oauth2/authorization");

                resolver.setAuthorizationRequestCustomizer(
                                builder -> builder.additionalParameters(
                                                params -> params.put("prompt", "login")));
                return resolver;
        }

        @Bean
        public SecurityFilterChain applicationSecurityFilterChain(
                        HttpSecurity http,
                        OAuth2AuthorizationRequestResolver authorizationRequestResolver,
                        UserAuthenticationRepository userAuthenticationRepository)
                        throws Exception {

                CsrfTokenRequestAttributeHandler csrfTokenRequestHandler = new CsrfTokenRequestAttributeHandler();

                http
                                .csrf(csrf -> csrf
                                                .csrfTokenRepository(
                                                                CookieCsrfTokenRepository.withHttpOnlyFalse())
                                                .csrfTokenRequestHandler(csrfTokenRequestHandler))
                                .addFilterAfter(new CsrfCookieFilter(), CsrfFilter.class)
                                .addFilterAfter(
                                                new DisabledAccountFilter(userAuthenticationRepository),
                                                AnonymousAuthenticationFilter.class)

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                                .sessionFixation(sessionFixation -> sessionFixation.changeSessionId()))

                                .logout(logout -> logout
                                                .logoutUrl("/api/auth/logout")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                                                .logoutSuccessHandler((request, response, authentication) -> {
                                                        response.setStatus(HttpStatus.OK.value());
                                                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                                        response.setCharacterEncoding("UTF-8");
                                                        response.getWriter().write(
                                                                        "{\"message\":\"Logged out successfully\"}");
                                                }))

                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers(
                                                                "/api/oauth2/authorization/**",
                                                                "/api/login/oauth2/code/**")
                                                .permitAll()
                                                .anyRequest().permitAll())

                                .oauth2Login(oauth2 -> oauth2
                                                .authorizationEndpoint(authorization -> authorization
                                                                .baseUri("/api/oauth2/authorization")
                                                                .authorizationRequestResolver(
                                                                                authorizationRequestResolver))
                                                .redirectionEndpoint(redirection -> redirection.baseUri(
                                                                "/api/login/oauth2/code/*"))
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .oidcUserService(n1hOidcUserService))
                                                .defaultSuccessUrl(frontendUrl, true));

                return http.build();
        }
}