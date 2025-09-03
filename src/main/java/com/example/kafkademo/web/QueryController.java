package com.example.kafkademo.web;

import com.example.kafkademo.domain.MessageEntity;
import com.example.kafkademo.domain.JoinedRecordEntity;
import com.example.kafkademo.repo.MessageRepository;
import com.example.kafkademo.repo.JoinedRecordRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QueryController {

    private final MessageRepository messageRepository;
    private final JoinedRecordRepository joinedRecordRepository;

    public QueryController(MessageRepository messageRepository, JoinedRecordRepository joinedRecordRepository) {
        this.messageRepository = messageRepository;
        this.joinedRecordRepository = joinedRecordRepository;
    }

    @GetMapping("/messages")
    public List<MessageEntity> listMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/joined")
    public List<JoinedRecordEntity> listJoined() {
        return joinedRecordRepository.findAll();
    }
}
