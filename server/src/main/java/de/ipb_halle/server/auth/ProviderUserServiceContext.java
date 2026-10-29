package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.models.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Context class for provider user services.
 * Uses the strategy pattern to select the appropriate provider-specific service
 * based on the registration ID (provider name).
 */
@Component
public class ProviderUserServiceContext {

    private static final Logger log = LoggerFactory.getLogger(ProviderUserServiceContext.class);

    private final Map<String, ProviderUserService> strategies;

    public ProviderUserServiceContext(OrcidUserService orcidUserService) {
        this.strategies = new HashMap<>();
        registerStrategy("orcid", orcidUserService);
    }

    /**
     * Registers a provider strategy.
     * 
     * @param registrationId the registration ID (provider name, e.g., "orcid")
     * @param service the provider user service implementation
     */
    private void registerStrategy(String registrationId, ProviderUserService service) {
        strategies.put(registrationId.toLowerCase(), service);
        log.debug("Registered strategy for provider: {}", registrationId);
    }

    /**
     * Provisions a user using the appropriate provider strategy based on registration ID.
     * After provisioning, updates the last login timestamp.
     * 
     * @param oidcUser the OIDC user from the OAuth2 provider
     * @param registrationId the provider registration ID (e.g., "orcid")
     * @return the provisioned UserEntity
     * @throws OAuth2AuthenticationException if the provider is not supported
     */
    public UserEntity provisionAndUpdateUser(OidcUser oidcUser, String registrationId) {
        String normalizedId = registrationId.toLowerCase();

        ProviderUserService service = strategies.get(normalizedId);

        if (service == null) {
            log.warn("No strategy found for provider: {}", registrationId);
            throw new OAuth2AuthenticationException(
                    "Unsupported OIDC provider: " + registrationId);
        }

        UserEntity user = service.provisionUser(oidcUser);
        service.updateLastLogin(user);

        return user;
    }
}
