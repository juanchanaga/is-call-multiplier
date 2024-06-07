/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(Include.NON_NULL)
@Data

public class ScreenPopDetail implements Serializable{

    private static final long serialVersionUID = 1364022904504200899L;
    private String key;
    private String command;
    private String data;
    private Integer position;
    private String delimiter;
    private String displayName;

}
