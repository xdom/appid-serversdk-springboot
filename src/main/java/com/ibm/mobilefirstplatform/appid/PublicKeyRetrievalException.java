package com.ibm.mobilefirstplatform.appid;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Dominik Matta
 */
public class PublicKeyRetrievalException extends NestedRuntimeException {
    public PublicKeyRetrievalException(String msg) {
        super(msg);
    }

    public PublicKeyRetrievalException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
