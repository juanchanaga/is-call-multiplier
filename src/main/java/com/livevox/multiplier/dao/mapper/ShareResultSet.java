/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.dao.mapper;

import com.livevox.multiplier.domain.Access;
import com.livevox.multiplier.domain.Permission;
import com.livevox.multiplier.domain.Share;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShareResultSet implements Serializable, ResultSetExtractor<List<Share>> {

    private static final long serialVersionUID = -328924970139242873L;


    @Override
    public List<Share> extractData(ResultSet rs) throws SQLException {
        if(rs == null) {
            return null;
        }
        List<Share> shares = new ArrayList<>();

        while(rs.next()) {
            String tmp = rs.getString("share_id");
            Share share = getPresentShare(shares, new Long(tmp), rs);
            if(share != null) {
                tmp = rs.getString("access_id");
                if(tmp != null) {
                    addAccess(share, new Long(tmp), rs );
                }
            }
        }
        return shares;
    }



    private Share getPresentShare(final List<Share> shares, final Long id,
                                  final ResultSet rs) throws SQLException {
        if(shares == null || id == null || rs == null) {
            return null;
        }
        for(Share existingShare : shares) {
            if(existingShare.getId() != null && existingShare.getId().equals(id) ) {
                return existingShare;
            }
        }
        Share newShare = new Share();
        newShare.setId( id );
        String tmp = rs.getString("share_client_id");
        if(tmp != null) {
            newShare.setClientId( new Integer(tmp) );
        }
        newShare.setCreatedDate( rs.getTimestamp("share_created_date") );
        newShare.setModifiedDate( rs.getTimestamp("share_modified_date")  );
        shares.add(newShare);

        return newShare;
    }


    private void addAccess( Share share, final Long id, final ResultSet rs) throws SQLException {
        if(share == null || id == null || rs == null) {
            return;
        }
        Access acc = null;
        if( share.getAccess() != null ) {
            for(Access existingCriteria : share.getAccess() ) {
                if(existingCriteria.getId() != null && existingCriteria.getId().equals(id) ) {
                    acc = existingCriteria;
                    break;
                }
            }
        }
        String tmp = null;
        if(acc == null) {
            acc = new AccessMapper().mapRow(rs, 0);
            share.addAccess( acc );
        }
        tmp = rs.getString("permission_id");
        if(tmp != null) {
            addPermission(acc, new Long(tmp), rs);
        }
        tmp = rs.getString("access_application");
        if(tmp != null) {
            addApplication(acc, tmp);
        }
    }



    private void addPermission(Access access , final Long id, final ResultSet rs) throws SQLException {
        if(access == null ||  id == null || rs == null) {
            return;
        }
        if( access.getPermissions() != null ) {
            for(Permission existingPer : access.getPermissions() ) {
                if(existingPer.getId() != null && existingPer.getId().equals(id) ) {
                    return;
                }
            }
        }
        access.addPermission( new PermissionMapper().mapRow(rs, 0) );
    }


    private void addApplication(Access access , final String name) throws SQLException {
        if(access == null ||  StringUtils.isBlank(name)  ) {
            return;
        }
        if( access.getApplications() != null ) {
            for(String existingApp : access.getApplications() ) {
                if(existingApp != null && existingApp.equals(name) ) {
                    return;
                }
            }
        }
        access.addApplication(name);
    }





}
