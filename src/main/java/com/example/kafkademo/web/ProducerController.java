package com.example.kafkademo.web;

import com.example.kafkademo.kafka.MessagePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ProducerController {

    private static final Logger log = LoggerFactory.getLogger(ProducerController.class);
    private static final SecureRandom random = new SecureRandom();

    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String topic;

    public ProducerController(KafkaTemplate<String, MessagePayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private BigDecimal randomAmount() {
        double value = 1.0 + (9.0 * random.nextDouble()); // 1.00..10.00
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    @GetMapping("/produce")
    public Map<String, Object> produceTwo() {
        return producePair();
    }

    @GetMapping("/produce/{n}")
    public Map<String, Object> produceMany(@PathVariable("n") int n) {
        List<String> correlationIds = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            correlationIds.add(producePair().get("correlationId").toString());
        }
        return Map.of(
                "status", "sent",
                "count", n,
                "correlationIds", correlationIds
        );
    }

    private Map<String, Object> producePair() {
        UUID correlationId = UUID.randomUUID();
        Instant now = Instant.now();

        MessagePayload p1 = new MessagePayload(correlationId, 1, "Hello from part 1", randomAmount(), now);
        MessagePayload p2 = new MessagePayload(correlationId, 2, "Hello from part 2", randomAmount(), now);

        kafkaTemplate.send(topic, correlationId.toString(), p1);
        kafkaTemplate.send(topic, correlationId.toString(), p2);

        var total = p1.getAmount().add(p2.getAmount());
        log.info("Produced two messages with correlationId={} and total amount={}", correlationId, total);

        return Map.of(
                "status", "sent",
                "correlationId", correlationId.toString(),
                "topic", topic,
                "amount1", p1.getAmount(),
                "amount2", p2.getAmount(),
                "totalAmount", total
        );
    }
}
