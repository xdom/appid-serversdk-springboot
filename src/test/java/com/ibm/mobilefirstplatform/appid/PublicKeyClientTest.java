package com.ibm.mobilefirstplatform.appid;

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
    @Mock
    private RestTemplate restTemplate;

    @Test
    public void shouldRetrievePubKey() throws Exception {
        // given
        ResponseEntity<PublicKeyClient.PublicKeyResponse> responseEntity = new ResponseEntity<>(
                PublicKeyClientTestConfig.DUMMY_RESPONSE, HttpStatus.OK
        );
        when(restTemplate.getForEntity(
                eq(PublicKeyClientTestConfig.DUMMY_WEB_ADDRESS + PublicKeyClient.PUBLIC_KEY_PATH),
                eq(PublicKeyClient.PublicKeyResponse.class)
        )).thenReturn(responseEntity);

        // when
        PublicKeyClient client = new PublicKeyClient(PublicKeyClientTestConfig.DUMMY_WEB_ADDRESS, restTemplate);
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
                startsWith(PublicKeyClientTestConfig.DUMMY_WEB_ADDRESS), eq(PublicKeyClient.PublicKeyResponse.class)
        )).thenReturn(responseEntity);

        // when
        PublicKeyClient client = new PublicKeyClient(PublicKeyClientTestConfig.DUMMY_WEB_ADDRESS, restTemplate);
        client.retrievePublicKey();

        // then PublicKeyRetrievalException is thrown
    }
}