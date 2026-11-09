package de.ipb_halle.server.api;

import de.ipb_halle.api.UsersApi;
import de.ipb_halle.model.UpdateUserRequest;
import de.ipb_halle.model.User;
import de.ipb_halle.server.postgre.mapping.UserMapper;
import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(path = "api")
public class UsersController implements UsersApi {

    private final UserRepository userRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;

    public UsersController(
            UserRepository userRepository,
            UserAuthenticationRepository userAuthenticationRepository) {
        this.userRepository = userRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    @Override
    public ResponseEntity<User> updateCurrentUser(
            UpdateUserRequest updateUserRequest) {

        // Only an authenticated ORCID user can edit their own profile
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Validate the update request
        String newDisplayName = updateUserRequest.getDisplayName();

        if (newDisplayName == null || newDisplayName.trim().isEmpty() || newDisplayName.length() > 200) {
            return ResponseEntity.badRequest().build();
        }

        // Resolve the local N1H account using the authenticated ORCID identity.
        String orcid = oidcUser.getSubject();

        UserAuthenticationEntity userAuthenticationEntity = userAuthenticationRepository
                .findByProviderAndProviderSubjectId(
                        AuthenticationProvider.ORCID, orcid)
                .orElse(null);

        if (userAuthenticationEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UserEntity currentUser = userAuthenticationEntity.getUser();

        // Update only the field that is editable in this request
        currentUser.setDisplayName(newDisplayName);
        UserEntity savedUserEntity = userRepository.save(currentUser);

        // Map the saved entity to a the generated API response model (UserDto)
        User response = UserMapper.MAPPER.toDto(savedUserEntity);
        response.setOrcid(orcid);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteCurrentUser() {
        // Account deletion will be implemented after reviewing
        // the complete data-retention and relationship requirements.
        return ResponseEntity.status(409).build();
    }
}