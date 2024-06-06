/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString( callSuper = true)
public class ListRequest extends Request implements Serializable {

    private Integer count;
    private Integer offset;

    public ListRequest(Integer clientId) {
        super(clientId);
    }

    public ListRequest(Integer clientId, String apiSessionId) {
        super(clientId, apiSessionId);
    }
}
