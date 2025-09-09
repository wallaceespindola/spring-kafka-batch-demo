package com.example.kafkademo.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
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

    public MessageEntity(UUID correlationId, Integer partNo, String content, BigDecimal amount) {
        this.correlationId = correlationId;
        this.partNo = partNo;
        this.content = content;
        this.amount = amount;
    }
}

