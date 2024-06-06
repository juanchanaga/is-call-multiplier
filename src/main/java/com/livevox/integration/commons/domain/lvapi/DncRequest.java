/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.enums.DncType;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DncRequest extends ListRequest implements Serializable {

    private static final long serialVersionUID = 672375166194013510L;


    private Long id;

    private Integer clientId;

    private Long serviceId;

    private String phone;

    private String account;

    private String zip;

    private DncType type;
    //
    //   This is only for the shared services call multiplier
    //
    private String application;


    public DncRequest() {}


    public  DncRequest(CallDetail callDetail) {
        this();
        if (callDetail == null) {
            return;
        }
        setAccount(callDetail.getAccount());
        setClientId(callDetail.getClientId());
        setPhone(callDetail.getPhoneDialed());
        setServiceId(callDetail.getServiceId());
        setApiSessionId(callDetail.getApiSessionId());
        setId(callDetail.getId());
        setZip(callDetail.getZipCode());
    }


    public  DncRequest(CallDetail callDetail, DncType dncType) {
        this(callDetail);
        if (dncType == null || dncType.equals(DncType.DAILY) ) {
            this.type = DncType.DAILY;
        } else {
            setType(dncType);
        }
    }
}