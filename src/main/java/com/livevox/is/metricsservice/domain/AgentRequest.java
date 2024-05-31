package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString( callSuper = true)
public class AgentRequest implements Serializable {
    private String application;
    private Date startDate;
    private Date endDate;
    private String account;
    private String originalAccountNumber;
    private RestFilter filter;
    private String loginId;
    private String lastName;
    private String phoneNumber;
}
