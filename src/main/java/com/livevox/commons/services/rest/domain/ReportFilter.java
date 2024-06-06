/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.rest.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.stats.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportFilter {
    private static final long serialVersionUID = 7409910233901640121L;
    private List<IdType> callCenter;
    private List<IdType> service;
    private List<IdType> type;
    private List<IdType> result;
    private Integer agent;
    private String account;
    private String phoneDialed;
    private String originalAccountNumber;
    private GenericRange transferDuration;
    private IdType campaign;

    public void addCallCenter(Long id) {
        if (id != null) {
            if (callCenter == null) {
                callCenter = new ArrayList<>();
            }
            callCenter.add(new IdType(id.longValue()));
        }
    }

    public void addService(Long id) {
        if (id != null) {
            if (service == null) {
                service = new ArrayList<>();
            }
            service.add(new IdType(id.longValue()));
        }
    }

    public void addType(Long id) {
        if (id != null) {
            if (type == null) {
                type = new ArrayList<>();
            }
            type.add(new IdType(id.longValue()));
        }
    }

    public void addResultCode(Long id) {
        if (id != null) {
            if (result == null) {
                result = new ArrayList<>();
            }
            result.add(new IdType(id));
        }
    }

}
