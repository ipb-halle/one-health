
package de.ipb_halle.server.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserCreditEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserCreditRepository;
import de.ipb_halle.server.postgre.repositories.UserRepository;

@Service
public class UserDeletionService {

    private final UserRepository userRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserCreditRepository userCreditRepository;

    public UserDeletionService(
            UserRepository userRepository,
            UserAuthenticationRepository userAuthenticationRepository,
            UserCreditRepository userCreditRepository) {

        this.userRepository = userRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userCreditRepository = userCreditRepository;
    }

    @Transactional
    public boolean deleteUserByOrcid(String orcid) {

        Optional<UserAuthenticationEntity> authentication =
                userAuthenticationRepository
                        .findByProviderAndProviderSubjectId(
                                AuthenticationProvider.ORCID,
                                orcid);

        if (authentication.isEmpty()) {
            return false;
        }

        Long userId = authentication.get().getUser().getId();

        // Delete all authentication associations belonging to this user.
        List<UserAuthenticationEntity> authentications =
                userAuthenticationRepository.findByUserId(userId);

        userAuthenticationRepository.deleteAll(authentications);

        // Delete the user's credit record.
        Optional<UserCreditEntity> credit =
                userCreditRepository.findByUser_Id(userId);

        credit.ifPresent(userCreditRepository::delete);

        // Delete the local user record last.
        userRepository.deleteById(userId);

        return true;
    }
}
