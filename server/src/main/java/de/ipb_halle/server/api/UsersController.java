package de.ipb_halle.server.api;

import de.ipb_halle.api.UsersApi;
import de.ipb_halle.model.UpdateUserRequest;
import de.ipb_halle.model.User;
import de.ipb_halle.server.postgre.mapping.UserMapper;
import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.repositories.UserCreditRepository;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserRepository;
import de.ipb_halle.server.auth.UserDeletionService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

@RestController
@RequestMapping(path = "api")
public class UsersController implements UsersApi {

    private final UserRepository userRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserCreditRepository userCreditRepository;
    private final UserDeletionService userDeletionService;

    public UsersController(
            UserRepository userRepository,
            UserAuthenticationRepository userAuthenticationRepository,
            UserCreditRepository userCreditRepository,
            UserDeletionService userDeletionService) {

        this.userRepository = userRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userCreditRepository = userCreditRepository;
        this.userDeletionService = userDeletionService;
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

        userCreditRepository.findByUser_Id(savedUserEntity.getId())
                .ifPresent(userCredit -> response.setCredits(userCredit.getCredit()));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Only an authenticated OIDC user can delete an account.
        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof OidcUser oidcUser)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String orcid = oidcUser.getSubject();

        if (orcid == null || orcid.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Obtain the current servlet request and response before deleting data.
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return ResponseEntity.internalServerError().build();
        }

        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }

        try {
            // The transactional service completes before logout is performed.
            boolean deleted = userDeletionService.deleteUserByOrcid(orcid);

            if (!deleted) {
                return ResponseEntity.notFound().build();
            }

            // Use Spring Security to invalidate the session and clear authentication.
            SecurityContextLogoutHandler securityLogoutHandler = new SecurityContextLogoutHandler();

            securityLogoutHandler.setInvalidateHttpSession(true);
            securityLogoutHandler.setClearAuthentication(true);

            securityLogoutHandler.logout(request, response, authentication);

            // Clear the same cookies used by the configured Spring Security logout.
            CookieClearingLogoutHandler cookieLogoutHandler = new CookieClearingLogoutHandler(
                    "JSESSIONID",
                    "XSRF-TOKEN");

            cookieLogoutHandler.logout(request, response, authentication);

            return ResponseEntity.noContent().build();

        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }
}