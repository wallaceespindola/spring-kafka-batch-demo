package com.example.kafkademo.web;

import com.example.kafkademo.kafka.MessagePayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProducerControllerTest {

    @Mock
    private KafkaTemplate<String, MessagePayload> kafkaTemplate;

    private ProducerController controller;

    @BeforeEach
    void setUp() {
        controller = new ProducerController(kafkaTemplate);
        // inject topic value (since @Value is not processed in unit test)
        TestUtils.setField(controller, "topic", "test-topic");
    }

    @Test
    void produceTwo_sendsTwoMessagesAndReturnsSummary() {
        Map<String, Object> result = controller.produceTwo();

        // two sends with same key occur
        ArgumentCaptor<MessagePayload> payloadCaptor = ArgumentCaptor.forClass(MessagePayload.class);
        verify(kafkaTemplate, times(2)).send(eq("test-topic"), any(String.class), payloadCaptor.capture());
        assertThat(payloadCaptor.getAllValues()).hasSize(2);

        // result map contains expected keys
        assertThat(result).containsKeys("status", "correlationId", "topic", "amount1", "amount2", "totalAmount");
        assertThat(result.get("status")).isEqualTo("sent");
        assertThat(result.get("topic")).isEqualTo("test-topic");

        // amounts are non-null and total equals sum
        BigDecimal a1 = (BigDecimal) result.get("amount1");
        BigDecimal a2 = (BigDecimal) result.get("amount2");
        BigDecimal total = (BigDecimal) result.get("totalAmount");
        assertThat(a1).isNotNull();
        assertThat(a2).isNotNull();
        assertThat(total).isEqualByComparingTo(a1.add(a2));
    }
}

class TestUtils {
    static void setField(Object target, String fieldName, Object value) {
        try {
            var f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
