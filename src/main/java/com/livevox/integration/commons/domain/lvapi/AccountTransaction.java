/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AccountTransaction {
    private String account;
    private Integer lvResultId;
    private Person consumer;
    private Integer serviceId;
    private List<Phone> phones;
    private List<KeyValue> extra;
    private List<KeyValue> daysDue;
}
