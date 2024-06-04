package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request implements Serializable {
    private String token;
    private String apiSessionId;
    private Integer clientId;
    private String apiVersion;

}
