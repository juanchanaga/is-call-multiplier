package com.livevox.is.metricsservice.service;

import com.livevox.is.domain.config.ClientSetup;

public interface ConfigurationService {

    ClientSetup getClientSetup(Integer clientId);

    String getSessionId(Integer clientId);
}
