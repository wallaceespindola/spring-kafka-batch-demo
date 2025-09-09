package com.example.kafkademo.service;

import com.example.kafkademo.domain.JoinedRecordEntity;
import com.example.kafkademo.domain.MessageEntity;
import com.example.kafkademo.repo.JoinedRecordRepository;
import com.example.kafkademo.repo.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JoinServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private JoinedRecordRepository joinedRecordRepository;

    @InjectMocks
    private JoinService joinService;

    private UUID correlationId;
    private MessageEntity m1;
    private MessageEntity m2;

    @BeforeEach
    void setUp() {
        correlationId = UUID.randomUUID();
        m1 = new MessageEntity(correlationId, 1, "a", new BigDecimal("1.10"));
        m2 = new MessageEntity(correlationId, 2, "b", new BigDecimal("2.20"));
    }

    @Test
    void processReadyPairs_noReadyIds_doesNothing() {
        when(messageRepository.findReadyCorrelationIds()).thenReturn(List.of());

        joinService.processReadyPairs();

        verify(messageRepository, times(1)).findReadyCorrelationIds();
        verifyNoMoreInteractions(messageRepository);
        verifyNoInteractions(joinedRecordRepository);
    }

    @Test
    void processReadyPairs_joinsTwoMessages_marksProcessed_andSaves() {
        when(messageRepository.findReadyCorrelationIds()).thenReturn(List.of(correlationId));
        when(messageRepository.findByCorrelationIdOrderByPartNoAsc(correlationId)).thenReturn(List.of(m1, m2));

        joinService.processReadyPairs();

        // verify joined entity persisted
        ArgumentCaptor<JoinedRecordEntity> joinedCaptor = ArgumentCaptor.forClass(JoinedRecordEntity.class);
        verify(joinedRecordRepository).save(joinedCaptor.capture());
        JoinedRecordEntity savedJoined = joinedCaptor.getValue();
        assertThat(savedJoined.getCorrelationId()).isEqualTo(correlationId);
        assertThat(savedJoined.getContent1()).isEqualTo("a");
        assertThat(savedJoined.getContent2()).isEqualTo("b");
        assertThat(savedJoined.getTotalAmount()).isEqualByComparingTo(new BigDecimal("3.30"));

        // verify messages marked processed and saved
        assertThat(m1.isProcessed()).isTrue();
        assertThat(m2.isProcessed()).isTrue();
        verify(messageRepository, times(1)).save(m1);
        verify(messageRepository, times(1)).save(m2);
    }
}
