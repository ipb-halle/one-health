package de.ipb_halle.server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ipb_halle.server.postgre.repositories.UserRepository;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserCreditRepository;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

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
}
