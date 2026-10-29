package de.ipb_halle.server.services;

import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer for handling user authentication lookups.
 * Abstracts the interaction with UserAuthenticationRepository.
 */
@Service
public class UserAuthenticationService {

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
}
