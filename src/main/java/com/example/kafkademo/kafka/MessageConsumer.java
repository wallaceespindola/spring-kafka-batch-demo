package com.example.kafkademo.kafka;

import com.example.kafkademo.domain.MessageEntity;
import com.example.kafkademo.repo.MessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {
    private final MessageRepository messageRepository;

    @KafkaListener(topics = "${app.kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(MessagePayload payload) {
        log.info("Consumed message: correlationId={}, partNo={}, content={}, amount={}",
                payload.getCorrelationId(), payload.getPartNo(), payload.getContent(), payload.getAmount());
        MessageEntity entity = new MessageEntity(payload.getCorrelationId(), payload.getPartNo(), payload.getContent(), payload.getAmount());
        entity.setCreatedAt(payload.getCreatedAt());
        messageRepository.save(entity);
    }
}

