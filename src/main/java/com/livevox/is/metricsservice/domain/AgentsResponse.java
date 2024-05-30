package com.livevox.is.metricsservice.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AgentsResponse {
    private List<Agent> data;
    private Integer status;
    private Integer totalCount;
}
