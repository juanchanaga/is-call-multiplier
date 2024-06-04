/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.domain.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.is.metricsservice.domain.ClientInfo;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ClientsAndPropertiesResponse {

    private List<ClientInfo> clients;
    private List<ConfigProperty> properties;

    @JsonIgnore
    public ConfigProperty getProperty(String key) {
        if( CollectionUtils.isEmpty(getProperties())  || StringUtils.isEmpty(key) ) {
            return null;
        }
        for(ConfigProperty prop :  getProperties()) {
            if(  StringUtils.isNotEmpty(prop.getKey())  && key.equals(prop.getKey()) ) {
                return prop;
            }
        }
        return null;
    }
}
