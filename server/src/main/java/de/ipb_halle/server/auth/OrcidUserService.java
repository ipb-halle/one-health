package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.models.UserRole;

import java.util.Optional;

import de.ipb_halle.server.services.UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class OrcidUserService implements ProviderUserService {

    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserService userService;

    public OrcidUserService(
            UserAuthenticationRepository userAuthenticationRepository,
            UserService userService) {

        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userService = userService;
    }

    @Override
    public UserEntity provisionUser(OidcUser oidcUser) {

        String orcid = oidcUser.getSubject();

        if (orcid == null || orcid.isBlank()) {
            throw new IllegalArgumentException("ORCID subject is missing");
        }

        String displayName = resolveDisplayName(oidcUser, orcid);

        Optional<UserAuthenticationEntity> authentification = userAuthenticationRepository
                .findByProviderAndProviderSubjectId(
                        AuthenticationProvider.ORCID,
                        orcid);

        if (authentification.isPresent()) {
            return updateExistingUser(authentification.get().getUser(), displayName);
        } else {
            return createNewUser(displayName, orcid);
        }

    }

    /**
     * Updates an existing user's display name if it's currently empty.
     * Delegates to UserService for consistency.
     * 
     * @param user the existing UserEntity
     * @param displayName the display name to set if current is empty
     * @return the updated UserEntity
     */
    private UserEntity updateExistingUser(UserEntity user, String displayName) {
        return userService.updateUserDisplayNameIfEmpty(user, displayName);
    }

    /**
     * Creates a new user with ORCID authentication.
     * Delegates all user creation and authentication setup to UserService.
     * 
     * @param displayName the display name for the new user
     * @param orcid the ORCID ID
     * @return the newly created UserEntity
     */
    private UserEntity createNewUser(String displayName, String orcid) {
        return userService.createUserWithAuthProvider(
                displayName,
                UserRole.VIEWER,
                AuthenticationProvider.ORCID,
                orcid);
    }

    private String resolveDisplayName(OidcUser oidcUser, String orcid) {

        String fullName = oidcUser.getFullName();

        if (fullName != null) {
            fullName = fullName.trim();

            if (!fullName.isEmpty()) {
                return fullName;
            }
        }

        String givenName = oidcUser.getGivenName();

        if (givenName != null) {
            givenName = givenName.trim();
        }

        String familyName = oidcUser.getFamilyName();

        if (familyName != null) {
            familyName = familyName.trim();
        }

        boolean hasGivenName = givenName != null && !givenName.isEmpty();

        boolean hasFamilyName = familyName != null && !familyName.isEmpty();

        if (hasGivenName && hasFamilyName) {
            return givenName + " " + familyName;
        }

        if (hasGivenName) {
            return givenName;
        }

        if (hasFamilyName) {
            return familyName;
        }

        return orcid;
    }

    @Override
    public void updateLastLogin(UserEntity user) {
        userService.updateLastLogin(user);
    }
}
