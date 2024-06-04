/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.util;

import com.livevox.is.metricsservice.domain.Access;
import com.livevox.is.metricsservice.domain.LvSession;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccessMapper implements Serializable, RowMapper<Access> {


    @Override
    public Access mapRow(ResultSet rs, int idx)
            throws SQLException {
        if(rs == null) {
            return null;
        }
        Access access = new Access();
        String tmp = rs.getString("access_id");
        if(tmp != null) {
            access.setId( Long.parseLong(tmp) );
        }
        tmp = rs.getString("share_id");
        if(tmp != null) {
            access.setShareId( Long.parseLong(tmp) );
        }

        tmp = rs.getString("access_client_id");
        if(tmp != null) {
            access.setClientId( Integer.parseInt(tmp) );
        }
        tmp = rs.getString("access_shared_client_id");
        if(tmp != null) {
            access.setSharedClientId( Integer.parseInt(tmp) );
        }
        access.setUserAcct( new LvSession() );
        access.getUserAcct().setClientId( access.getClientId() );
        access.getUserAcct().setClientName( rs.getString("access_lvp_client_name") );
        access.getUserAcct().setUserName( rs.getString("access_lvp_user_name") );
        access.getUserAcct().setPassword( rs.getString("access_lvp_password") );
        access.setCreatedDate( rs.getTimestamp("access_created_date") );
        access.setModifiedDate(  rs.getTimestamp("access_modified_date") );

        return access;
    }



}

