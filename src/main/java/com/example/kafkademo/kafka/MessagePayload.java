package com.example.kafkademo.kafka;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class MessagePayload {
    private UUID correlationId;
    private int partNo;
    private String content;
    private BigDecimal amount;
    private Instant createdAt;

    public MessagePayload() {}

    public MessagePayload(UUID correlationId, int partNo, String content, BigDecimal amount, Instant createdAt) {
        this.correlationId = correlationId;
        this.partNo = partNo;
        this.content = content;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public UUID getCorrelationId() { return correlationId; }
    public void setCorrelationId(UUID correlationId) { this.correlationId = correlationId; }

    public int getPartNo() { return partNo; }
    public void setPartNo(int partNo) { this.partNo = partNo; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
