package com.ibm.mobilefirstplatform.appid;

import org.springframework.security.core.Authentication;

/**
 * @author Dominik Matta
 */
interface AppIDTokens {
    Authentication getAuthentication();

    boolean validate();
}
