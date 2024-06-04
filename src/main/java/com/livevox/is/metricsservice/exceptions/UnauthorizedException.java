/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.exceptions;

public class UnauthorizedException extends AtgRuntimeException {
    private static final long serialVersionUID = 4705599121202095321L;

    public UnauthorizedException() {
    }

    public UnauthorizedException(String errorMsg) {
        super(errorMsg);
    }

    public int getHttpStatus() {
        return 401;
    }

    public String getError() {
        return "Session Token Unauthorized";
    }
}
