package com.example.kafkademo.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID correlationId;

    @Column(nullable = false)
    private Integer partNo;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private boolean processed = false;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public MessageEntity() {}

    public MessageEntity(UUID correlationId, Integer partNo, String content, BigDecimal amount) {
        this.correlationId = correlationId;
        this.partNo = partNo;
        this.content = content;
        this.amount = amount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getCorrelationId() { return correlationId; }
    public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }

    public Integer getPartNo() { return partNo; }
    public void setPartNo(Integer partNo) { this.partNo = partNo; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public boolean isProcessed() { return processed; }
    public void setProcessed(boolean processed) { this.processed = processed; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
