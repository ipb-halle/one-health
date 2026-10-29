package de.ipb_halle.server.auth;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

public class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        CsrfToken csrfToken =
                (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        // Force deferred token loading so CookieCsrfTokenRepository
        // writes a current XSRF-TOKEN cookie.
        csrfToken.getToken();

        filterChain.doFilter(request, response);
    }
}