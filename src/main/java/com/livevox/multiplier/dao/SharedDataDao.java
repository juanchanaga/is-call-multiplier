/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.multiplier.dao;

import com.livevox.multiplier.dao.mapper.ShareResultSet;
import com.livevox.multiplier.dao.support.CommonsJdbcDaoSupport;
import com.livevox.multiplier.domain.Share;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
@Slf4j
public class SharedDataDao extends CommonsJdbcDaoSupport implements Serializable {

    @Transactional(readOnly=true)
    public List<Share> getSharesForClient(Integer clientId) throws SQLException {
        log.debug("Start getSharesForClient() ");
        if(clientId == null) {
            return null;
        }
        String sql = " SELECT * FROM client_data_shares WHERE share_client_id = ? ORDER BY access_lvp_client_name";
        log.debug("Query: "+sql);
        List<Share> shares = getJdbcTemplate().query(sql,
                new Object[] {clientId}, new ShareResultSet() );
        log.debug("End getSharesForClient() ");
        return shares;
    }
}
