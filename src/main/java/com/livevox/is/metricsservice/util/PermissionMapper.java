/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.util;

import com.livevox.is.metricsservice.domain.Permission;
import com.livevox.is.metricsservice.domain.Permission.PermissionType;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionMapper implements Serializable, RowMapper<Permission> {


    @Override
    public Permission mapRow(ResultSet rs, int arg1)
            throws SQLException {
        if(rs == null) {
            return null;
        }
        Permission per = new Permission();
        String tmp = rs.getString("permission_id");
        if(tmp != null) {
            per.setId( Long.valueOf(tmp) );
        }
        tmp = rs.getString("access_id");
        if(tmp != null) {
            per.setAccessId( Long.valueOf(tmp) );
        }
        tmp = rs.getString("permission_type");
        if(tmp != null) {
            per.setType( PermissionType.valueOf(tmp) );
        }
        per.setName( rs.getString("permission_name") );

        per.setCreatedDate( rs.getTimestamp("permission_created_date") );
        per.setModifiedDate(  rs.getTimestamp("permission_modified_date") );

        return per;
    }



}