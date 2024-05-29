package com.livevox.is.metricsservice.service;

import com.livevox.is.domain.metrics.ServiceMetrics;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.domain.metrics.request.ActiveServicesRequest;
import com.livevox.is.domain.metrics.request.CallMetricsRequest;
import com.livevox.is.domain.util.IdClass;

import java.util.List;
import java.util.Optional;

public interface InteractionEventService {

    List<IdClass> getActiveServices(ActiveServicesRequest request);

    ServiceMetrics getActiveServiceMetrics(ActiveServiceMetricsRequest request);

    Optional<ServiceMetrics> getCallMetrics(CallMetricsRequest request);
}
