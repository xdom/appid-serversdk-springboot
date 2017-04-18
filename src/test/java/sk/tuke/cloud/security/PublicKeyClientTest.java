package sk.tuke.cloud.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.when;

/**
 * @author Dominik Matta
 */
@RunWith(MockitoJUnitRunner.class)
public class PublicKeyClientTest {
    private static final String DUMMY_WEB_ADDRESS = "https://dummy.webaddress.com/something";
    @Mock
    private RestTemplate restTemplate;

    @Test
    public void shouldRetrievePubKey() throws Exception {
        // given
        PublicKeyClient.PublicKeyResponse response = new PublicKeyClient.PublicKeyResponse(
                "RSA",
                "AJ+E8O4KJT6So/lUkCIkU0QKW7QjMp9vG7S7vZx0M399idZ4mP7iWWW6OTvjLHpDTx7uapiwRQktDNx3" +
                        "GHigJDmbbu8/VtS5K6J6be1gVrvu6pxmZtrz8PazlH5WYxkuUIfUYpzyfUubZzqzuVWqQO0W" +
                        "9kOhFN7HILAxb1WsQREX+iLg14MGGafrQnJgXHBAwSH0OOJr7v+nRz8AFCAicN8v0uIar9lR" +
                        "A7JRHQCZtpI/lkSGKKBQT1Zae9+9YlWbZlfXErQS1uYoAb3j3uaLbJVO7SNjQqEsRTjYxfpB" +
                        "sTtkvJmwcwA0wV2gBO3JR6K6ep0Y/KyMR8w9Fd/lvJqdltU=",
                "AQAB"
        );
        ResponseEntity<PublicKeyClient.PublicKeyResponse> responseEntity = new ResponseEntity<>(
                response, HttpStatus.OK
        );
        when(restTemplate.getForEntity(
                startsWith(DUMMY_WEB_ADDRESS), eq(PublicKeyClient.PublicKeyResponse.class)
        )).thenReturn(responseEntity);

        // when
        PublicKeyClient client = new PublicKeyClient(DUMMY_WEB_ADDRESS, restTemplate);
        PublicKey publicKey = client.retrievePublicKey();

        // then
        assertNotNull(publicKey);
        assertEquals("RSA", publicKey.getAlgorithm());
    }

    @Test(expected = PublicKeyRetrievalException.class)
    public void shouldThrowExceptionOnServerError() throws Exception {
        // given
        ResponseEntity<PublicKeyClient.PublicKeyResponse> responseEntity = new ResponseEntity<>(
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        when(restTemplate.getForEntity(
                startsWith(DUMMY_WEB_ADDRESS), eq(PublicKeyClient.PublicKeyResponse.class)
        )).thenReturn(responseEntity);

        // when
        PublicKeyClient client = new PublicKeyClient(DUMMY_WEB_ADDRESS, restTemplate);
        client.retrievePublicKey();

        // then PublicKeyRetrievalException is thrown
    }
}