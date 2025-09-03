package com.example.kafkademo.kafka;

import com.example.kafkademo.domain.MessageEntity;
import com.example.kafkademo.repo.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumer.class);
    private final MessageRepository messageRepository;

    public MessageConsumer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "${app.kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(MessagePayload payload) {
        log.info("Consumed message: correlationId={}, partNo={}, content={}, amount={}",
                payload.getCorrelationId(), payload.getPartNo(), payload.getContent(), payload.getAmount());
        MessageEntity entity = new MessageEntity(payload.getCorrelationId(), payload.getPartNo(), payload.getContent(), payload.getAmount());
        entity.setCreatedAt(payload.getCreatedAt());
        messageRepository.save(entity);
    }
}
