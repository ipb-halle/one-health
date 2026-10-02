package de.ipb_halle.server.api;

import de.ipb_halle.api.AuthApi;
import de.ipb_halle.model.LogoutResponse;
import de.ipb_halle.model.User;
import de.ipb_halle.server.postgre.mapping.UserMapper;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.models.AuthenticationProvider;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api")
public class AuthController implements AuthApi {

    private final UserAuthenticationRepository userAuthenticationRepository;

    public AuthController(
            UserAuthenticationRepository userAuthenticationRepository) {

        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    @Override
    public ResponseEntity<User> getCurrentUser() {

        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
            || !authentication.isAuthenticated()
            || !(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            return ResponseEntity.status(401).build();
        }

        String orcid = oidcUser.getSubject();

        return userAuthenticationRepository
            .findByProviderAndProviderSubjectId(
                    AuthenticationProvider.ORCID,
                    orcid)
            .map(userAuthentication -> {
                UserEntity userEntity = userAuthentication.getUser();

                User user = UserMapper.MAPPER.toDto(userEntity);
                user.setOrcid(orcid);

                return ResponseEntity.ok(user);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<LogoutResponse> logout() {
        return ResponseEntity.ok(new LogoutResponse("Logged out successfully"));
    }

}
