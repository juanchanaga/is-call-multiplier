/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ClientInternalDetails implements Serializable {

    private static final long serialVersionUID = 738654598772323112L;


    private Integer maxDailyAttempts;

    @Deprecated
    private Boolean answeringMachineDetection;

    private Integer callRecordingDays;

    private Boolean encryptCcsPassword;

    private Integer maxFailedCcsLoginAttempts;

    private Boolean encryptAcdPassword;

    private Integer maxFailedAcdLoginAttempts;

    private Integer passwordExpirationDays;
}
