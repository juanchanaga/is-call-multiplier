package com.livevox.is.metricsservice.repository.impl;

import com.livevox.is.domain.metrics.request.ActiveServiceMetricsRequest;
import com.livevox.is.domain.metrics.request.ActiveServicesRequest;
import com.livevox.is.domain.util.IdClass;
import com.livevox.is.metricsservice.domain.InteractionEventDAO;
import com.livevox.is.util.sql.querybuilder.QueryBuilder;
import com.livevox.is.util.sql.querybuilder.QueryCondition;
import com.livevox.is.util.sql.querybuilder.QueryConditionBuilder;
import com.livevox.is.util.sql.querybuilder.QueryDirection;
import com.livevox.is.util.sql.querybuilder.QueryTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.livevox.is.domain.metrics.request.MetricsDirection.INBOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InteractionEventRepositoryImplTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Captor
    private ArgumentCaptor<String> queryCaptor;

    @InjectMocks
    private InteractionEventRepositoryImpl interactionEventRepository;

    @Test
    public void testGetActiveServices() {

        Random random = new Random();
        String expectedQuery = """
                SELECT DISTINCT agent_skill_id AS id \
                FROM interaction_events \
                WHERE created_time BETWEEN ? AND ? \
                AND call_center_id = ?\
                """;

        List<IdClass> expectedResponse = new ArrayList<>();
        ActiveServicesRequest request = new ActiveServicesRequest();
        request.setCallCenterId(random.nextInt());
        request.setStart(Instant.now());
        request.setEnd(Instant.now());

        when(jdbcTemplate.getJdbcTemplate().query(
                queryCaptor.capture(),
                ArgumentMatchers.<BeanPropertyRowMapper<IdClass>>any(),
                any(Object[].class)))
                .thenReturn(expectedResponse);

        List<IdClass> response = interactionEventRepository.getActiveServices(request);

        assertEquals(expectedResponse, response);
        assertEquals(expectedQuery, queryCaptor.getValue());
    }

    @Test
    public void testGetActiveServiceInteractionDetails() {

        Random random = new Random();
        QueryBuilder queryBuilder = mock(QueryBuilder.class, RETURNS_SELF);
        QueryConditionBuilder queryConditionBuilder = mock(QueryConditionBuilder.class, RETURNS_SELF);
        QueryTemplate queryTemplate = mock(QueryTemplate.class);
        ActiveServiceMetricsRequest request = new ActiveServiceMetricsRequest();
        request.setServiceId(random.nextInt());
        request.setCallCenterId(random.nextInt());
        request.setStart(Instant.MIN);
        request.setDirection(INBOUND);
        request.setCount(random.nextInt());
        request.setOffset(random.nextInt());

        List<InteractionEventDAO> response = new ArrayList<>();

        when(queryBuilder.where(any()))
                .thenReturn(queryConditionBuilder);
        when(queryBuilder.build())
                .thenReturn(queryTemplate);
        when(queryConditionBuilder.finishWhere())
                .thenReturn(queryBuilder);
        when(jdbcTemplate.getJdbcTemplate().query(
                queryCaptor.capture(),
                ArgumentMatchers.<BeanPropertyRowMapper<InteractionEventDAO>>any(),
                any(Object[].class)))
                .thenReturn(response);
        try (MockedStatic<QueryBuilder> builder = mockStatic(QueryBuilder.class)) {

            builder.when(QueryBuilder::select).thenReturn(queryBuilder);
            List<InteractionEventDAO> details = interactionEventRepository.getActiveServiceInteractionDetails(request);
            assertEquals(response, details);
        }

        verify(queryBuilder).from(eq("interaction_events"));
        verify(queryBuilder).where(any(QueryCondition.class));
        verify(queryConditionBuilder, times(4)).and(any(QueryCondition.class));
        verify(queryBuilder).orderBy(eq("start"), eq(QueryDirection.ASC));
        verify(queryBuilder).limit(eq(request.getCount()));
        verify(queryBuilder).offset(eq(request.getOffset()));


    }
}