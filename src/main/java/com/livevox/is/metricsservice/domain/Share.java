/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.livevox.integration.commons.domain.stats.DefaultDao;
import com.livevox.is.metricsservice.domain.Permission.PermissionType;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(Include.NON_NULL)
@Data
public class Share extends DefaultDao implements Serializable {

    private Integer clientId;

    private List<Access> access;




    public void addAccess(Access newAccess) {
        if(access == null) {
            access = new ArrayList<>();
        }
        if(newAccess != null ) {
            access.add(newAccess);
        }
    }


    public Access getAccessByid(Long accessId) {
        if(access == null) {
            return null;
        }
        for(Access acc : access) {
            if(acc.getId().equals(accessId) ) {
                return acc;
            }
        }
        return null;
    }


    public List<Integer> getClientIdsShared() {
        List<Integer> clientIds = new ArrayList<>();
        if(access != null) {
            for(Access a : access) {
                clientIds.add(a.getSharedClientId());
            }
        }
        return clientIds;
    }


    public List<Integer> getClientIdsForPermissionType(PermissionType pType) {
        List<Integer> clientIds = new ArrayList<>();
        if(pType == null) {
            return clientIds;
        }
        if(access != null) {
            for(Access a : access) {
                if( a.hasPermissionType(pType) ) {
                    clientIds.add(a.getSharedClientId());
                }
            }
        }
        return clientIds;
    }

    public List<Integer> getClientIdsForPermissionTypeAndApplication(PermissionType pType, String app) {
        List<Integer> clientIds = new ArrayList<>();
        if (pType == null || access == null) {
            return clientIds;
        }
        for (Access a : access) {
            if (a.hasPermissionType(pType) && a.hasApplication(app)) {
                clientIds.add(a.getSharedClientId());
            }
        }
        return clientIds;
    }



    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, true);
    }


    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, true);
    }


}
