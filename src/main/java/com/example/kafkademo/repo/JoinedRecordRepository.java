package com.example.kafkademo.repo;

import com.example.kafkademo.domain.JoinedRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinedRecordRepository extends JpaRepository<JoinedRecordEntity, Long> {
}
