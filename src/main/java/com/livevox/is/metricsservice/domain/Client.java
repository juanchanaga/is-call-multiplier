package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client implements Serializable {

    private Integer id;

    private String clientName;

    private String clientCode;

    private List<CallCenter> callCenters;

    private List<Skill> services;

    private List<TermCode> termCodes;
//
//    private List<CallDetail> calls;
//
//    private List<CallRecording> recordings;

}
