package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListRequest extends Request implements Serializable {
    private Integer count;
    private Integer offset;
}
