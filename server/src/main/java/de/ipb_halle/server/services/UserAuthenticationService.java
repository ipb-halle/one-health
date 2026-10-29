package de.ipb_halle.server.services;

import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service layer for handling user authentication lookups.
 * Abstracts the interaction with UserAuthenticationRepository.
 * Provides provider mapping functionality to determine the AuthenticationProvider
 * from OAuth2 registration IDs.
 */
@Service
public class UserAuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(UserAuthenticationService.class);

    /**
     * Registry mapping OAuth2 registration IDs (e.g., "orcid") to AuthenticationProvider enums.
     * Used to support multiple OAuth2 providers with extensibility.
     */
    private static final Map<String, AuthenticationProvider> REGISTRATION_ID_REGISTRY =
            new HashMap<String, AuthenticationProvider>() {{
                put("orcid", AuthenticationProvider.ORCID);
            }};

    private final UserAuthenticationRepository userAuthenticationRepository;

    public UserAuthenticationService(UserAuthenticationRepository userAuthenticationRepository) {
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    /**
     * Finds a user by the authentication provider and provider subject ID.
     * 
     * @param provider the authentication provider (e.g., ORCID)
     * @param providerSubjectId the subject ID from the provider (e.g., ORCID ID)
     * @return Optional containing the UserAuthenticationEntity if found
     */
    public Optional<UserAuthenticationEntity> findByProviderAndSubjectId(
            AuthenticationProvider provider,
            String providerSubjectId) {

        return userAuthenticationRepository.findByProviderAndProviderSubjectId(provider, providerSubjectId);
    }

    /**
     * Finds a user by authentication provider and subject ID.
     * Returns the associated UserEntity.
     * 
     * @param provider the authentication provider
     * @param providerSubjectId the subject ID from the provider
     * @return Optional containing the UserEntity if found
     */
    public Optional<UserEntity> findUserByProviderAndSubjectId(
            AuthenticationProvider provider,
            String providerSubjectId) {

        return userAuthenticationRepository
                .findByProviderAndProviderSubjectId(provider, providerSubjectId)
                .map(UserAuthenticationEntity::getUser);
    }

    /**
     * Finds a user by OAuth2 registration ID and subject ID.
     * Automatically maps the registration ID to the appropriate AuthenticationProvider.
     * This method is used by controllers that don't want to hardcode specific providers.
     * 
     * @param registrationId the OAuth2 registration ID (e.g., "orcid")
     * @param subjectId the subject ID from the provider
     * @return Optional containing the UserEntity if found
     * @throws OAuth2AuthenticationException if the registration ID is not supported
     */
    public Optional<UserEntity> findUserByRegistrationIdAndSubjectId(
            String registrationId,
            String subjectId) {

        AuthenticationProvider provider = getProviderByRegistrationId(registrationId);
        return findUserByProviderAndSubjectId(provider, subjectId);
    }

    /**
     * Finds a user from the current OAuth2 authentication context.
     * Extracts the registration ID from the Authentication object and maps it to a provider.
     * This is the most controller-friendly method as it requires minimal parameter extraction.
     * 
     * @param subjectId the subject ID from the provider (e.g., ORCID)
     * @param authentication the current Spring Security Authentication
     * @return Optional containing the UserEntity if found
     */
    public Optional<UserEntity> findUserByOidcAuthentication(
            String subjectId,
            Authentication authentication) {

        String registrationId = extractRegistrationId(authentication);
        return findUserByRegistrationIdAndSubjectId(registrationId, subjectId);
    }

    /**
     * Maps an OAuth2 registration ID to the corresponding AuthenticationProvider.
     * Uses a registry that can be extended with new providers.
     * 
     * @param registrationId the OAuth2 registration ID
     * @return the corresponding AuthenticationProvider
     * @throws OAuth2AuthenticationException if the registration ID is not supported
     */
    private AuthenticationProvider getProviderByRegistrationId(String registrationId) {
        if (registrationId == null || registrationId.isBlank()) {
            throw new OAuth2AuthenticationException("Registration ID cannot be empty");
        }

        AuthenticationProvider provider = REGISTRATION_ID_REGISTRY.get(registrationId.toLowerCase());

        if (provider == null) {
            log.warn("Unsupported OAuth2 provider: {}", registrationId);
            throw new OAuth2AuthenticationException(
                    "Unsupported OAuth2 provider: " + registrationId);
        }

        return provider;
    }

    /**
     * Extracts the OAuth2 registration ID from the Spring Security Authentication object.
     * Handles OAuth2AuthenticationToken and falls back gracefully.
     * 
     * @param authentication the Spring Security Authentication
     * @return the registration ID, or "orcid" as default fallback
     */
    private String extractRegistrationId(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken token) {
            String registrationId = token.getAuthorizedClientRegistrationId();
            if (registrationId != null && !registrationId.isBlank()) {
                log.debug("Extracted registration ID from OAuth2 token: {}", registrationId);
                return registrationId;
            }
        }

        log.debug("Could not extract registration ID from authentication, using default provider");
        return "orcid"; // Default for current implementation
    }
}
