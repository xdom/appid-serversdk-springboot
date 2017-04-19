package sk.tuke.cloud.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServerSdkApplicationTests {
    private static final String CORRECT_AUTH_TOKEN = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2p3dC1pZHAuZXhhbXBsZS5jb20iLCJzdWIiOiJtYWlsdG86bWlrZUBleGFtcGxlLmNvbSIsIm5iZiI6MTQ5MjU5ODY1NCwiZXhwIjoxNDkyNjAyMjU0LCJpYXQiOjE0OTI1OTg2NTQsImp0aSI6ImlkMTIzNDU2IiwidHlwIjoiaHR0cHM6Ly9leGFtcGxlLmNvbS9yZWdpc3RlciJ9.D9eq_ntTdO5AWvJzKIgZ734iGLoa10LtFPX2HWXNZ37Jrb2xK7H2NwNqOHiIsCm_MZboewQB9ki-gKZ_kPIpx7eSV9qMyG8FdS4gDmF_g4WhCFx_Jgg_5TPfl6s0ELr_uPdtShns63lUDKdII2hBdvkmZfOeF8j2mQLsRU3ACVErtg1KNAT7XElrMSht_XSp0VXkVKmIzl1Eha3xW3TBKGKSqPlySlSSSQb9gBx4mHMALcL1bMNcH47lAF7rA5EkRuxVJdOshZU4_5nTHoSnXgcelBS85izA3GoQTrqLkLUv4ngiJ_8lQ4ByVIXJsJ5lYNSHEWM4hBqrGz0DDHw3uw";
    private static final String INVALID_AUTH_TOKEN = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2p3dC1pZHAuZXhhbXBsZS5jb20iLCJzdWIiOiJtYWlsdG86bWlrZUBleGFtcGxlLmNvbSIsIm5iZiI6MTQ5MjYwMTg4OCwiZXhwIjoxNDkyNjA1NDg4LCJpYXQiOjE0OTI2MDE4ODgsImp0aSI6ImlkMTIzNDU2IiwidHlwIjoiaHR0cHM6Ly9leGFtcGxlLmNvbS9yZWdpc3RlciJ9.YNXLuwOmzibvPM4AixYX9Wbqe8hYCdNy0n0-kHy7syzlLdU7HsaozptBgNeBL389bqXtPFiKNraxbHDfD1UaopBqHkxG4Cc8kU6p4buyAKksLsaVYAYO1s8l18x21CwXObPMumm4NGbLK7dDBOc-z_KBicsURZbZl9SbSf_r50LzfmEL7BDn2OyJmGZXhPfSslPUiu27AAXoEFq7zG3TVjz9h6FugZ-0sCRRr77VfMONCftvVsFHITrXx1lqiDogs2CRPHG2gkGmqVrknrJUKfmvmx3Yiu8cL46nQjFuD0d0Ke7uB7XMg9TqrWXrzhgCWa9zkcmgTcVb1CbrMRhXtg";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldDenyAccessWithoutToken() throws Exception {
        ResponseEntity<String> res = restTemplate.getForEntity("/something", String.class);
        assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
    }

    @Test
    public void shouldDenyAccessWithInvalidToken() throws Exception {
        ResponseEntity<String> res = requestForSomething(INVALID_AUTH_TOKEN);
        assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
    }

    @Test
    public void shouldPermitAccessWithValidToken() throws Exception {
        ResponseEntity<String> res = requestForSomething(CORRECT_AUTH_TOKEN);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals("works", res.getBody());
    }

    private ResponseEntity<String> requestForSomething(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, authToken);
        HttpEntity req = new HttpEntity(headers);
        return restTemplate.exchange("/something", HttpMethod.GET, req, String.class);
    }
}