package de.ipb_halle.server.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrcidService {

    private final RestClient restClient;

    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl;
    private final String redirectUri;

    public OrcidService(
            @Value("${orcid.client-id}") String clientId,
            @Value("${orcid.client-secret}") String clientSecret,
            @Value("${orcid.token-url}") String tokenUrl,
            @Value("${orcid.redirect-uri}") String redirectUri) {

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUrl = tokenUrl;
        this.redirectUri = redirectUri;

        this.restClient = RestClient.builder().build();
    }

    public OrcidTokenResponse exchangeCode(String code) {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("redirect_uri", redirectUri);

        return restClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(OrcidTokenResponse.class);
    }

    public static class OrcidTokenResponse {

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("orcid")
        private String orcid;

        @JsonProperty("name")
        private String name;
        
        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("scope")
        private String scope;

        @JsonProperty("expires_in")
        private Long expiresIn;

        public String getAccessToken() {
            return accessToken;
        }

        public String getOrcid() {
            return orcid;
        }

        public String getName() {
            return name;
        }

        public String getTokenType() {
            return tokenType;
        }

        public String getScope() {
            return scope;
        }

        public Long getExpiresIn() {
            return expiresIn;
        }
    }
}
