package com.example.kafkademo.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "joined_records")
@Getter
@Setter
@NoArgsConstructor
public class JoinedRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID correlationId;

    @Column(nullable = false, length = 1000)
    private String content1;

    @Column(nullable = false, length = 1000)
    private String content2;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private Instant joinedAt = Instant.now();

    public JoinedRecordEntity(UUID correlationId, String content1, String content2, BigDecimal totalAmount) {
        this.correlationId = correlationId;
        this.content1 = content1;
        this.content2 = content2;
        this.totalAmount = totalAmount;
    }
}

