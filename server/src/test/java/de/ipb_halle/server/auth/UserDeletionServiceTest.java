
package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserCreditEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserCreditRepository;
import de.ipb_halle.server.postgre.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDeletionServiceTest {

    private UserRepository userRepository;
    private UserAuthenticationRepository userAuthenticationRepository;
    private UserCreditRepository userCreditRepository;

    private UserDeletionService userDeletionService;

    private static final String ORCID = "0009-0005-1075-0774";
    private static final Long USER_ID = 42L;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userAuthenticationRepository = mock(UserAuthenticationRepository.class);
        userCreditRepository = mock(UserCreditRepository.class);

        userDeletionService = new UserDeletionService(
                userRepository,
                userAuthenticationRepository,
                userCreditRepository);
    }

    // Verifies that an existing user's authentication associations,
    // credit record, and local user record are deleted.
    @Test
    void shouldDeleteExistingUserAndAssociatedRecords() {

        UserEntity user = new UserEntity();
        user.setId(USER_ID);

        UserAuthenticationEntity authentication = new UserAuthenticationEntity();
        authentication.setUser(user);
        authentication.setProvider(AuthenticationProvider.ORCID);
        authentication.setProviderSubjectId(ORCID);

        UserAuthenticationEntity anotherAuthentication = new UserAuthenticationEntity();
        anotherAuthentication.setUser(user);

        UserCreditEntity credit = new UserCreditEntity();
        credit.setUser(user);

        when(userAuthenticationRepository.findByProviderAndProviderSubjectId(
                AuthenticationProvider.ORCID, ORCID))
                .thenReturn(Optional.of(authentication));

        when(userAuthenticationRepository.findByUserId(USER_ID))
                .thenReturn(List.of(authentication, anotherAuthentication));

        when(userCreditRepository.findByUser_Id(USER_ID))
                .thenReturn(Optional.of(credit));

        boolean result = userDeletionService.deleteUserByOrcid(ORCID);

        assertTrue(result);

        verify(userAuthenticationRepository).deleteAll(
                List.of(authentication, anotherAuthentication));

        verify(userCreditRepository).delete(credit);

        verify(userRepository).deleteById(USER_ID);

        verify(userAuthenticationRepository).findByProviderAndProviderSubjectId(
                AuthenticationProvider.ORCID, ORCID);

        verify(userAuthenticationRepository).findByUserId(USER_ID);
        verify(userCreditRepository).findByUser_Id(USER_ID);
    }

    // Verifies that a missing ORCID association results in no deletion.
    @Test
    void shouldReturnFalseWhenOrcidAssociationDoesNotExist() {

        when(userAuthenticationRepository.findByProviderAndProviderSubjectId(
                AuthenticationProvider.ORCID, ORCID))
                .thenReturn(Optional.empty());

        boolean result = userDeletionService.deleteUserByOrcid(ORCID);

        assertFalse(result);

        verify(userAuthenticationRepository, never()).findByUserId(anyLong());
        verify(userAuthenticationRepository, never()).deleteAll(anyList());

        verify(userCreditRepository, never()).findByUser_Id(anyLong());
        verify(userCreditRepository, never()).delete(any(UserCreditEntity.class));

        verify(userRepository, never()).deleteById(anyLong());
    }

    // Verifies that a user without a credit record can still be deleted.
    @Test
    void shouldDeleteUserWhenCreditRecordDoesNotExist() {

        UserEntity user = new UserEntity();
        user.setId(USER_ID);

        UserAuthenticationEntity authentication = new UserAuthenticationEntity();
        authentication.setUser(user);

        when(userAuthenticationRepository.findByProviderAndProviderSubjectId(
                AuthenticationProvider.ORCID, ORCID))
                .thenReturn(Optional.of(authentication));

        when(userAuthenticationRepository.findByUserId(USER_ID))
                .thenReturn(List.of(authentication));

        when(userCreditRepository.findByUser_Id(USER_ID))
                .thenReturn(Optional.empty());

        boolean result = userDeletionService.deleteUserByOrcid(ORCID);

        assertTrue(result);

        verify(userAuthenticationRepository).deleteAll(List.of(authentication));
        verify(userCreditRepository, never()).delete(any(UserCreditEntity.class));
        verify(userRepository).deleteById(USER_ID);
    }
}
