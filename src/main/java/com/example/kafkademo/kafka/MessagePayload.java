package com.example.kafkademo.kafka;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePayload {
    private UUID correlationId;
    private int partNo;
    private String content;
    private BigDecimal amount;
    private Instant createdAt;
}

