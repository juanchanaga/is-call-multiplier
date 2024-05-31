package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Agent {
    private Long id;
    private String loginId;
    private String firstName;
    private String lastName;
    private String phone;
    private Long wrapUpTime;
    private Boolean homeAgent;
    private Boolean active;
    private Boolean locked;
    private Integer clientId;
    private Date passwordLastModified;
    private List<IdType> assignedService;
}
