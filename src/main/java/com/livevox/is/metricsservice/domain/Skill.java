package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Skill implements Serializable {
    private Integer callCenterId;
    private Integer skillId;
    private Integer serviceId;
    private String name;
    private List<TermCode> termCodes;
    private String direction;
    private Long id;
}
