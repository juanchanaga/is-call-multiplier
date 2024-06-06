/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.shareddata.multiplier.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.commons.services.rest.domain.RestFilter;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.beanutils.BeanUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ToString( callSuper = true)
public class AgentRequest extends com.livevox.commons.services.rest.domain.AgentRequest {

    private static final long serialVersionUID = 5786255260784754L;

    private RestFilter filter;

    public AgentRequest copy() {
        try {
            AgentRequest req = new AgentRequest();
            BeanUtils.copyProperties(req, this);
            return req;
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
