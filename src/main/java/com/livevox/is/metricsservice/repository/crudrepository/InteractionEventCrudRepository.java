package com.livevox.is.metricsservice.repository.crudrepository;

import com.livevox.is.metricsservice.domain.InteractionEventDAO;
import com.livevox.is.metricsservice.repository.InteractionEventRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InteractionEventCrudRepository extends CrudRepository<InteractionEventDAO, Long>, InteractionEventRepository {

    @Query("""
            SELECT * FROM interaction_events 
            WHERE client_id = :clientId
            AND session_id = :sessionId
            ORDER BY transaction_id, timestamp, id
            """)
    Optional<InteractionEventDAO> getEventBySession(
            @Param("sessionId") String callSessionId,
            @Param("clientId") Integer clientId);
}
