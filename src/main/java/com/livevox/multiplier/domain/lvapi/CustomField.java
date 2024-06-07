/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.multiplier.domain.enums.Field;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CustomField implements Serializable {

    private String value;
    private Field field;

    /**
     * Platform API uses "value" for creation and uses "label" for retrieval.
     */
    private String label;
}