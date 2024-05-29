package com.livevox.is.metricsservice.repository;

import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.domain.metrics.request.ActiveServicesRequest;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;

import java.util.List;

public interface InteractionEventRepository {

    List<IdClass> getActiveServices(ActiveServicesRequest request);

    List<InteractionEventDAO> getActiveServiceInteractionDetails(ActiveServiceMetricsRequest request);
}
