package de.ipb_halle.server.api;

import de.ipb_halle.api.UsersApi;
import de.ipb_halle.model.User;
import de.ipb_halle.model.UpdateUserRequest;
import de.ipb_halle.server.postgre.mapping.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.RequestMapping;

import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.services.UserService;
import de.ipb_halle.server.services.UserAuthenticationService;
import org.springframework.web.bind.annotation.RestController;
import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(path = "api")
public class UserController implements UsersApi {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserAuthenticationService userAuthenticationService;
    private final UserService userService;

    public UserController(
            UserAuthenticationService userAuthenticationService,
            UserService userService) {
        this.userAuthenticationService = userAuthenticationService;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<Void> deleteCurrentUser() {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
            || !authentication.isAuthenticated()
            || !(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            log.warn("Unauthorized delete user attempt");
            return ResponseEntity.status(401).build();
        }

        String orcid = oidcUser.getSubject();

        try {
            var userAuthentication = userAuthenticationService
                .findByProviderAndSubjectId(AuthenticationProvider.ORCID, orcid)
                .orElseThrow(() -> {
                    log.warn("User authentication not found for ORCID: {}", orcid);
                    return new RuntimeException("User not found");
                });

            UserEntity userEntity = userAuthentication.getUser();
            Long userId = userEntity.getId();

            log.info("Deleting user account for user ID: {} (ORCID: {})", userId, orcid);

            userService.deleteUserById(userId);

            log.info("Successfully deleted user account for user ID: {} (ORCID: {})", userId, orcid);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            log.error("Failed to delete user account for ORCID: {}", orcid, e);
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<User> updateCurrentUser(UpdateUserRequest updateUserRequest) {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
            || !authentication.isAuthenticated()
            || !(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            log.warn("Unauthorized update user attempt");
            return ResponseEntity.status(401).build();
        }

        if (updateUserRequest == null
            || updateUserRequest.getDisplayName() == null
            || updateUserRequest.getDisplayName().isBlank()) {
            log.warn("Invalid update user request: displayName is required");
            return ResponseEntity.badRequest().build();
        }

        String orcid = oidcUser.getSubject();

        try {
            var userAuthentication = userAuthenticationService
                .findByProviderAndSubjectId(AuthenticationProvider.ORCID, orcid)
                .orElseThrow(() -> {
                    log.warn("User authentication not found for ORCID: {}", orcid);
                    return new RuntimeException("User not found");
                });

            UserEntity userEntity = userAuthentication.getUser();
            Long userId = userEntity.getId();

            log.info("Updating user profile for user ID: {} (ORCID: {})", userId, orcid);

            UserEntity updatedUser = userService.updateUserDisplayName(
                    userId,
                    updateUserRequest.getDisplayName());

            User userDto = UserMapper.MAPPER.toDto(updatedUser);
            userDto.setOrcid(orcid);

            log.info("Successfully updated user profile for user ID: {} (ORCID: {})", userId, orcid);

            return ResponseEntity.ok(userDto);

        } catch (IllegalArgumentException e) {
            log.warn("Validation error updating user for ORCID: {} - {}", orcid, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Failed to update user profile for ORCID: {}", orcid, e);
            return ResponseEntity.status(500).build();
        }
    }
}
