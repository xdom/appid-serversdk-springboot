package sk.tuke.cloud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Dominik Matta
 */
@Configuration
public class PublicKeyClientTestConfig {
    static final String DUMMY_WEB_ADDRESS = "https://dummy.webaddress.com/something";
    static final PublicKeyClient.PublicKeyResponse DUMMY_RESPONSE = new PublicKeyClient.PublicKeyResponse(
            "RSA",
            "AODINNiGjJ94Oy9XDwZL2nGAuqxhwz2cSCGjCAP7TC0fsazxJ+ut0zpNuznBhynTP0Cn5aCm3UYDr+nkb3HIoBXJaLjUghUG44+Co5fLA+qyLmTsacb8FdP277L0hZiq0/iV4yjb7/raC+bEgxtDG9U12sCT0txwspufs0yBp+ddpZ6i4z7BreSiMjKPRGmhIg0bspYZRgTRivbTlIWVUKNS+F6zd+zT0fRSxejOTO80igrC/ybzPXSfUAp6c3cLA2dfiQuDEM97EAPQxNQcSgkMthcxyVJLcUIpXuMpNWB5FZeYnLIdsRPs4burf2DzOTSMq5+x/k/QW33vOFmjqaM=",
            "AQAB"
    );

    @Bean
    public PublicKeyClient publicKeyClient() {
        RestTemplate mockRestTemplate = restTemplate();
        return new PublicKeyClient(DUMMY_WEB_ADDRESS, mockRestTemplate);
    }

    private RestTemplate restTemplate() {
        ResponseEntity<PublicKeyClient.PublicKeyResponse> responseEntity = new ResponseEntity<>(
                DUMMY_RESPONSE, HttpStatus.OK
        );
        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        when(mockRestTemplate.getForEntity(
                eq(DUMMY_WEB_ADDRESS + PublicKeyClient.PUBLIC_KEY_PATH),
                eq(PublicKeyClient.PublicKeyResponse.class)
        )).thenReturn(responseEntity);
        return mockRestTemplate;
    }
}
