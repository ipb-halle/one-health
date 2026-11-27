
package de.ipb_halle.server.auth;

import java.io.IOException;
import java.util.Optional;

import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

public class DisabledAccountFilter extends OncePerRequestFilter {

    private final UserAuthenticationRepository userAuthenticationRepository;

    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    public DisabledAccountFilter(
            UserAuthenticationRepository userAuthenticationRepository) {
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof OidcUser oidcUser)) {

            filterChain.doFilter(request, response);
            return;
        }

        String orcid = oidcUser.getSubject();

        Optional<UserAuthenticationEntity> userAuthentication = userAuthenticationRepository
                .findByProviderAndProviderSubjectId(
                        AuthenticationProvider.ORCID,
                        orcid);

        boolean accountEnabled = userAuthentication
                .map(UserAuthenticationEntity::getUser)
                .map(user -> Boolean.TRUE.equals(user.getEnabled()))
                .orElse(false);

        if (!accountEnabled) {
            logoutHandler.logout(request, response, authentication);

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"message\":\"N1H account is disabled or no longer exists\"}");

            return;
        }

        filterChain.doFilter(request, response);
    }
}
