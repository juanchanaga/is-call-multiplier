/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.shareddata.multiplier.config;

import com.livevox.commons.dao.SharedDataDao;
import com.livevox.commons.services.config.AtgConfigRestService;
import com.livevox.commons.services.config.AtgConfigRestServiceImpl;
import com.livevox.commons.services.config.AtgConfigServiceImpl;
import com.livevox.commons.services.signin.SignInService;
import com.livevox.is.util.grpc.exception.GlobalGrpcExceptionHandler;
import com.livevox.is.util.rest.exception.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * The type App config.
 *
 * @autor Luis Felipe Sosa Alvarez lsosa@livevox.com
 */
@Import({
        GlobalExceptionHandler.class,
        GlobalGrpcExceptionHandler.class,})
public class AppConfig {


    private AtgConfigServiceImpl configService;

    @Value(value = "${atg.config.host}")
    private String configHost;

    @Value(value = "${atg.config.key}")
    private String configKey;

    @Autowired
    private DataSource dataSource;


    /**
     * Config service atg config service.
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
