/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.domain;

import com.livevox.integration.commons.domain.stats.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SkillRelatedDetails implements Serializable {

    private static final long serialVersionUID = 672375166194013510L;

    private IdType inboundResourceGroup;
    private List<IdType> agent;
    private List<IdType> campaign;
    private List<IdType> outboundResourceGroup;
    private IdType callCenter;
}
