
package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserCreditEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.models.UserRole;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserCreditRepository;
import de.ipb_halle.server.postgre.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrcidUserServiceTest {

    private UserRepository userRepository;
    private UserAuthenticationRepository userAuthenticationRepository;
    private UserCreditRepository userCreditRepository;

    private OrcidUserService orcidUserService;

    private static final String ORCID = "0009-0005-1075-0774";

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userAuthenticationRepository = mock(UserAuthenticationRepository.class);
        userCreditRepository = mock(UserCreditRepository.class);

        orcidUserService = new OrcidUserService(
                userRepository,
                userAuthenticationRepository,
                userCreditRepository);
    }

    // Verifies that a new ORCID user is created with the expected
    // local account properties, authentication association, and credits.
    @Test
    void shouldCreateNewUserWithInitialCredits() {
        OidcUser oidcUser = mock(OidcUser.class);
        when(oidcUser.getSubject()).thenReturn(ORCID);
        when(oidcUser.getFullName()).thenReturn("Test User");

        when(userAuthenticationRepository.findByProviderAndProviderSubjectId(
                AuthenticationProvider.ORCID, ORCID))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity result = orcidUserService.provisionUser(oidcUser);

        assertNotNull(result);
        assertEquals("Test User", result.getDisplayName());
        assertEquals(UserRole.VIEWER, result.getRole());
        assertEquals(true, result.getEnabled());
        assertEquals(AuthenticationProvider.ORCID, result.getRegisteredVia());

        verify(userRepository).save(result);

        verify(userAuthenticationRepository).save(
                argThat(authentication -> authentication.getUser() == result
                        && authentication.getProvider() == AuthenticationProvider.ORCID
                        && ORCID.equals(authentication.getProviderSubjectId())));

        verify(userCreditRepository).save(
                argThat(credit -> credit.getUser() == result
                        && new BigDecimal("5.00").compareTo(credit.getCredit()) == 0
                        && credit.getLastCreditAssigned() != null));

        verify(userAuthenticationRepository).findByProviderAndProviderSubjectId(
                AuthenticationProvider.ORCID, ORCID);
    }
}
