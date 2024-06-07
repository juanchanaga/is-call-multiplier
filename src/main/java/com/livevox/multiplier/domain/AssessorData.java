package com.livevox.multiplier.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(Include.NON_NULL)
public class AssessorData implements Serializable {

    private static final long serialVersionUID = 87425260784754L;

    private Long id;

    private String token;

    private Long clientId;

    private String callId;

    private String service;

    private Long serviceId;

    private String name;

    private String account;

    private String phone;

    private String agent;

    private Long agentId;

    private Date recordingDate;

    private String originalAccountNumber;

    private String campaign;

    private Long campaignId;

    private String outcome;

    private String listenerId;

    private Date listenDate;

    private String callCenter;

    private Long callCenterId;

    private String client;

    private Boolean listenCompleted;





    public AssessorData() {
    }



    public AssessorData copy()  {
        try {
            AssessorData newAssDat = new AssessorData();
            BeanUtils.copyProperties(newAssDat, this);
            return newAssDat;
        } catch (Exception e) {
            throw new IllegalStateException("Clonging AssessorData failed.", e);
        }
    }


    public String getListener() {
        return listenerId;
    }


    public void setListener(String listenerId) {
        this.listenerId = listenerId;
    }


}