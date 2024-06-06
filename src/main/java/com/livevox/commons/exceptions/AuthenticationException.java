/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.exceptions;

public class AuthenticationException extends AtgRuntimeException {
    private static final long serialVersionUID = 599121202095321L;

    public AuthenticationException() {
    }

    public AuthenticationException(String errorMsg) {
        super(errorMsg);
    }
}
