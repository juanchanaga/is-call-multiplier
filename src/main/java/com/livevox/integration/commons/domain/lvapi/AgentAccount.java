/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AgentAccount implements Serializable {

    private String callCenter;
    private String service;
    private String customerName;
    private String originalAccountNumber;
    private String account;
    private String phone;
    private String agent;
    private Date start;
    private Date end;
    private String campaign;
    private String callerId;
    private String termCode;
    private Integer serviceId;
    private Integer callCenterId;
}
