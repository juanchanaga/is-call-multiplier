/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * INTEGRATION-CALL-MULTIPLIER-SERVICE
 */

package com.livevox.is.metricsservice.configuration;

import com.livevox.is.metricsservice.domain.dao.SharedDataDao;
import com.livevox.is.metricsservice.service.config.AtgConfigRestService;
import com.livevox.is.metricsservice.service.config.AtgConfigRestServiceImpl;
import com.livevox.is.metricsservice.service.config.AtgConfigServiceImpl;
import com.livevox.is.metricsservice.session.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * The type App config.
 *
 * @autor Luis Felipe Sosa Alvarez lsosa@livevox.com
 */
public class AppConfig {


    private AtgConfigServiceImpl configService;

    @Value(value = "${atg.config.host}")
    private String configHost;

    @Value(value = "${atg.config.key}")
    private String configKey;

    private DataSource dataSource;

    public AppConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * Config service atg config service.
     *
     * @return the atg config service
     */
    @Bean
    public AtgConfigServiceImpl configService() {
        if( configService != null ) {
            return configService;
        }
        configService = new AtgConfigServiceImpl();
        configService.setAtgConfigService(configServiceClient() );
        configService.initialize();

        return configService;
    }


    /**
     * Config service client atg config rest service.
     *
     * @return the atg config rest service
     */
    public AtgConfigRestService configServiceClient() {
        AtgConfigRestServiceImpl configSrvcClnt =  new AtgConfigRestServiceImpl();
        configSrvcClnt.setKey(configKey);
        configSrvcClnt.setHostName(configHost);

        return configSrvcClnt;
    }

    /**
     * Sign in service sign in service.
     *
     * @param configService the config service
     * @return the sign in service
     */
    @Bean
    public SignInService signInService(AtgConfigServiceImpl configService ) {
        return new SignInService(  configService );
    }

    @Bean
    public SharedDataDao sharedDataDao() {
        SharedDataDao sharedDataDao = new SharedDataDao();
        sharedDataDao.setDataSource(dataSource);
        return sharedDataDao;
    }
}
