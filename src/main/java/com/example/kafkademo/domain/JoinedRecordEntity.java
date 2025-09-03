package com.example.kafkademo.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "joined_records")
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

    public JoinedRecordEntity() {}

    public JoinedRecordEntity(UUID correlationId, String content1, String content2, BigDecimal totalAmount) {
        this.correlationId = correlationId;
        this.content1 = content1;
        this.content2 = content2;
        this.totalAmount = totalAmount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getCorrelationId() { return correlationId; }
    public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }

    public String getContent1() { return content1; }
    public void setContent1(String content1) { this.content1 = content1; }

    public String getContent2() { return content2; }
    public void setContent2(String content2) { this.content2 = content2; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Instant getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Instant joinedAt) { this.joinedAt = joinedAt; }
}
