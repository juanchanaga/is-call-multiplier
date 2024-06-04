package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.stats.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ReportFilter {
    private static final long serialVersionUID = 7409910233901640121L;
    private List<com.livevox.integration.commons.domain.stats.IdType> callCenter;
    private List<com.livevox.integration.commons.domain.stats.IdType> service;
    private List<com.livevox.integration.commons.domain.stats.IdType> type;
    private List<com.livevox.integration.commons.domain.stats.IdType> result;
    private Integer agent;
    private String account;
    private String phoneDialed;
    private String originalAccountNumber;
    private GenericRange transferDuration;
    private com.livevox.integration.commons.domain.stats.IdType campaign;

    public void addCallCenter(Long id) {
        if (id != null) {
            if (callCenter == null) {
                callCenter = new ArrayList<>();
            }
            callCenter.add(new com.livevox.integration.commons.domain.stats.IdType(id.longValue()));
        }
    }

    public void addService(Long id) {
        if (id != null) {
            if (service == null) {
                service = new ArrayList<>();
            }
            service.add(new com.livevox.integration.commons.domain.stats.IdType(id.longValue()));
        }
    }

    public void addType(Long id) {
        if (id != null) {
            if (type == null) {
                type = new ArrayList<>();
            }
            type.add(new com.livevox.integration.commons.domain.stats.IdType(id.longValue()));
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
