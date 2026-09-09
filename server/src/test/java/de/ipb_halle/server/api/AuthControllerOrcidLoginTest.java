package de.ipb_halle.server.api;

import de.ipb_halle.model.OrcidTokenRequest;
import de.ipb_halle.server.auth.OrcidService;
import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import de.ipb_halle.server.postgre.models.UserRole;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthControllerOrcidLoginTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private UserAuthenticationRepository userAuthenticationRepository;

        @Mock
        private OrcidService orcidService;

        @InjectMocks
        private AuthController authController;

        @Test
        void orcidLogin_nullOrcidResponse_returns401() {

                // Arrange
                OrcidTokenRequest request = new OrcidTokenRequest("auth-code", "state");

                when(orcidService.exchangeCode("auth-code"))
                                .thenReturn(null);

                // Act
                ResponseEntity<?> response = authController.orcidLogin(request);

                // Assert
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

                verify(orcidService).exchangeCode("auth-code");
        }

        @Test
        void orcidLogin_newUser_createsUserWithDefaultProperties() {

                // Arrange
                OrcidTokenRequest request = new OrcidTokenRequest("auth-code", "state");

                OrcidService.OrcidTokenResponse orcidResponse = mock(OrcidService.OrcidTokenResponse.class);

                when(orcidService.exchangeCode("auth-code"))
                                .thenReturn(orcidResponse);

                when(orcidResponse.getOrcid())
                                .thenReturn("0000-0001-2345-6789");

                when(orcidResponse.getName())
                                .thenReturn("Dr. Jane Doe");

                when(orcidResponse.getAccessToken())
                                .thenReturn("orcid-access-token");

                // Simulate a first-time ORCID login with no existing authentication identity
                when(userAuthenticationRepository.findByProviderAndProviderSubjectId(
                                AuthenticationProvider.ORCID,
                                "0000-0001-2345-6789"))
                                .thenReturn(Optional.empty());

                when(userRepository.save(any(UserEntity.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                when(userAuthenticationRepository.save(any(UserAuthenticationEntity.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                // Act
                authController.orcidLogin(request);

                // Assert
                ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);

                verify(userRepository).save(userCaptor.capture());

                UserEntity newUser = userCaptor.getValue();

                // Verify the default properties assigned to newly registered users
                assertThat(newUser.getDisplayName()).isEqualTo("Dr. Jane Doe");
                assertThat(newUser.getRole()).isEqualTo(UserRole.VIEWER);
                assertThat(newUser.getEnabled()).isTrue();
                assertThat(newUser.getRegisteredVia())
                                .isEqualTo(AuthenticationProvider.ORCID);
        }

        @Test
        void orcidLogin_existingUser_reusesAccount() {

                // Arrange
                OrcidTokenRequest request = new OrcidTokenRequest("auth-code", "state");

                OrcidService.OrcidTokenResponse orcidResponse = mock(OrcidService.OrcidTokenResponse.class);

                // Simulate an existing user
                UserEntity existingUser = new UserEntity();
                existingUser.setId(1L);
                existingUser.setDisplayName("Old Name");
                existingUser.setRole(UserRole.VIEWER);
                existingUser.setEnabled(true);
                existingUser.setRegisteredVia(AuthenticationProvider.ORCID);

                UserAuthenticationEntity authentication = new UserAuthenticationEntity();
                authentication.setUser(existingUser);
                authentication.setProvider(AuthenticationProvider.ORCID);
                authentication.setProviderSubjectId("0000-0001-2345-6789");

                when(orcidService.exchangeCode("auth-code"))
                                .thenReturn(orcidResponse);

                when(orcidResponse.getOrcid())
                                .thenReturn("0000-0001-2345-6789");

                when(orcidResponse.getName())
                                .thenReturn("Dr. Jane Doe");

                when(orcidResponse.getAccessToken())
                                .thenReturn("orcid-access-token");

                // Simulate a returning ORCID user whose identity is already linked
                // to an existing user
                when(userAuthenticationRepository.findByProviderAndProviderSubjectId(
                                AuthenticationProvider.ORCID,
                                "0000-0001-2345-6789"))
                                .thenReturn(Optional.of(authentication));

                when(userRepository.save(existingUser))
                                .thenReturn(existingUser);

                // Act
                ResponseEntity<?> response = authController.orcidLogin(request);

                // Assert
                assertThat(response.getStatusCode())
                                .isEqualTo(HttpStatus.OK);

                // The existing account is updated rather than creating a new local user.
                assertThat(existingUser.getDisplayName())
                                .isEqualTo("Dr. Jane Doe");

                verify(userRepository).save(existingUser);

                verify(userAuthenticationRepository)
                                .findByProviderAndProviderSubjectId(
                                                AuthenticationProvider.ORCID,
                                                "0000-0001-2345-6789");

                verify(userAuthenticationRepository, never())
                                .save(any(UserAuthenticationEntity.class));
        }
}