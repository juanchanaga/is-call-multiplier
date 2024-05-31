package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestFilter implements Serializable {
    private List<IdType> callCenter;
    private List<IdType> service;
    private List<IdType> type;
    private List<IdType> agent;
    private List<IdType> campaign;
    private String pattern;
    private List<IdType> campaignType;
    private List<RecordingType> exclusions;
    private List<ResultType> result;
}
