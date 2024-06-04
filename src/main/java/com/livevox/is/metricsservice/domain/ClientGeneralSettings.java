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
public class ClientGeneralSettings implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1977330959116773713L;

    private String displayName;
    private String clientCode;
    private Integer defaultScreenPopId;
    private Integer defaultDialingProfileId;
    private Integer serviceLevelSeconds;
    private Boolean restrictStrategiesScope;
    private Integer defaultStrategyId;
    private Integer requeueStrategyId;
    private Integer defaultContactTimingId;
    private Integer defaultVoiceId;
    private String answeringMachineOption;
    private Boolean scrubWireless;
    private Boolean acdResourceGroupEnabled;
    private Boolean acdAgentSkillEnabled;
    private Boolean acctRealtimeDnc;
    private Integer lcidPackageId;
    private Boolean reportOnlyAfterAllRetries;
}
