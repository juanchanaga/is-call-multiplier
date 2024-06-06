package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.lvapi.ListRequest;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ToString( callSuper = true)
public class AgentRequest  extends ListRequest implements Serializable, Cloneable {
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
