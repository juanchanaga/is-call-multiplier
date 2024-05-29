package com.livevox.is.metricsservice.repository.impl;

import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.metrics.request.MetricsRequest;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.domain.AgentEventDAO;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;
import com.livevox.is.metricsservice.repository.AgentEventRepository;
import com.livevox.is.util.annotation.Loggable;
import com.livevox.is.util.sql.querybuilder.QueryBuilder;
import com.livevox.is.util.sql.querybuilder.QueryCondition;
import com.livevox.is.util.sql.querybuilder.QueryDirection;
import com.livevox.is.util.sql.querybuilder.QueryTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class AgentEventRepositoryImpl implements AgentEventRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public AgentEventRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<IdClass> getActiveAgents(MetricsRequest request) {

        log.info("Getting active agents for client id {}", request.getClientId());

        QueryTemplate queryTemplate = QueryBuilder
                .select()
                .distinct("agent_id as id")
                .from("agent_events")
                .where(QueryCondition.eq("client_id", request.getClientId()))
                .and(QueryCondition.between("timestamp", request.getStart(), request.getEnd()))
                .finishWhere()
                .orderBy("agent_id", QueryDirection.ASC)
                .limit(request.getCount())
                .offset(request.getOffset())
                .build();

        return jdbcTemplate.getJdbcTemplate()
                .query(queryTemplate.getQuery(), new BeanPropertyRowMapper<>(IdClass.class), queryTemplate.getParameters().toArray());

    }

    @Override
    public List<AgentEventDAO> getAgentEvents(AgentEventRequest agentEventRequest) {
        log.info("Getting events for agent id {}", agentEventRequest.getAgentId());

        QueryTemplate queryTemplate = QueryBuilder
                .select()
                .from("agent_events")
                .where(QueryCondition.eq("client_id", agentEventRequest.getClientId()))
                .and(QueryCondition.eq("agent_id", agentEventRequest.getAgentId()))
                .and(QueryCondition.between("timestamp", agentEventRequest.getStart(), agentEventRequest.getEnd()))
                .and(QueryCondition.eq("event_type", agentEventRequest.getEventType()))
                .finishWhere()
                .orderBy("agent_service_id, seq_num", QueryDirection.ASC)
                .limit(agentEventRequest.getCount())
                .offset(agentEventRequest.getOffset())
                .build();

        return jdbcTemplate.getJdbcTemplate()
                .query(queryTemplate.getQuery(), new BeanPropertyRowMapper<>(AgentEventDAO.class), queryTemplate.getParameters().toArray());
    }

    @Override
    public List<AgentEventDAO> getAgentEventsByClient(AgentEventRequest agentEventRequest) {
        log.info("Getting events for client id {}", agentEventRequest.getClientId());

        QueryTemplate queryTemplate = QueryBuilder
                .select()
                .from("agent_events")
                .where(QueryCondition.eq("client_id", agentEventRequest.getClientId()))
                .and(QueryCondition.between("timestamp", agentEventRequest.getStart(), agentEventRequest.getEnd()))
                .finishWhere()
                .orderBy("agent_id, timestamp, seq_num", QueryDirection.ASC)
                .limit(agentEventRequest.getCount())
                .offset(agentEventRequest.getOffset())
                .build();

        return jdbcTemplate.getJdbcTemplate()
                .query(queryTemplate.getQuery(), new BeanPropertyRowMapper<>(AgentEventDAO.class), queryTemplate.getParameters().toArray());
    }

    @Override
    @Loggable
    public List<AgentEventDAO> getEventsByAgentExcludingLogoff(
            InteractionEventDAO event,
            Instant start,
            Instant end) {

        String sql = """
                SELECT *
                FROM agent_events
                WHERE client_id = :clientId 
                AND transaction_id = :transactionId
                AND event_type <> 'LOGOFF'
                UNION
                SELECT *
                FROM agent_events
                WHERE client_id = :clientId
                AND agent_id = :agentId
                AND agent_service_id = :serviceId
                AND created_time BETWEEN :from AND :to
                AND event_type <> 'LOGOFF'
                ORDER BY transaction_id, seq_num
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("clientId", event.getClientId())
                .addValue("transactionId", event.getTransactionId())
                .addValue("serviceId", event.getServiceId())
                .addValue("agentId", event.getAgentId())
                .addValue("from", start)
                .addValue("to", end);

        return jdbcTemplate.query(
                sql,
                parameters,
                new BeanPropertyRowMapper<>(AgentEventDAO.class));
    }

    @Override
    @Loggable
    public Optional<Instant> getNextAgentEventTimestamp(AgentEventDAO agentEvent) {

        String sql = """
                SELECT timestamp FROM agent_events 
                WHERE agent_id = :agentId
                AND timestamp >= :timestamp
                AND id <> :id
                AND transaction_id IS NULL 
                ORDER BY seq_num
                LIMIT 1
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("agentId", agentEvent.getAgentId())
                .addValue("timestamp", agentEvent.getTimestamp())
                .addValue("id", agentEvent.getId());

        List<Instant> result = jdbcTemplate.query(
                sql,
                parameters,
                new SingleColumnRowMapper<>(Instant.class));

        return result.stream().findFirst();

    }
}
