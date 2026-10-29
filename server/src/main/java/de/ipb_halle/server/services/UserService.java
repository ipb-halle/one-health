package de.ipb_halle.server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ipb_halle.server.postgre.repositories.UserRepository;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserCreditRepository;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserCreditEntity;
import de.ipb_halle.server.postgre.models.UserRole;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final BigDecimal INITIAL_CREDIT = new BigDecimal("5.00");
    private static final LocalDateTime LAST_LOCAL_DATE_TIME = LocalDateTime.now(ZoneId.of("Europe/Berlin"));

    private final UserRepository userRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserCreditRepository userCreditRepository;

    public UserService(
            UserRepository userRepository,
            UserAuthenticationRepository userAuthenticationRepository,
            UserCreditRepository userCreditRepository) {

        this.userRepository = userRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userCreditRepository = userCreditRepository;
    }

    /**
     * Deletes a user and all associated data.
     * 
     * This method handles deletion in the correct order to avoid database
     * constraint violations:
     * 1. Delete user credits
     * 2. Delete user authentication records
     * 3. Delete the user entity
     * 
     * @param userId the ID of the user to delete
     * @throws IllegalArgumentException if userId is null or invalid
     */
    @Transactional
    public void deleteUserById(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        try {
            // Delete user credits first (OneToOne relationship)
            userCreditRepository.deleteAll(
                    userCreditRepository.findAll().stream()
                            .filter(credit -> credit.getUser().getId().equals(userId))
                            .toList());

            log.debug("Deleted user credits for user ID: {}", userId);

            // Delete user authentication records (ManyToOne relationship)
            userAuthenticationRepository.deleteAll(
                    userAuthenticationRepository.findByUserId(userId));

            log.debug("Deleted user authentication records for user ID: {}", userId);

            // Finally, delete the user entity
            userRepository.deleteById(userId);

            log.info("Successfully deleted user with ID: {}", userId);

        } catch (Exception e) {
            log.error("Failed to delete user with ID: {}", userId, e);
            throw e;
        }
    }

    /**
     * Updates a user's display name.
     * 
     * @param userId the ID of the user to update
     * @param displayName the new display name (must be non-empty and between 1-200 characters)
     * @return the updated UserEntity
     * @throws IllegalArgumentException if userId is invalid or displayName is invalid
     */
    @Transactional
    public de.ipb_halle.server.postgre.models.UserEntity updateUserDisplayName(
            Long userId,
            String displayName) {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if (displayName == null || displayName.isBlank() || displayName.length() > 200) {
            throw new IllegalArgumentException(
                    "Display name must be between 1 and 200 characters");
        }

        var user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("User not found for update: {}", userId);
                return new IllegalArgumentException("User not found");
            });

        user.setDisplayName(displayName.trim());

        de.ipb_halle.server.postgre.models.UserEntity updatedUser = userRepository.save(user);

        log.info("Updated display name for user ID: {}", userId);

        return updatedUser;
    }

    /**
     * Updates a user's display name only if the current display name is empty or null.
     * Used by provider services during user provisioning.
     * 
     * @param user the UserEntity to potentially update
     * @param displayName the new display name to set if current is empty
     * @return the UserEntity (updated or unchanged)
     */
    @Transactional
    public UserEntity updateUserDisplayNameIfEmpty(UserEntity user, String displayName) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (displayName == null || displayName.isBlank()) {
            return user;
        }

        if (user.getDisplayName() == null || user.getDisplayName().isBlank()) {
            user.setDisplayName(displayName.trim());
            return userRepository.save(user);
        }

        return user;
    }

    /**
     * Creates a new user with authentication provider information and initial credits.
     * This method handles the complete user provisioning process:
     * 1. Create the UserEntity
     * 2. Create the UserAuthenticationEntity linking user to provider
     * 3. Assign initial credits
     * 
     * @param displayName the display name for the new user
     * @param userRole the role to assign to the user (e.g., VIEWER)
     * @param authProvider the authentication provider (e.g., ORCID)
     * @param providerSubjectId the subject ID from the provider (e.g., ORCID ID)
     * @return the created UserEntity with all associations persisted
     * @throws IllegalArgumentException if any required parameter is invalid
     */
    @Transactional
    public UserEntity createUserWithAuthProvider(
            String displayName,
            UserRole userRole,
            AuthenticationProvider authProvider,
            String providerSubjectId) {

        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("Display name cannot be empty");
        }

        if (userRole == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }

        if (authProvider == null) {
            throw new IllegalArgumentException("Authentication provider cannot be null");
        }

        if (providerSubjectId == null || providerSubjectId.isBlank()) {
            throw new IllegalArgumentException("Provider subject ID cannot be empty");
        }

        // Create the user entity
        UserEntity newUser = new UserEntity();
        newUser.setDisplayName(displayName.trim());
        newUser.setRole(userRole);
        newUser.setEnabled(true);
        newUser.setRegisteredVia(authProvider);

        UserEntity savedUser = userRepository.save(newUser);
        log.debug("Created new user with ID: {} via provider: {}", savedUser.getId(), authProvider);

        // Create the authentication record linking user to provider
        UserAuthenticationEntity authentication = new UserAuthenticationEntity();
        authentication.setUser(savedUser);
        authentication.setProvider(authProvider);
        authentication.setProviderSubjectId(providerSubjectId);

        userAuthenticationRepository.save(authentication);
        log.debug("Created authentication record for user ID: {} with provider: {}", 
                  savedUser.getId(), authProvider);

        // Assign initial credits
        assignInitialCredits(savedUser);

        log.info("Successfully provisioned new user with ID: {} via provider: {}", 
                 savedUser.getId(), authProvider);

        return savedUser;
    }

    /**
     * Assigns initial credits to a newly created user.
     * 
     * @param user the UserEntity to assign credits to
     */
    private void assignInitialCredits(UserEntity user) {
        UserCreditEntity userCreditEntity = new UserCreditEntity();
        userCreditEntity.setUser(user);
        userCreditEntity.setCredit(INITIAL_CREDIT);
        userCreditEntity.setLastCreditAssigned(LAST_LOCAL_DATE_TIME);
        userCreditRepository.save(userCreditEntity);
        log.debug("Assigned initial credits to user ID: {}", user.getId());
    }

    /**
     * Updates the last login timestamp for a user.
     * Called by provider services after successful user provisioning.
     * 
     * @param user the UserEntity to update
     * @throws IllegalArgumentException if user is null
     */
    @Transactional
    public void updateLastLogin(UserEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        user.setLastLogin(LAST_LOCAL_DATE_TIME);
        userRepository.save(user);
        log.debug("Updated last login for user ID: {}", user.getId());
    }
}
