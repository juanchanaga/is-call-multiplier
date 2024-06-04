/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.exceptions;

public class MissingFieldsException extends AtgRuntimeException {
    private static final long serialVersionUID = 3391511328988187009L;

    public MissingFieldsException() {
    }

    public int getHttpStatus() {
        return 406;
    }

    public String getError() {
        return "Missing Required Fields";
    }
}
