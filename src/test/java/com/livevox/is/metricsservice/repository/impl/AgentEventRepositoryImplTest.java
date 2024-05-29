package com.livevox.is.metricsservice.repository.impl;

import com.livevox.is.domain.metrics.request.AgentEventRequest;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.domain.AgentEventDAO;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;
import com.livevox.is.util.sql.querybuilder.QueryBuilder;
import com.livevox.is.util.sql.querybuilder.QueryCondition;
import com.livevox.is.util.sql.querybuilder.QueryConditionBuilder;
import com.livevox.is.util.sql.querybuilder.QueryDirection;
import com.livevox.is.util.sql.querybuilder.QueryTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgentEventRepositoryImplTest {

    @InjectMocks
    private AgentEventRepositoryImpl agentEventRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private AgentEventRequest agentEventRequest;
    private QueryBuilder queryBuilder;
    private QueryConditionBuilder queryConditionBuilder;
    private QueryTemplate queryTemplate;

    @BeforeEach
    public void initialize() {
        Random random = new Random();

        agentEventRequest = spy(AgentEventRequest.class);
        agentEventRequest.setClientId(random.nextInt());
        agentEventRequest.setStart(Instant.now());
        agentEventRequest.setEnd(Instant.now());
        agentEventRequest.setCount(random.nextInt());
        agentEventRequest.setOffset(random.nextInt());

        queryBuilder = mock(QueryBuilder.class);
        queryConditionBuilder = mock(QueryConditionBuilder.class);
        queryTemplate = mock(QueryTemplate.class);
    }

    @Test
    public void getActiveAgentsTest() {

        prepareBuilder(true);

        try (MockedStatic<QueryBuilder> staticQueryBuilder = mockStatic(QueryBuilder.class)) {

            staticQueryBuilder.when(QueryBuilder::select).thenReturn(queryBuilder);

            agentEventRepository.getActiveAgents(agentEventRequest);
            staticQueryBuilder.verify(QueryBuilder::select);
            verify(namedParameterJdbcTemplate.getJdbcTemplate()).query(anyString(), ArgumentMatchers.<BeanPropertyRowMapper<IdClass>>any(), any(Object[].class));
        }
    }

    @Test
    public void getAgentEventsTest() {

        prepareBuilder(false);

        try (MockedStatic<QueryBuilder> staticQueryBuilder = mockStatic(QueryBuilder.class)) {

            staticQueryBuilder.when(QueryBuilder::select).thenReturn(queryBuilder);

            agentEventRepository.getAgentEvents(agentEventRequest);
            staticQueryBuilder.verify(QueryBuilder::select);
            verify(namedParameterJdbcTemplate.getJdbcTemplate()).query(anyString(), ArgumentMatchers.<BeanPropertyRowMapper<AgentEventDAO>>any(), any(Object[].class));
        }
    }

    @Test
    public void getAgentEventsByClientTest() {

        prepareBuilder(false);

        try (MockedStatic<QueryBuilder> staticQueryBuilder = mockStatic(QueryBuilder.class)) {

            staticQueryBuilder.when(QueryBuilder::select).thenReturn(queryBuilder);

            agentEventRepository.getAgentEventsByClient(agentEventRequest);
            staticQueryBuilder.verify(QueryBuilder::select);
            verify(namedParameterJdbcTemplate.getJdbcTemplate()).query(anyString(), ArgumentMatchers.<BeanPropertyRowMapper<AgentEventDAO>>any(), any(Object[].class));
        }
    }

    @Test
    public void testGetEventsByAgent() {

        Random random = new Random();

        InteractionEventDAO event = new InteractionEventDAO();
        event.setClientId(random.nextInt());
        event.setTransactionId(random.nextLong());
        event.setServiceId(random.nextInt());
        event.setAgentId(random.nextLong());

        Instant start = Instant.MIN;
        Instant end = Instant.MAX;
        ArgumentCaptor<MapSqlParameterSource> captor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        List<AgentEventDAO> agentEvents = new ArrayList<>();

        when(namedParameterJdbcTemplate
                .query(anyString(), captor.capture(), ArgumentMatchers.<BeanPropertyRowMapper<AgentEventDAO>>any()))
                .thenReturn(agentEvents);

        List<AgentEventDAO> events = agentEventRepository.getEventsByAgentExcludingLogoff(event, start, end);

        MapSqlParameterSource queryParameters = captor.getValue();

        assertEquals(agentEvents, events);
        assertEquals(event.getClientId(), queryParameters.getValue("clientId"));
        assertEquals(event.getTransactionId(), queryParameters.getValue("transactionId"));
        assertEquals(event.getServiceId(), queryParameters.getValue("serviceId"));
        assertEquals(event.getAgentId(), queryParameters.getValue("agentId"));
        assertEquals(start, queryParameters.getValue("from"));
        assertEquals(end, queryParameters.getValue("to"));

    }

    @Test
    public void testGetNextAgentEventTimestamp() {

        Random random = new Random();
        AgentEventDAO event = new AgentEventDAO();
        event.setAgentId(random.nextInt());
        event.setTimestamp(Instant.EPOCH);
        event.setId(random.nextLong());
        ArgumentCaptor<MapSqlParameterSource> captor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        Instant response = Instant.now();

        List<Instant> nextTimestamp = Collections.singletonList(response);
        when(namedParameterJdbcTemplate
                .query(anyString(), captor.capture(), ArgumentMatchers.<SingleColumnRowMapper<Instant>>any()))
                .thenReturn(nextTimestamp);

        Optional<Instant> nextAgentEvent = agentEventRepository.getNextAgentEventTimestamp(event);
        MapSqlParameterSource queryParameters = captor.getValue();

        assertTrue(nextAgentEvent.isPresent());
        assertEquals(response, nextAgentEvent.get());
        assertEquals(event.getId(), queryParameters.getValue("id"));
        assertEquals(event.getTimestamp(), queryParameters.getValue("timestamp"));
        assertEquals(event.getAgentId(), queryParameters.getValue("agentId"));
    }

    private void prepareBuilder(boolean distinct) {

        if (distinct) {
            when(queryBuilder.distinct(anyString())).thenReturn(queryBuilder);
        }

        when(queryBuilder.from(anyString())).thenReturn(queryBuilder);
        when(queryBuilder.where(any(QueryCondition.class))).thenReturn(queryConditionBuilder);
        when(queryConditionBuilder.and(any(QueryCondition.class))).thenReturn(queryConditionBuilder);
        when(queryConditionBuilder.finishWhere()).thenReturn(queryBuilder);
        when(queryBuilder.orderBy(anyString(), any(QueryDirection.class))).thenReturn(queryBuilder);
        when(queryBuilder.limit(anyInt())).thenReturn(queryBuilder);
        when(queryBuilder.offset(anyInt())).thenReturn(queryBuilder);
        when(queryBuilder.build()).thenReturn(queryTemplate);

        when(queryTemplate.getQuery()).thenReturn("");
        when(queryTemplate.getParameters()).thenReturn(new ArrayList<>());
    }

}
