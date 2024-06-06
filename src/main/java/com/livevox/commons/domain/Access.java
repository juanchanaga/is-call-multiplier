/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.livevox.commons.domain.Permission.PermissionType;
import com.livevox.integration.commons.domain.stats.DefaultDao;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(Include.NON_NULL)
@Data
public class Access extends DefaultDao implements Serializable {

    private static final long serialVersionUID = 2869206479761421295L;

    private Long shareId;

    private List<String> applications;

    private Integer clientId;

    private Integer sharedClientId;

    private LvSession userAcct;

    private List<Permission> permissions;

    public void addApplication(String application) {
        if(applications == null) {
            applications = new ArrayList<>();
        }
        applications.add(application);
    }

    public boolean hasApplication(String app) {
        if(applications == null || app == null) {
            return false;
        }
        for(String existingApp : applications ) {
            if(existingApp != null && existingApp.equals(app)) {
                return true;
            }
        }
        return false;
    }

    public void addPermission(Permission newPermission) {
        if(permissions == null) {
            permissions = new ArrayList<>();
        }
        if(newPermission != null ) {
            permissions.add(newPermission);
        }
    }


    public boolean hasPermissionName(String permissionName) {
        if(permissions == null || permissionName == null) {
            return false;
        }
        for(Permission perm : permissions ) {
            if(perm.getName() != null && perm.getName().equals(permissionName)) {
                return true;
            }
        }
        return false;
    }


    public boolean hasPermissionType(PermissionType type) {
        if(permissions == null || type == null) {
            return false;
        }
        for(Permission perm : permissions ) {
            if(perm.getType() != null && perm.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasReadPermission() {
        return hasPermissionType(PermissionType.READ);
    }


    public boolean hasAddPermission() {
        return hasPermissionType(PermissionType.ADD);
    }


    public boolean hasDeletePermission() {
        return hasPermissionType(PermissionType.DELETE);
    }


    public boolean hasUpdatePermission() {
        return hasPermissionType(PermissionType.UPDATE);
    }


    public boolean hasHidePermission() {
        return hasPermissionType(PermissionType.HIDE);
    }

    public void setSharedClientId(Integer sharedClientId) {
        this.sharedClientId = sharedClientId;
        if(userAcct != null) {
            userAcct.setClientId(sharedClientId);
        }
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
