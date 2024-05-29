package com.livevox.is.metricsservice.repository.crudrepository;

import com.livevox.is.metricsservice.domain.Audit;
import com.livevox.is.metricsservice.repository.GenericRepository;
import com.livevox.is.util.annotation.Loggable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenericCrudRepository extends CrudRepository<Audit, Long>, GenericRepository {
    @Query("""
            SELECT estado FROM tabla 
            WHERE id = :id
            """)
    @Loggable
    boolean getStateById(@Param("id") Integer id);
}
