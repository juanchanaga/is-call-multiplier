/*
 * Copyright (c) 2021. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.domain.dao;

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
