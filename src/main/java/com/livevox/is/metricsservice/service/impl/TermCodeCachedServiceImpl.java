package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.api.configuration.service.TermCodeService;
import com.livevox.is.domain.api.configuration.response.LiveVoxResult;
import com.livevox.is.domain.config.ClientSetup;
import com.livevox.is.metricsservice.service.TermCodeCachedService;
import com.livevox.is.util.annotation.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class TermCodeCachedServiceImpl implements TermCodeCachedService {

    private TermCodeService termCodeService;

    private RetryTemplate retryTemplate;

    public TermCodeCachedServiceImpl(
            TermCodeService termCodeService,
            RetryTemplate retryTemplate) {

        this.termCodeService = termCodeService;
        this.retryTemplate = retryTemplate;
    }

    @Override
    @Loggable
    @Cacheable(value = LV_RESULT_CACHE, key = "{#liveVoxResultId, #clientSetup.apiHost}")
    public Optional<LiveVoxResult> readLiveVoxResult(
            ClientSetup clientSetup,
            Integer liveVoxResultId,
            String sessionId) {

        log.debug("Calling readLiveVoxResult for lvResult: {}", liveVoxResultId);

        ResponseEntity<LiveVoxResult> liveVoxResultResponseEntity = retryTemplate.execute(context ->
                termCodeService.readLiveVoxResult(clientSetup, liveVoxResultId, sessionId));

        return Optional.ofNullable(liveVoxResultResponseEntity.getBody());
    }

    @Scheduled(cron="${app.metrics-service.lv-result-cache.cron-expression}")
    @CacheEvict(value = LV_RESULT_CACHE, allEntries = true)
    public void clearCache() {
        log.debug("Cache '{}' cleared.", LV_RESULT_CACHE);
    }

}
