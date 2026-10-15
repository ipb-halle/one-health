package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserRepository;
import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.models.UserRole;
import de.ipb_halle.server.postgre.models.UserCreditEntity;
import de.ipb_halle.server.postgre.repositories.UserCreditRepository;

import java.util.Optional;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.math.BigDecimal;

@Service
public class OrcidUserService {

    private final UserRepository userRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserCreditRepository userCreditRepository;
    private static final BigDecimal INITIAL_CREDIT = new BigDecimal("5.00");

    public OrcidUserService(
            UserRepository userRepository,
            UserAuthenticationRepository userAuthenticationRepository,
            UserCreditRepository userCreditRepository) {

        this.userRepository = userRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userCreditRepository = userCreditRepository;
    }

    public UserEntity provisionUser(OidcUser oidcUser) {

        String orcid = oidcUser.getSubject();

        if (orcid == null || orcid.isBlank()) {
            throw new IllegalArgumentException("ORCID subject is missing");
        }

        String displayName = resolveDisplayName(oidcUser, orcid);

        Optional<UserAuthenticationEntity> authentification = userAuthenticationRepository
                .findByProviderAndProviderSubjectId(
                        AuthenticationProvider.ORCID,
                        orcid);

        if (authentification.isPresent()) {
            return updateExistingLocalUser(authentification.get(), displayName);
        } else {
            return createNewLocalUser(displayName, orcid);
        }

    }

    private UserEntity updateExistingLocalUser(UserAuthenticationEntity authentication, String displayName) {
        UserEntity existingUser = authentication.getUser();

        if (existingUser.getDisplayName() == null
                || existingUser.getDisplayName().isBlank()) {
            existingUser.setDisplayName(displayName);
            return userRepository.save(existingUser);
        }
        return existingUser;
    }

    private UserEntity createNewLocalUser(String displayName, String orcid) {
        UserEntity newUser = new UserEntity();
        newUser.setDisplayName(displayName);
        newUser.setRole(UserRole.VIEWER);
        newUser.setEnabled(true);
        newUser.setRegisteredVia(AuthenticationProvider.ORCID);

        UserEntity savedUser = userRepository.save(newUser);

        UserAuthenticationEntity authentication = new UserAuthenticationEntity();

        authentication.setUser(savedUser);
        authentication.setProvider(AuthenticationProvider.ORCID);
        authentication.setProviderSubjectId(orcid);

        userAuthenticationRepository.save(authentication);
        assignInitialCredits(savedUser);

        return savedUser;
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

    public void updateLastLogin(UserEntity user) {
        user.setLastLogin(
                LocalDateTime.now(ZoneId.of("Europe/Berlin")));
        userRepository.save(user);
    }

    private void assignInitialCredits(UserEntity user) {
        UserCreditEntity userCreditEntity = new UserCreditEntity();
        userCreditEntity.setUser(user);
        userCreditEntity.setCredit(INITIAL_CREDIT);
        userCreditEntity.setLastCreditAssigned(LocalDateTime.now(ZoneId.of("Europe/Berlin")));
        userCreditRepository.save(userCreditEntity);
    }
}
