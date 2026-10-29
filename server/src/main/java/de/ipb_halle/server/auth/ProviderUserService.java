package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.models.UserEntity;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

/**
 * Interface for provider-specific user service implementations.
 * Defines the contract for handling user provisioning and updates across different OAuth2 providers.
 */
public interface ProviderUserService {

    /**
     * Provisions a user from an OIDC user object.
     * This may involve creating a new user or updating an existing one.
     * 
     * @param oidcUser the OIDC user from the OAuth2 provider
     * @return the provisioned UserEntity
     */
    UserEntity provisionUser(OidcUser oidcUser);

    /**
     * Updates the last login timestamp for a user.
     * 
     * @param user the UserEntity to update
     */
    void updateLastLogin(UserEntity user);
}
