package de.ipb_halle.server.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfFilter;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@Configuration
public class SecurityConfig {

    private final N1hOidcUserService n1hOidcUserService;

    public SecurityConfig(N1hOidcUserService n1hOidcUserService) {
        this.n1hOidcUserService = n1hOidcUserService;
    }

    @Bean
    OAuth2AuthorizationRequestResolver authorizationRequestResolver(
        ClientRegistrationRepository clientRegistrationRepository) {

        DefaultOAuth2AuthorizationRequestResolver resolver =
            new DefaultOAuth2AuthorizationRequestResolver(
                    clientRegistrationRepository,
                    "/api/oauth2/authorization");

        resolver.setAuthorizationRequestCustomizer(builder ->
                builder.additionalParameters(params ->
                        params.put("prompt", "login")));
    return resolver;
}

    @Bean
    public SecurityFilterChain applicationSecurityFilterChain(
        HttpSecurity http,
        OAuth2AuthorizationRequestResolver authorizationRequestResolver)
            throws Exception {

        CsrfTokenRequestAttributeHandler csrfTokenRequestHandler =
                new CsrfTokenRequestAttributeHandler();

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(
                                CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(csrfTokenRequestHandler))
                .addFilterAfter(new CsrfCookieFilter(), CsrfFilter.class)

                .logout(logout -> logout
                    .logoutUrl("/api/auth/logout")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                    .logoutSuccessHandler((request, response, authentication) -> {
                        response.setStatus(HttpStatus.OK.value());
                        response.setContentType(
                                MediaType.APPLICATION_JSON_VALUE);
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
                        .redirectionEndpoint(redirection ->
                                redirection.baseUri(
                                "/api/login/oauth2/code/*"))
                        .userInfoEndpoint(userInfo ->
                                userInfo.oidcUserService(n1hOidcUserService))
                        .defaultSuccessUrl(
                                "http://a.localhost:5173/", true));

        return http.build();
    }
}