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
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

@JsonInclude(Include.NON_NULL)
@Data
public class Permission extends DefaultDao implements Serializable {

    private static final long serialVersionUID = 5224491787119094056L;

    public enum PermissionType {
        ADD,
        READ,
        UPDATE,
        DELETE,
        HIDE
    }


    private String name;

    private PermissionType type;

    private Long accessId;


    public Permission() {}


    public Permission(PermissionType type, String name) {
        this.type = type;
        this.name = name;
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
