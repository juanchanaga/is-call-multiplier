package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.lvapi.ListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper=false)
public class AgentRequest extends ListRequest implements Serializable {

    private RestFilter filter;
    private String application;
    private Date startDate;
    private Date endDate;
    private String account;
    private String originalAccountNumber;
    private String loginId;
    private String lastName;
    private String phoneNumber;

}
