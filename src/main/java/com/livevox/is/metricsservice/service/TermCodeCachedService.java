package com.livevox.is.metricsservice.service;

import com.livevox.is.domain.api.configuration.response.LiveVoxResult;
import com.livevox.is.domain.config.ClientSetup;

import java.util.Optional;

public interface TermCodeCachedService {

    String LV_RESULT_CACHE = "lvResultCache";

    Optional<LiveVoxResult> readLiveVoxResult(
            ClientSetup clientSetup,
            Integer liveVoxResultId,
            String sessionId);
}
