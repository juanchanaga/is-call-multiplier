/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CallHistory implements Serializable {

    private Person person;
    private Integer callCenterId;
    private Integer serviceId;
    private Integer agentId;
    private String phone;
    private String account;
    private String campaignName;
    private String lvResult;
    private Date start;
    private Date end;
}
