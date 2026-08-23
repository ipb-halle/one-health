package de.ipb_halle.server.api;

import de.ipb_halle.api.AuthApi;
import de.ipb_halle.model.LoginResponse;
import de.ipb_halle.model.LogoutResponse;
import de.ipb_halle.model.OrcidAuthUrlResponse;
import de.ipb_halle.model.OrcidTokenRequest;
import de.ipb_halle.model.User;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping(path = "api")
@CrossOrigin(origins = "*")
public class AuthController implements AuthApi {

    @Value("${orcid.client-id}")
    private String clientId;

    @Value("${orcid.authorize-url}")
    private String authorizeUrl;

    @Value("${orcid.redirect-uri}")
    private String redirectUri;

    @Override
    public ResponseEntity<User> getCurrentUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentUser'");
    }

    @Override
    public ResponseEntity<OrcidAuthUrlResponse> getOrcidAuthorizeUrl() {

        String state = UUID.randomUUID().toString();

        String url = authorizeUrl
                + "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                + "&response_type=code"
               // + "&scope=%2Fauthenticate"
                + "&scope=openid"
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8);

        return ResponseEntity.ok(
                new OrcidAuthUrlResponse(URI.create(url), state));
    }

    @Override
    public ResponseEntity<LogoutResponse> logout() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }

    @Override
    public ResponseEntity<LoginResponse> orcidLogin(@Valid OrcidTokenRequest orcidTokenRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orcidLogin'");
    }

}
