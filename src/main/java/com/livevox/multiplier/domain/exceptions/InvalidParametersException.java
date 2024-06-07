/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.exceptions;

public class InvalidParametersException extends AtgRuntimeException {
    private static final long serialVersionUID = -3049907542861626178L;

    public int getHttpStatus() {
        return 400;
    }

    public String getError() {
        return "Invalid rest parameters";
    }

    public InvalidParametersException() {
    }

    public InvalidParametersException(String message) {
        super(message);
        this.setErrorMsg(message);
    }

    public InvalidParametersException(Exception e) {
        super(e);
    }

    public InvalidParametersException(Throwable t) {
        super(t);
    }
}

