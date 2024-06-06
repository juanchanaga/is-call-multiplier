/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.rest.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.lvapi.ListRequest;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ToString( callSuper = true)
public class AgentRequest  extends ListRequest implements Serializable, Cloneable {

    private static final long serialVersionUID = 8536255260784754L;
    private String application;
    private Date startDate;
    private Date endDate;
    private String account;
    private String originalAccountNumber;
    private RestFilter filter;
    private String loginId;
    private String lastName;
    private String phoneNumber;

    public boolean valid() {
        int numberOfCriteria = 0;
        if (!StringUtils.isBlank(this.loginId)) {
            ++numberOfCriteria;
        }

        if (!StringUtils.isBlank(this.lastName)) {
            ++numberOfCriteria;
        }

        if (!StringUtils.isBlank(this.phoneNumber)) {
            ++numberOfCriteria;
        }

        return numberOfCriteria == 1;
    }

    public AgentRequest clone() {
        AgentRequest req = new AgentRequest();

        try {
            BeanUtils.copyProperties(req, this);
            if (this.getFilter() != null) {
                req.setFilter(req.getFilter().clone());
            }

            return req;
        } catch (Exception var3) {
            throw new IllegalStateException("Could not clone AgentRequest.  " + var3);
        }
    }

    public void addServiceIds(List<Long> serviceIds) {
        if (this.filter == null) {
            this.filter = new RestFilter();
        }

        if (serviceIds == null) {
            this.filter.setService((List)null);
        } else {
            Iterator var2 = serviceIds.iterator();

            while(var2.hasNext()) {
                Long srvcId = (Long)var2.next();
                this.filter.addService(srvcId);
            }
        }
    }


}
