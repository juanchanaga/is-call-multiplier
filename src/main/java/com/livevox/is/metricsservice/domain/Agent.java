package com.livevox.is.metricsservice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Agent {
    private Boolean active;
    private Integer clientId;
    private String firstName;
    private String lastName;
    private Integer id;
    private String loginId;
    private String phone;
}
