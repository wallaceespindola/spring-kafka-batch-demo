package com.example.kafkademo.web;

import com.example.kafkademo.domain.JoinedRecordEntity;
import com.example.kafkademo.domain.MessageEntity;
import com.example.kafkademo.repo.JoinedRecordRepository;
import com.example.kafkademo.repo.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryControllerTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private JoinedRecordRepository joinedRecordRepository;

    @InjectMocks
    private QueryController controller;

    @Test
    void listMessages_returnsAllMessages() {
        MessageEntity m = new MessageEntity();
        when(messageRepository.findAll()).thenReturn(List.of(m));

        List<MessageEntity> result = controller.listMessages();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isSameAs(m);
    }

    @Test
    void listJoined_returnsAllJoinedRecords() {
        JoinedRecordEntity j = new JoinedRecordEntity();
        when(joinedRecordRepository.findAll()).thenReturn(List.of(j));

        List<JoinedRecordEntity> result = controller.listJoined();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isSameAs(j);
    }
}
