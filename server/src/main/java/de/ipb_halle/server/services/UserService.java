package de.ipb_halle.server.services;

import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserCreditEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.models.UserRole;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserCreditRepository;
import de.ipb_halle.server.postgre.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * Core service for user provisioning and profile management.
 * Encapsulates all database operations related to users, keeping
 * repository access isolated from provider-specific logic.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserCreditRepository userCreditRepository;
    private static final BigDecimal INITIAL_CREDIT = new BigDecimal("5.00");
    private static final ZoneId DEFAULT_TIMEZONE = ZoneId.of("Europe/Berlin");

    @Autowired
    public UserService(
            UserRepository userRepository,
            UserAuthenticationRepository userAuthenticationRepository,
            UserCreditRepository userCreditRepository) {

        this.userRepository = userRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userCreditRepository = userCreditRepository;
    }

    /**
     * Finds an existing user by authentication provider and provider subject ID,
     * or returns an empty Optional if not found.
     */
    public Optional<UserEntity> findUserByProviderSubjectId(
            AuthenticationProvider provider,
            String providerSubjectId) {

        return userAuthenticationRepository
                .findByProviderAndProviderSubjectId(provider, providerSubjectId)
                .map(UserAuthenticationEntity::getUser);
    }

    /**
     * Creates a new user with the given display name and provider information,
     * then assigns initial credits.
     *
     * @param displayName the user's display name
     * @param provider the authentication provider (e.g., ORCID)
     * @param providerSubjectId the unique ID from the provider
     * @return the newly created UserEntity
     */
    public UserEntity createUser(
            String displayName,
            AuthenticationProvider provider,
            String providerSubjectId) {

        // Create the new user
        UserEntity newUser = new UserEntity();
        newUser.setDisplayName(displayName);
        newUser.setRole(UserRole.VIEWER);
        newUser.setEnabled(true);
        newUser.setRegisteredVia(provider);

        UserEntity savedUser = userRepository.save(newUser);

        // Link the user to their authentication provider
        UserAuthenticationEntity authentication = new UserAuthenticationEntity();
        authentication.setUser(savedUser);
        authentication.setProvider(provider);
        authentication.setProviderSubjectId(providerSubjectId);

        userAuthenticationRepository.save(authentication);

        // Assign initial credits
        assignInitialCredits(savedUser);

        return savedUser;
    }

    /**
     * Updates an existing user's display name if it is currently null or blank.
     *
     * @param user the user to update
     * @param displayName the new display name
     * @return the updated UserEntity
     */
    public UserEntity updateUserDisplayName(UserEntity user, String displayName) {
        if (user.getDisplayName() == null || user.getDisplayName().isBlank()) {
            user.setDisplayName(displayName);
            return userRepository.save(user);
        }
        return user;
    }

    /**
     * Updates the user's last login timestamp to the current time in the default timezone.
     *
     * @param user the user to update
     */
    public void updateLastLogin(UserEntity user) {
        user.setLastLogin(LocalDateTime.now(DEFAULT_TIMEZONE));
        userRepository.save(user);
    }

    /**
     * Assigns initial credits to a newly created user.
     * This is typically called automatically during user creation.
     *
     * @param user the user to assign credits to
     */
    private void assignInitialCredits(UserEntity user) {
        UserCreditEntity userCreditEntity = new UserCreditEntity();
        userCreditEntity.setUser(user);
        userCreditEntity.setCredit(INITIAL_CREDIT);
        userCreditEntity.setLastCreditAssigned(LocalDateTime.now(DEFAULT_TIMEZONE));
        userCreditRepository.save(userCreditEntity);
    }
}
