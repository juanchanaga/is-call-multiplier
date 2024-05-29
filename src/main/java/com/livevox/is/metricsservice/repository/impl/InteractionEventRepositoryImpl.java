package com.livevox.is.metricsservice.repository.impl;

import com.livevox.is.domain.api.realtime.enumeration.InteractionTypeEnum;
import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.domain.metrics.request.ActiveServicesRequest;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;
import com.livevox.is.metricsservice.repository.InteractionEventRepository;
import com.livevox.is.util.annotation.Loggable;
import com.livevox.is.util.sql.querybuilder.QueryBuilder;
import com.livevox.is.util.sql.querybuilder.QueryDirection;
import com.livevox.is.util.sql.querybuilder.QueryTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.livevox.is.util.sql.querybuilder.QueryCondition.between;
import static com.livevox.is.util.sql.querybuilder.QueryCondition.eq;

@Repository
@Slf4j
public class InteractionEventRepositoryImpl implements InteractionEventRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public InteractionEventRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<IdClass> getActiveServices(ActiveServicesRequest request) {

        log.info("Getting active services for callCenterId {}", request.getCallCenterId());

        QueryTemplate queryTemplate = QueryBuilder
                .select()
                .distinct("agent_skill_id AS id")
                .from("interaction_events")
                .where(between("created_time", request.getStart(), request.getEnd()))
                .and(eq("call_center_id", request.getCallCenterId()))
                .finishWhere()
                .limit(request.getCount())
                .offset(request.getOffset())
                .build();

        return jdbcTemplate.getJdbcTemplate().query(
                queryTemplate.getQuery(),
                new BeanPropertyRowMapper<>(IdClass.class),
                queryTemplate.getParameters().toArray());
    }

    @Override
    @Loggable
    public List<InteractionEventDAO> getActiveServiceInteractionDetails(ActiveServiceMetricsRequest request) {

        QueryTemplate queryTemplate = QueryBuilder
                .select()
                .from("interaction_events")
                .where(between("created_time", request.getStart(), request.getEnd()))
                .and(eq("interaction_type", InteractionTypeEnum.VOICE.getName()))
                .and(eq("service_id", request.getServiceId()))
                .and(eq("call_center_id", request.getCallCenterId()))
                .and(eq("call_direction", request.getDirection()))
                .finishWhere()
                .orderBy("start", QueryDirection.ASC)
                .limit(request.getCount())
                .offset(request.getOffset())
                .build();

        return jdbcTemplate.getJdbcTemplate().query(
                queryTemplate.getQuery(),
                new BeanPropertyRowMapper<>(InteractionEventDAO.class),
                queryTemplate.getParameters().toArray());
    }

}
