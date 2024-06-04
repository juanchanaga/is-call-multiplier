/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.domain;

import com.livevox.integration.commons.domain.stats.Skill;
import lombok.Data;

import java.util.List;

@Data
public class SkillPhoneSettings extends Skill {


    /**
     *
     */
    private static final long serialVersionUID = 2171825530549630740L;
    //Read Phone Settings
    private String operatorPhone;
    private String callbackPhone;
    private String agentCallInNumber;
    private String defaultCallerId;
    private List<String> callerId;
}
