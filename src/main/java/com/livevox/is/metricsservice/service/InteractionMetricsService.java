package com.livevox.is.metricsservice.service;

import com.livevox.is.domain.metrics.ServiceMetrics;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;

import java.util.List;

public interface InteractionMetricsService {
    ServiceMetrics calculateMetrics(
            List<InteractionEventDAO> events,
            ActiveServiceMetricsRequest request);

    ServiceMetrics calculateCallMetrics(InteractionEventDAO event);
}
