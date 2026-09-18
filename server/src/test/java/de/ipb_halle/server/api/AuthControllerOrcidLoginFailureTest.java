package de.ipb_halle.server.api;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.ipb_halle.model.OrcidTokenRequest;
import de.ipb_halle.server.auth.OrcidService;
import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;
import de.ipb_halle.server.postgre.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthControllerOrcidLoginFailureTest {

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
        void orcidLogin_nullOrcid_returns401() {

                // Arrange
                OrcidTokenRequest request = new OrcidTokenRequest("auth-code", "state");

                OrcidService.OrcidTokenResponse orcidResponse =
                        mock(OrcidService.OrcidTokenResponse.class);

                when(orcidService.exchangeCode("auth-code"))
                        .thenReturn(orcidResponse);

                when(orcidResponse.getOrcid())
                        .thenReturn(null);

                // Act
                ResponseEntity<?> response = authController.orcidLogin(request);

                // Assert
                assertThat(response.getStatusCode())
                        .isEqualTo(HttpStatus.UNAUTHORIZED);

                verify(orcidService).exchangeCode("auth-code");

                verify(userAuthenticationRepository, never())
                        .findByProviderAndProviderSubjectId(
                                any(AuthenticationProvider.class),
                                any(String.class));

                verify(userRepository, never())
                        .save(any(UserEntity.class));

                verify(userAuthenticationRepository, never())
                        .save(any(UserAuthenticationEntity.class));
        }
} 