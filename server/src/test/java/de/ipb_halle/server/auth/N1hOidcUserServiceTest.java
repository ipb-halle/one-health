package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.models.UserEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class N1hOidcUserServiceTest {

    private OrcidUserService orcidUserService;
    private OidcUserRequest userRequest;
    private ClientRegistration clientRegistration;
    private OidcUser oidcUser;

    @BeforeEach
    void setUp() {
        orcidUserService = mock(OrcidUserService.class);
        userRequest = mock(OidcUserRequest.class);
        clientRegistration = mock(ClientRegistration.class);
        oidcUser = mock(OidcUser.class);

        when(userRequest.getClientRegistration())
                .thenReturn(clientRegistration);

        when(clientRegistration.getRegistrationId())
                .thenReturn("orcid");
    }

    // Verifies that a disabled local account cannot authenticate
    // through ORCID and its last login is not updated.
    @Test
    void shouldRejectDisabledUser() {

        UserEntity user = new UserEntity();
        user.setEnabled(false);

        when(orcidUserService.provisionUser(oidcUser))
                .thenReturn(user);

        try (MockedConstruction<OidcUserService> mocked = mockConstruction(OidcUserService.class)) {

            N1hOidcUserService service = new N1hOidcUserService(orcidUserService);

            OidcUserService delegate = mocked.constructed().get(0);

            when(delegate.loadUser(userRequest))
                    .thenReturn(oidcUser);

            assertThrows(
                    DisabledException.class,
                    () -> service.loadUser(userRequest));

            verify(orcidUserService).provisionUser(oidcUser);

            verify(orcidUserService, never())
                    .updateLastLogin(any(UserEntity.class));
        }
    }

    // Verifies that an enabled local account can authenticate
    // through ORCID and its last login is updated.
    @Test
    void shouldAllowEnabledUserAndUpdateLastLogin() {

        UserEntity user = new UserEntity();
        user.setEnabled(true);

        when(orcidUserService.provisionUser(oidcUser))
                .thenReturn(user);

        try (MockedConstruction<OidcUserService> mocked = mockConstruction(OidcUserService.class)) {

            N1hOidcUserService service = new N1hOidcUserService(orcidUserService);

            OidcUserService delegate = mocked.constructed().get(0);

            when(delegate.loadUser(userRequest))
                    .thenReturn(oidcUser);

            OidcUser result = service.loadUser(userRequest);

            assertSame(oidcUser, result);

            verify(orcidUserService).provisionUser(oidcUser);
            verify(orcidUserService).updateLastLogin(user);
        }
    }
}
