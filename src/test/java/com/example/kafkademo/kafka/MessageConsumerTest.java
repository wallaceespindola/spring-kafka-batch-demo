package com.example.kafkademo.kafka;

import com.example.kafkademo.domain.MessageEntity;
import com.example.kafkademo.repo.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageConsumerTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageConsumer consumer;

    @Test
    void onMessage_persistsEntityWithPayloadData() {
        UUID cid = UUID.randomUUID();
        Instant now = Instant.now();
        MessagePayload payload = new MessagePayload(cid, 1, "hello", new BigDecimal("5.55"), now);

        consumer.onMessage(payload);

        ArgumentCaptor<MessageEntity> captor = ArgumentCaptor.forClass(MessageEntity.class);
        verify(messageRepository).save(captor.capture());
        MessageEntity saved = captor.getValue();
        assertThat(saved.getCorrelationId()).isEqualTo(cid);
        assertThat(saved.getPartNo()).isEqualTo(1);
        assertThat(saved.getContent()).isEqualTo("hello");
        assertThat(saved.getAmount()).isEqualByComparingTo("5.55");
        assertThat(saved.getCreatedAt()).isEqualTo(now);
        assertThat(saved.isProcessed()).isFalse();
    }
}
