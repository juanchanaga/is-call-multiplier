package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TermCode implements Serializable {
    private Integer termCategoryId;
    private String termCategoryName;
    private String resultName;
    private Integer lvResultId;
    private Integer serviceId;
    private String name;
    private String action;
    private Integer reportOrder;
    private Boolean previewDialEnabled;
    private Integer clientId;
}
