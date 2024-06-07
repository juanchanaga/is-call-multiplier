/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.dao.mapper;

import com.livevox.multiplier.domain.Access;
import com.livevox.multiplier.domain.LvSession;

import org.springframework.jdbc.core.RowMapper;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccessMapper  implements Serializable, RowMapper<Access> {

    private static final long serialVersionUID = -24989823242873L;


    @Override
    public Access mapRow(ResultSet rs, int idx)
            throws SQLException {
        if(rs == null) {
            return null;
        }
        Access access = new Access();
        String tmp = rs.getString("access_id");
        if(tmp != null) {
            access.setId( new Long(tmp) );
        }
        tmp = rs.getString("share_id");
        if(tmp != null) {
            access.setShareId( new Long(tmp) );
        }

        tmp = rs.getString("access_client_id");
        if(tmp != null) {
            access.setClientId( new Integer(tmp) );
        }
        tmp = rs.getString("access_shared_client_id");
        if(tmp != null) {
            access.setSharedClientId( new Integer(tmp) );
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

