package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.configurationclient.services.ClientService;
import com.livevox.is.domain.config.ClientSetup;
import com.livevox.is.metricsservice.configuration.AppProperties;
import com.livevox.is.metricsservice.service.ConfigurationService;
import com.livevox.is.signinclient.services.SignInClientService;
import com.livevox.is.util.annotation.Loggable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private SignInClientService signInClientService;
    private AppProperties appProperties;
    private ClientService clientService;
    private RetryTemplate retryTemplate;

    public ConfigurationServiceImpl(
            SignInClientService signInClientService,
            AppProperties appProperties,
            ClientService clientService,
            RetryTemplate retryTemplate) {

        this.signInClientService = signInClientService;
        this.appProperties = appProperties;
        this.clientService = clientService;
        this.retryTemplate = retryTemplate;
    }

    @Loggable
    public ClientSetup getClientSetup(Integer clientId) {

        return retryTemplate.execute(context ->
                clientService.getClientSetup(clientId,
                        appProperties.getApplicationName(),
                        appProperties.getApplicationToken()));
    }

    @Loggable
    public String getSessionId(Integer clientId) {
        String sessionId = retryTemplate.execute(context ->
                signInClientService.signIn(clientId, appProperties.getApplicationName(), appProperties.getApplicationToken()).getToken());

        return sessionId;
    }

}
