/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.dao.mapper;

import com.livevox.multiplier.domain.Permission;
import com.livevox.multiplier.domain.Permission.PermissionType;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionMapper implements Serializable, RowMapper<Permission> {

    private static final long serialVersionUID = -89823242873L;


    @Override
    public Permission mapRow(ResultSet rs, int arg1)
            throws SQLException {
        if(rs == null) {
            return null;
        }
        Permission per = new Permission();
        String tmp = rs.getString("permission_id");
        if(tmp != null) {
            per.setId( new Long(tmp) );
        }
        tmp = rs.getString("access_id");
        if(tmp != null) {
            per.setAccessId( new Long(tmp) );
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