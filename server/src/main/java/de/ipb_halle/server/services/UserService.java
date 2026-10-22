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
}
