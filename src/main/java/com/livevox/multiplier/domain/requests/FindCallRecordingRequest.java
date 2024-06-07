/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.livevox.integration.commons.domain.stats.DateRange;
import com.livevox.multiplier.domain.stats.NumberRange;
import lombok.Data;


import java.io.Serializable;

@JsonInclude(Include.NON_NULL)
@Data
public class FindCallRecordingRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5031582497248519176L;
    private DateRange dateRange;
    private RestFilter service;
    private RestFilter type;
    private RestFilter callCenter;
    private RestFilter agent;
    private NumberRange durationRange;
    private String sortAsc;
    private String sortDsc;
    private RestFilter results;
    private RestFilter exclusions;
    private RestFilter campaign;
    private String originalAccountNumber;
}
