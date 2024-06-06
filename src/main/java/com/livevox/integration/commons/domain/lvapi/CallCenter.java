/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.commons.domain.lvapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.livevox.integration.commons.domain.stats.Skill;
import com.livevox.integration.commons.domain.stats.TermCode;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallCenter implements Serializable, Comparable<CallCenter> {
    private Long  callCenterId;
    private String name;
    private int clientId;
    private List<Skill> services;

    public void sortServices() {
        if (services != null) {
            Collections.sort(services);
        }
    }

    @Override
    public int compareTo(CallCenter otherCallCenter) {
        if (otherCallCenter == null || (otherCallCenter.getName() == null && getName() == null) ||
                (getName().equals(otherCallCenter.getName()))) {
            return 0;
        }

        if (getName() != null && otherCallCenter.getName() == null) {
            return 1;
        }

        if (getName() == null && otherCallCenter.getName() != null) {
            return -1;
        }

        return getName().compareTo(otherCallCenter.getName());

    }
    public void addService(Skill service) {
        if(this.services == null) {
            this.services = new ArrayList<>();
        }
        this.services.add(service);
    }


    public boolean hasService(Integer id) {
        if(services == null || id == null) {
            return false;
        }
        return getService(id) != null;
    }


    public Skill getService(Integer id) {
        if(services == null || id == null) {
            return null;
        }
        for(Skill service : services) {
            if(service.getSkillId().equals(id)) {
                return service;
            }
        }
        return null;
    }


    public boolean hasTermCode(String tcCode) {
        if(services == null || tcCode == null) {
            return false;
        }
        for(Skill service : services) {
            if(service.hasTermCode(tcCode)) {
                return true;
            }
        }
        return false;
    }


    public TermCode getTermCode(String tcCode) {
        if(services == null || tcCode == null) {
            return null;
        }
        for(Skill service : services) {
            TermCode tc = service.getTermCode(tcCode);
            if(tc != null) {
                return tc;
            }
        }
        return null;
    }


    public boolean hasTermCodeId(Long id) {
        if(services == null || id == null) {
            return false;
        }
        for(Skill service : services) {
            if(service.hasTermCodeId(id)) {
                return true;
            }
        }
        return false;
    }


    public boolean hasResultCodeId(Integer resultCodeId) {
        if(services == null || resultCodeId == null) {
            return false;
        }
        for(Skill service : services) {
            if(service.hasResultCodeId(resultCodeId)) {
                return true;
            }
        }
        return false;
    }

    public Long getId(){
        return callCenterId;
    }
}
