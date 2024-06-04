/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.domain;

import com.livevox.integration.commons.domain.stats.Skill;
import lombok.Data;

@Data
public class SkillDetails extends Skill {

    /**
     *
     */
    private static final long serialVersionUID = -8811057472399632660L;
    //General Settings
    private String  abbreviation;
    private Boolean unattended;
    private Boolean termCodeEnabled;
    private String acdFeatureCode;
    private Integer inboundService;
    private String callDirection;
    private String previewMode;
}
