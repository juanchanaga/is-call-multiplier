/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.services.config;

import lombok.Data;

import java.io.Serializable;
@Data
public class KeyValidRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -126544526167703674L;
    private String key;
}
