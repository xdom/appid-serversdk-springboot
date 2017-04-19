package com.ibm.mobilefirstplatform.appid;

import org.springframework.security.core.Authentication;

/**
 * @author Dominik Matta
 */
final class NullAppIDTokens implements AppIDTokens {
    @Override
    public Authentication getAuthentication() {
        throw new UnsupportedOperationException("Invalid token, cannot create authentication");
    }

    @Override
    public boolean validate() {
        return false;
    }
}
