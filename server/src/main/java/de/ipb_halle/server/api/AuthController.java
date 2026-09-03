package de.ipb_halle.server.api;

import de.ipb_halle.api.AuthApi;
import de.ipb_halle.model.LoginResponse;
import de.ipb_halle.model.LogoutResponse;
import de.ipb_halle.model.OrcidAuthUrlResponse;
import de.ipb_halle.model.OrcidTokenRequest;
import de.ipb_halle.model.User;
import de.ipb_halle.server.auth.OrcidService;
import de.ipb_halle.server.postgre.mapping.UserMapper;
import de.ipb_halle.server.postgre.models.UserEntity;
import de.ipb_halle.server.postgre.models.UserRole;
import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import de.ipb_halle.server.postgre.repositories.UserRepository;
import de.ipb_halle.server.postgre.repositories.UserAuthenticationRepository;

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
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final OrcidService orcidService;

    public AuthController(
            UserRepository userRepository,
            UserAuthenticationRepository userAuthenticationRepository,
            OrcidService orcidService) {

        this.userRepository = userRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.orcidService = orcidService;
    }

    @Override
    public ResponseEntity<OrcidAuthUrlResponse> getOrcidAuthorizeUrl() {

        String state = UUID.randomUUID().toString();

        String url = authorizeUrl
                + "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                + "&response_type=code"
                + "&scope=openid"
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8)
                + "&prompt=login";

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
        return ResponseEntity.ok(new LogoutResponse("Logged out successfully"));
    }

    @Override
    public ResponseEntity<LoginResponse> orcidLogin(
            @Valid OrcidTokenRequest orcidTokenRequest) {
        OrcidService.OrcidTokenResponse orcidResponse = orcidService.exchangeCode(orcidTokenRequest.getCode());

        String orcid = orcidResponse.getOrcid();
        if (orcid == null || orcid.isBlank()) {
            return ResponseEntity.status(401).build();
        }

        UserEntity userEntity = userAuthenticationRepository
                .findByOrcidId(orcid)
                .map(authentication -> {
                    UserEntity existingUser = authentication.getUser();
                    existingUser.setDisplayName(orcidResponse.getName());
                    userRepository.save(existingUser);

                    authentication.setAccessToken(orcidResponse.getAccessToken());

                    if (orcidResponse.getExpiresIn() != null) {
                        authentication.setExpiresAt(
                                java.time.LocalDateTime.now()
                                        .plusSeconds(orcidResponse.getExpiresIn()));
                    }

                    userAuthenticationRepository.save(authentication);
                    return existingUser;
                })
                .orElseGet(() -> {
                    UserEntity newUserEntity = new UserEntity();
                    newUserEntity.setDisplayName(orcidResponse.getName());
                    newUserEntity.setRole(UserRole.VIEWER);
                    newUserEntity.setEnabled(true);

                    UserEntity savedUser = userRepository.save(newUserEntity);

                    UserAuthenticationEntity authentication = new UserAuthenticationEntity();
                    authentication.setUser(savedUser);
                    authentication.setRegistrationMethod("ORCID");
                    authentication.setOrcidId(orcid);
                    authentication.setAccessToken(orcidResponse.getAccessToken());

                    if (orcidResponse.getExpiresIn() != null) {
                        authentication.setExpiresAt(
                                java.time.LocalDateTime.now()
                                        .plusSeconds(orcidResponse.getExpiresIn()));
                    }

                    userAuthenticationRepository.save(authentication);
                    return savedUser;
                });

        if (!Boolean.TRUE.equals(userEntity.getEnabled())) {
            return ResponseEntity.status(401).build();
        }

        LoginResponse response = new LoginResponse();
        response.setToken(orcidResponse.getAccessToken());
        if (orcidResponse.getExpiresIn() != null) {
            response.setExpiresInSeconds(orcidResponse.getExpiresIn().intValue());
        }
        response.setUser(UserMapper.MAPPER.toDto(userEntity));

        return ResponseEntity.ok(response);
    }

}
