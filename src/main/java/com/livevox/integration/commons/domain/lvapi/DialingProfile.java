/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DialingProfile extends ListRequest implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Boolean failed;
    private List<DialingPolicy> policy;

    public DialingProfile() { }

    public DialingProfile(String name, String description) {

        this.name = name;
        this.description = description;
    }

    public boolean validCreate() {
        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(description) ) {
            return false;
        }
        if( policy != null && policy.size() < 1) {
            for(DialingPolicy newPlc : policy) {
                if( !newPlc.validCreate() ) {
                    return false;
                }
            }
        }
        return true;
    }
    public void addPolicy(DialingPolicy newPolicy) {
        if(policy == null) {
            policy = new ArrayList<>();
        }
        policy.add(newPolicy);
    }

    public boolean validUpdate() {
        return ( validCreate() && id != null);
    }
}
