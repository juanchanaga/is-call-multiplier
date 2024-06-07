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
public class ManualCall implements Serializable {

    private static final long serialVersionUID = 5738651873437445112L;

    private Long id;
    private String account;
    private String phoneNumber;
    private Long transactionId;
    private String tfhResultId;
    private String lvResultCode;
    private Long templateId;
    private String callerId;
    private Integer errorCode;
    private String reason;
    private Date timeOfFailure;


    public ManualCall(String account, String phoneDialed) {
        setAccount(account);
        setPhoneNumber(phoneDialed);
    }


    public ManualCall(String account, String phoneDialed, Long transactionId) {
        this(account, phoneDialed);
        setTransactionId(transactionId);
    }


    public ManualCall(String account, String phoneDialed, Long templateid, String callerId) {
        this(account, phoneDialed);
        setTemplateId(templateid);
        setCallerId(callerId);
    }


    public ManualCall(String account, String phoneDialed, Long templateid, Long transactionId, String callerId) {
        this(account, phoneDialed, templateid, callerId);
        setTransactionId(transactionId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ManualCall other = (ManualCall) obj;
        if (account == null) {
            if (other.account != null)
                return false;
        } else if (!account.equals(other.account))
            return false;
        if (callerId == null) {
            if (other.callerId != null)
                return false;
        } else if (!callerId.equals(other.callerId))
            return false;
        if (errorCode == null) {
            if (other.errorCode != null)
                return false;
        } else if (!errorCode.equals(other.errorCode))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (lvResultCode == null) {
            if (other.lvResultCode != null)
                return false;
        } else if (!lvResultCode.equals(other.lvResultCode))
            return false;
        if (phoneNumber == null) {
            if (other.phoneNumber != null)
                return false;
        } else if (!phoneNumber.equals(other.phoneNumber))
            return false;
        if (reason == null) {
            if (other.reason != null)
                return false;
        } else if (!reason.equals(other.reason))
            return false;
        if (templateId == null) {
            if (other.templateId != null)
                return false;
        } else if (!templateId.equals(other.templateId))
            return false;
        if (tfhResultId == null) {
            if (other.tfhResultId != null)
                return false;
        } else if (!tfhResultId.equals(other.tfhResultId))
            return false;
        if (timeOfFailure == null) {
            if (other.timeOfFailure != null)
                return false;
        } else if (!timeOfFailure.equals(other.timeOfFailure))
            return false;
        if (transactionId == null) {
            if (other.transactionId != null)
                return false;
        } else if (!transactionId.equals(other.transactionId))
            return false;
        return true;
    }
}
