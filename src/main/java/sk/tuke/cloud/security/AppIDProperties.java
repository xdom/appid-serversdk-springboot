package sk.tuke.cloud.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Dominik Matta
 */
@ConfigurationProperties(prefix = "appid")
public class AppIDProperties {
    private String oauthServerUrl;

    public String getOauthServerUrl() {
        return oauthServerUrl;
    }

    public void setOauthServerUrl(String oauthServerUrl) {
        this.oauthServerUrl = oauthServerUrl;
    }
}
