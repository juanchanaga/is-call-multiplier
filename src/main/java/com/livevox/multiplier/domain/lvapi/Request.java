/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request  implements Serializable {

    @Deprecated
    private String token;

    private String apiSessionId;
    private Integer clientId;
    private String apiVersion;


    public Request(Integer clientId) {
        setClientId(clientId);
        if(clientId != null) {
            setApiSessionId( String.valueOf(clientId) );
        }
    }

    public Request(Integer clientId, String apiSessionId) {
        this(clientId);

        if(apiSessionId == null && clientId != null) {
            setApiSessionId( String.valueOf(clientId) );
        } else {
            setApiSessionId(apiSessionId);
        }
    }

    public String getApiSessionId() {
        if(apiSessionId == null) {
            return token;
        }

        return apiSessionId;
    }
}
