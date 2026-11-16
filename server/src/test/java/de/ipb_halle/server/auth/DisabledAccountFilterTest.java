
package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.models.AuthenticationProvider;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.FilterChain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DisabledAccountFilterTest {

    private UserAuthenticationRepository repository;
    private DisabledAccountFilter filter;
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;

    private static final String ORCID = "0009-0005-1075-0774";

    @BeforeEach
    void setUp() {
        repository = mock(UserAuthenticationRepository.class);
        filter = new DisabledAccountFilter(repository);
        filterChain = mock(FilterChain.class);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
        request.setSession(session);

        OidcUser oidcUser = mock(OidcUser.class);
        when(oidcUser.getSubject()).thenReturn(ORCID);

        TestingAuthenticationToken authentication = new TestingAuthenticationToken(
                oidcUser, "credentials", "ROLE_USER");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }

    @AfterEach
    void cleanUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueWhenAccountIsEnabled() throws Exception {
        UserEntity user = new UserEntity();
        user.setEnabled(true);

        UserAuthenticationEntity userAuthentication = new UserAuthenticationEntity();
        userAuthentication.setUser(user);

        when(repository.findByProviderAndProviderSubjectId(
                AuthenticationProvider.ORCID, ORCID))
                .thenReturn(Optional.of(userAuthentication));

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertEquals(200, response.getStatus());
        assertFalse(session.isInvalid());
    }

    @Test
    void shouldRejectAndInvalidateSessionWhenAccountIsDisabled()
            throws Exception {
        UserEntity user = new UserEntity();
        user.setEnabled(false);

        UserAuthenticationEntity userAuthentication = new UserAuthenticationEntity();
        userAuthentication.setUser(user);

        when(repository.findByProviderAndProviderSubjectId(
                AuthenticationProvider.ORCID, ORCID))
                .thenReturn(Optional.of(userAuthentication));

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertEquals(403, response.getStatus());
        assertTrue(session.isInvalid());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldRejectAndInvalidateSessionWhenAccountDoesNotExist()
            throws Exception {
        when(repository.findByProviderAndProviderSubjectId(
                AuthenticationProvider.ORCID, ORCID))
                .thenReturn(Optional.empty());

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        assertEquals(403, response.getStatus());
        assertTrue(session.isInvalid());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
