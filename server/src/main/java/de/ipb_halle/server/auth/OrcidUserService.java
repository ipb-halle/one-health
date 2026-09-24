package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserRepository;
import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.models.UserRole;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class OrcidUserService {

    private final UserRepository userRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;

    public OrcidUserService(
            UserRepository userRepository,
            UserAuthenticationRepository userAuthenticationRepository) {

        this.userRepository = userRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    public UserEntity provisionUser(OidcUser oidcUser) {

        String orcid = oidcUser.getSubject();

        if (orcid == null || orcid.isBlank()) {
            throw new IllegalArgumentException("ORCID subject is missing");
        }

        return userAuthenticationRepository
                .findByProviderAndProviderSubjectId(
                        AuthenticationProvider.ORCID,
                        orcid)
                .map(UserAuthenticationEntity::getUser)
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setDisplayName(oidcUser.getFullName());
                    newUser.setRole(UserRole.VIEWER);
                    newUser.setEnabled(true);
                    newUser.setRegisteredVia(AuthenticationProvider.ORCID);

                    UserEntity savedUser = userRepository.save(newUser);

                    UserAuthenticationEntity authentication = new UserAuthenticationEntity();

                    authentication.setUser(savedUser);
                    authentication.setProvider(AuthenticationProvider.ORCID);
                    authentication.setProviderSubjectId(orcid);

                    userAuthenticationRepository.save(authentication);

                    return savedUser;
                });
    }
}