package de.ipb_halle.server.auth;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.core.GrantedAuthority;

import de.ipb_halle.server.postgre.models.UserRole;

import java.util.Collection;
import java.util.Map;

/**
 * Wraps an underlying OidcUser and adds N1H-specific information.
 * This allows us to carry database user context (ID and role) throughout
 * the authentication flow while maintaining provider isolation.
 */
public class CustomOidcUser implements OidcUser {

    private final OidcUser delegate;
    private final Long userId;
    private final UserRole userRole;

    public CustomOidcUser(OidcUser delegate, Long userId, UserRole userRole) {
        this.delegate = delegate;
        this.userId = userId;
        this.userRole = userRole;
    }

    // N1H-specific accessors
    public Long getUserId() {
        return userId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    // OidcUser delegate methods
    @Override
    public Map<String, Object> getClaims() {
        return delegate.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return delegate.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return delegate.getIdToken();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public String getSubject() {
        return delegate.getSubject();
    }

    @Override
    public String getFullName() {
        return delegate.getFullName();
    }

    @Override
    public String getGivenName() {
        return delegate.getGivenName();
    }

    @Override
    public String getFamilyName() {
        return delegate.getFamilyName();
    }

    @Override
    public String getMiddleName() {
        return delegate.getMiddleName();
    }

    @Override
    public String getNickName() {
        return delegate.getNickName();
    }

    @Override
    public String getPreferredUsername() {
        return delegate.getPreferredUsername();
    }

    @Override
    public String getProfile() {
        return delegate.getProfile();
    }

    @Override
    public String getPicture() {
        return delegate.getPicture();
    }

    @Override
    public String getWebsite() {
        return delegate.getWebsite();
    }

    @Override
    public String getEmail() {
        return delegate.getEmail();
    }

    @Override
    public Boolean getEmailVerified() {
        return delegate.getEmailVerified();
    }

    @Override
    public String getGender() {
        return delegate.getGender();
    }

    @Override
    public String getBirthdate() {
        return delegate.getBirthdate();
    }

    @Override
    public String getLocale() {
        return delegate.getLocale();
    }

    @Override
    public String getPhoneNumber() {
        return delegate.getPhoneNumber();
    }

    @Override
    public Boolean getPhoneNumberVerified() {
        return delegate.getPhoneNumberVerified();
    }
}
