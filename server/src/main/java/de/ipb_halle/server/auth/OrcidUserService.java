package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.services.UserService;

import java.util.Optional;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

/**
 * Provider-specific service for ORCID OIDC authentication.
 * Responsible for parsing ORCID claims and delegating user provisioning
 * to the core UserService layer.
 */
@Service
public class OrcidUserService {

    private final UserService userService;

    public OrcidUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Provisions a user from an ORCID OIDC user. Handles ORCID-specific claim extraction
     * and delegates the actual user creation/update to UserService.
     *
     * @param oidcUser the OIDC user from ORCID
     * @return the provisioned UserEntity
     * @throws IllegalArgumentException if the ORCID subject is missing
     */
    public UserEntity provisionUser(OidcUser oidcUser) {
        String orcid = oidcUser.getSubject();

        if (orcid == null || orcid.isBlank()) {
            throw new IllegalArgumentException("ORCID subject is missing");
        }

        String displayName = resolveDisplayName(oidcUser, orcid);

        Optional<UserEntity> existingUser = userService.findUserByProviderSubjectId(
                AuthenticationProvider.ORCID,
                orcid);

        if (existingUser.isPresent()) {
            return userService.updateUserDisplayName(existingUser.get(), displayName);
        } else {
            return userService.createUser(displayName, AuthenticationProvider.ORCID, orcid);
        }
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


    /**
     * Updates the last login timestamp for the given user.
     *
     * @param user the user to update
     */
    public void updateLastLogin(UserEntity user) {
        userService.updateLastLogin(user);
    }
}
