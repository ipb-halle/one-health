package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.models.UserEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class N1hOidcUserService
        implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcUserService delegate = new OidcUserService();
    private final OrcidUserService orcidUserService;

    public N1hOidcUserService(OrcidUserService orcidUserService) {
        this.orcidUserService = orcidUserService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest)
            throws OAuth2AuthenticationException {

        OidcUser oidcUser = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (!"orcid".equals(registrationId)) {
            throw new OAuth2AuthenticationException(
                    "Unsupported OIDC provider: " + registrationId);
        }

        UserEntity user = orcidUserService.provisionUser(oidcUser);

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new DisabledException("N1H user account is disabled");
        }

        return oidcUser;
    }
}