package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallCenter implements Serializable {
    private Long callCenterId;
    private String name;
    private int clientId;
    private int id;
    private List<Skill> services;
}
