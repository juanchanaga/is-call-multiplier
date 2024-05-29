package com.livevox.is.metricsservice.repository.crudrepository;

import com.livevox.is.metricsservice.domain.AgentEventDAO;
import com.livevox.is.metricsservice.repository.AgentEventRepository;
import com.livevox.is.util.annotation.Loggable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentEventCrudRepository extends CrudRepository<AgentEventDAO, Long>, AgentEventRepository {
    @Query("""
            SELECT * FROM agent_events 
            WHERE transaction_id = :transactionId
            AND event_type <> 'LOGOFF'
            ORDER BY seq_num
            """)
    @Loggable
    List<AgentEventDAO> getEventsByTransactionIdExcludingLogoff(@Param("transactionId") Long transactionId);

    @Query("""
            SELECT * FROM agent_events 
            WHERE transaction_id = :transactionId
            ORDER BY seq_num
            """)
    List<AgentEventDAO> getEventsByTransactionId(@Param("transactionId") Long transactionId);
}
