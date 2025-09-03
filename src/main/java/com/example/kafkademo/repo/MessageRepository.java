package com.example.kafkademo.repo;

import com.example.kafkademo.domain.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("select m.correlationId from MessageEntity m where m.processed = false group by m.correlationId having count(m) = 2")
    List<UUID> findReadyCorrelationIds();

    List<MessageEntity> findByCorrelationIdOrderByPartNoAsc(UUID correlationId);

    long countByCorrelationId(UUID correlationId);
}
