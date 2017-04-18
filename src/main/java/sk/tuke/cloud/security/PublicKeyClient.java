package sk.tuke.cloud.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sun.security.rsa.RSAPublicKeyImpl;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.util.Base64;

/**
 * @author Dominik Matta
 */
@Component
public class PublicKeyClient {
    public static final String PUBLIC_KEY_PATH = "/publickey";

    private static final Logger LOG = LoggerFactory.getLogger(PublicKeyClient.class);
    private final String url;
    private final RestTemplate restTemplate;

    public PublicKeyClient(AppIDProperties properties) {
        this(properties.getOauthServerUrl(), new RestTemplate());
    }

    PublicKeyClient(String oauthServerUrl, RestTemplate restTemplate) {
        this.url = oauthServerUrl + PUBLIC_KEY_PATH;
        this.restTemplate = restTemplate;
    }

    public PublicKey retrievePublicKey() throws PublicKeyRetrievalException {
        try {
            LOG.debug("Getting public key from {}", url);
            ResponseEntity<PublicKeyResponse> response = restTemplate.getForEntity(
                    url, PublicKeyResponse.class
            );
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new PublicKeyRetrievalException(
                        "Failed to retrieve public key (server response " + response.getStatusCode() + ")"
                );
            }
            return createPublicKey(response);
        } catch (RestClientException | InvalidKeyException e) {
            throw new PublicKeyRetrievalException("Failed to retrieve public key", e);
        }
    }

    private PublicKey createPublicKey(ResponseEntity<PublicKeyResponse> response) throws InvalidKeyException {
        LOG.info("Public key data retrieved, converting to RSA public key");
        PublicKeyResponse body = response.getBody();
        BigInteger base = new BigInteger(body.getN());
        BigInteger power = new BigInteger(body.getE());
        return new RSAPublicKeyImpl(base, power);
    }

    static class PublicKeyResponse {
        private String kty;
        private byte[] n;
        private byte[] e;

        PublicKeyResponse(@JsonProperty("kty") String kty,
                          @JsonProperty("n") String encodedN,
                          @JsonProperty("e") String encodedE) {
            this.kty = kty;
            this.n = Base64.getDecoder().decode(encodedN);
            this.e = Base64.getDecoder().decode(encodedE);
        }

        String getKty() {
            return kty;
        }

        byte[] getN() {
            return n;
        }

        byte[] getE() {
            return e;
        }
    }
}
