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

        String displayName = resolveDisplayName(oidcUser, orcid);

        return userAuthenticationRepository
                .findByProviderAndProviderSubjectId(
                        AuthenticationProvider.ORCID,
                        orcid)
                .map(authentication -> {
                    UserEntity existingUser = authentication.getUser();

                    if (existingUser.getDisplayName() == null
                            || existingUser.getDisplayName().isBlank()) {
                        existingUser.setDisplayName(displayName);
                        return userRepository.save(existingUser);
                    }
                    return existingUser;

                })
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setDisplayName(displayName);
                    newUser.setRole(UserRole.VIEWER);
                    newUser.setEnabled(true);
                    newUser.setRegisteredVia(AuthenticationProvider.ORCID);

                    UserEntity savedUser = userRepository.save(newUser);

                    UserAuthenticationEntity authentication = 
                        new UserAuthenticationEntity();

                    authentication.setUser(savedUser);
                    authentication.setProvider(AuthenticationProvider.ORCID);
                    authentication.setProviderSubjectId(orcid);

                    userAuthenticationRepository.save(authentication);

                    return savedUser;
                });
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

        boolean hasGivenName =
                givenName != null && !givenName.isEmpty();

        boolean hasFamilyName =
                familyName != null && !familyName.isEmpty();

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

}