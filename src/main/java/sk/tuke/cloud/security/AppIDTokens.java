package sk.tuke.cloud.security;

import org.springframework.security.core.Authentication;

/**
 * @author Dominik Matta
 */
interface AppIDTokens {
    Authentication getAuthentication();

    boolean validate();
}
