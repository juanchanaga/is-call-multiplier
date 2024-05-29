package com.livevox.is.metricsservice.repository.impl;

import com.livevox.is.metricsservice.repository.GenericRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
public class GenericRepositoryImpl implements GenericRepository {
    @Override
    public String getGenericResponse() {
        return "Prueba";
    }
}
