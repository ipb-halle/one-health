package de.ipb_halle.server.api;

import de.ipb_halle.api.AuthApi;
import de.ipb_halle.model.LoginResponse;
import de.ipb_halle.model.LogoutResponse;
import de.ipb_halle.model.OrcidAuthUrlResponse;
import de.ipb_halle.model.OrcidTokenRequest;
import de.ipb_halle.model.User;
import de.ipb_halle.server.auth.JwtService;
import de.ipb_halle.server.auth.OrcidService;
import de.ipb_halle.server.postgre.mapping.UserMapper;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.models.UserRole;
import de.ipb_halle.server.postgre.repositories.UserRepository;

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

    private final UserRepository userRepository;
    private final OrcidService orcidService;
    private final JwtService jwtService;

    public AuthController(
            UserRepository userRepository,
            OrcidService orcidService,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.orcidService = orcidService;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<OrcidAuthUrlResponse> getOrcidAuthorizeUrl() {

        String state = UUID.randomUUID().toString();

        String url = authorizeUrl
                + "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                + "&response_type=code"
                + "&scope=openid"
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8);

        return ResponseEntity.ok(
                new OrcidAuthUrlResponse(URI.create(url), state));
    }

    @Override
    public ResponseEntity<User> getCurrentUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentUser'");
    }

    @Override
    public ResponseEntity<LogoutResponse> logout() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }

    @Override
    public ResponseEntity<LoginResponse> orcidLogin(
            @Valid OrcidTokenRequest orcidTokenRequest) {
        OrcidService.OrcidTokenResponse orcidResponse = orcidService.exchangeCode(orcidTokenRequest.getCode());

        String orcid = orcidResponse.getOrcid();
        if (orcid == null || orcid.isBlank()) {
            return ResponseEntity.status(401).build();
        }

        UserEntity userEntity = userRepository
                .findByOrcid(orcid)
                .orElseGet(() -> {
                    UserEntity newUserEntity = new UserEntity();

                    newUserEntity.setOrcid(orcid);
                    newUserEntity.setDisplayName(orcidResponse.getName());
                    newUserEntity.setEmail(null);
                    newUserEntity.setRole(UserRole.CURATOR);
                    newUserEntity.setEnabled(true);
                    return userRepository.save(newUserEntity);
                });

        if (!Boolean.TRUE.equals(userEntity.getEnabled())) {
            return ResponseEntity.status(401).build();
        }

        String jwt = jwtService.generateToken(userEntity);

        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setUser(UserMapper.MAPPER.toDto(userEntity));

        return ResponseEntity.ok(response);
    }

}
