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

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallRecording implements Serializable {

    private String id;
    private Integer clientId;
    private Long callCenterId;
    private Long serviceId;
    private Long agentId;
    private String agent;
    private Long campaignId;
    private String account;
    private String phone;
    private Long termCodeId;
    private Integer duration;
    private String originalAccountNumber;
    private String service;
    private String callCenter;
    private String termCode;
    private String soundFile;
    private String campaign;
    private String name;
    private Date start;
    private Date end;
    private String accountTransactionId;
    private Integer size;
    private Boolean deleted;
    private String objectId;

    /**
     *   This is the field name the call recording report API uses for call duration so
     *   it has to be supported
     *
     * @return Integer
     */
    public Integer getTransferDuration() {
        return duration;
    }

    /**
     *   This is the field name the call recording report API uses for call duration so
     *   it has to be supported
     *
     */
    public void setTransferDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     *   This is the field name the call recording report API uses for start date so
     *   it has to be supported
     *
     * @return Date
     */
    public Date getTransferConnect() {
        return start;
    }

    /**
     *   This is the field name the call recording report API uses for start date so
     *   it has to be supported
     *
     */
    public void setTransferConnect(Date start) {
        this.start = start;
    }

    /**
     *   This is the field name the call recording report API uses for end date so
     *   it has to be supported
     *
     */
    public Date getTransferEnd() {
        return end;
    }

    /**
     *   This is the field name the call recording report API uses for end date so
     *   it has to be supported
     *
     */
    public void setTransferEnd(Date end) {
        this.end = end;
    }

    /**
     *   This is the field name the call recording report API uses for call duration so
     *   it has to be supported
     *
     */
    public String getSession() {
        return id;
    }

    /**
     *   This is the field name the call recording report API uses for call duration so
     *   it has to be supported
     *
     */
    public void setSession(String id) {
        this.id = id;
    }

    public String getCampaignName() {
        return this.campaign;
    }

    public void setCampaignName(String campaign) {
        this.campaign = campaign;
    }

    public String getRecordingId() {
        return this.getObjectId();
    }

    public void setRecordingId(String recordingId) {
        this.setObjectId(recordingId);
    }

    public Date getToDate() {
        return this.end;
    }

    public void setToDate(Date toDate) {
        this.end = toDate;
    }

    public Date getFromDate() {
        return this.start;
    }

    public void setFromDate(Date fromDate) {
        this.start = fromDate;
    }

}
