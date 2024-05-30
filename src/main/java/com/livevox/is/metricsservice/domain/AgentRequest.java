package com.livevox.is.metricsservice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentRequest {
    private Integer clientId;
    private Integer offset;
    private String token;
}
