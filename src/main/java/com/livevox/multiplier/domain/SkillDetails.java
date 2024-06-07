/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain;

import com.livevox.multiplier.domain.stats.Skill;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
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
