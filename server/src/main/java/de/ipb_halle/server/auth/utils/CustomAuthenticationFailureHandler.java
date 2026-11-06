package de.ipb_halle.server.auth.utils;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final String frontendUrl;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomAuthenticationFailureHandler(@Value("${app.frontend-url}") String frontendUrl) {
        this.frontendUrl = frontendUrl.endsWith("/") 
                ? frontendUrl.substring(0, frontendUrl.length() - 1) 
                : frontendUrl;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String errorCode;
        String errorDescription;

        // Check if it's a direct error coming from the OAuth2/OIDC provider
        if (exception instanceof OAuth2AuthenticationException oAuth2Exception) {
            var oAuth2Error = oAuth2Exception.getError();
            errorCode = oAuth2Error.getErrorCode() != null 
                    ? oAuth2Error.getErrorCode().toUpperCase() 
                    : "OAUTH_ERROR";
            
            errorDescription = oAuth2Error.getDescription() != null 
                    ? oAuth2Error.getDescription() 
                    : oAuth2Exception.getMessage();
        } else {
            // Fallback for internal app errors or custom service failures
            errorCode = "AUTHENTICATION_FAILED";
            errorDescription = exception.getMessage() != null 
                    ? exception.getMessage() 
                    : "An unexpected error occurred during authentication.";
        }

        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                .path("/")
                .queryParam("auth_error", errorCode)
                .queryParam("auth_message", errorDescription)
                .build()
                .toUriString();

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}