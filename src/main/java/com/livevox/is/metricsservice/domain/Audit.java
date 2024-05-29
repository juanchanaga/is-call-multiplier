package com.livevox.is.metricsservice.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
public class Audit {

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant createdTime;

    @LastModifiedDate
    private Instant modifiedTime;

}
