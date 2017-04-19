package com.ibm.mobilefirstplatform.appid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

/**
 * @author Dominik Matta
 */
@Component
class PublicKeyProvider {
    private static final Logger LOG = LoggerFactory.getLogger(PublicKeyProvider.class);
    private final PublicKey publicKey;

    @Autowired
    PublicKeyProvider(PublicKeyClient publicKeyClient) {
        this.publicKey = publicKeyClient.retrievePublicKey();
    }

    PublicKey getPublicKey() {
        return publicKey;
    }
}
