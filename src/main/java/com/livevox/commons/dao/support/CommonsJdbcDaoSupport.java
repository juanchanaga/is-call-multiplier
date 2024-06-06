/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.commons.dao.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

public class CommonsJdbcDaoSupport extends NamedParameterJdbcDaoSupport {

    @Autowired
    protected DataSource dataSource;

    @PostConstruct
    protected void initialize() {
        setDataSource(dataSource);
    }

}
