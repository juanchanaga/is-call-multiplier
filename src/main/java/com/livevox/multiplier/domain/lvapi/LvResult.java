/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.stats.DefaultDao;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class LvResult extends DefaultDao {

    private static final long serialVersionUID = 7037809477861686497L;
    private String name;
    private Integer priority;
    private String resultType;
    private Boolean notConnected;
    private Boolean operatorTransfer;
    private Boolean notMade;
    private Boolean operatorTransferSuccessful;
    private Boolean attendedServicesOnly;
    private Boolean requeueable;
    private Boolean liveAnswer;
    private String agentActionType;

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, true);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, true);
    }
}