package com.livevox.is.metricsservice.service.impl;

import com.livevox.is.domain.metrics.request.MetricsRequest;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.repository.impl.GenericRepositoryImpl;
import com.livevox.is.metricsservice.service.GenericService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenericServiceImpl implements GenericService {

    public GenericServiceImpl() {
    }

    @Override
    public String getResponse() {
        return "Prueba";
    }

}
