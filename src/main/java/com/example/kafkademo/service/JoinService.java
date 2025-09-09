package com.example.kafkademo.service;

import com.example.kafkademo.domain.JoinedRecordEntity;
import com.example.kafkademo.domain.MessageEntity;
import com.example.kafkademo.repo.JoinedRecordRepository;
import com.example.kafkademo.repo.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JoinService {
    private final MessageRepository messageRepository;
    private final JoinedRecordRepository joinedRecordRepository;

    @Transactional
    public void processReadyPairs() {
        List<UUID> ready = messageRepository.findReadyCorrelationIds();
        if (ready.isEmpty()) {
            log.debug("No ready pairs to join");
            return;
        }
        for (UUID cid : ready) {
            var pair = messageRepository.findByCorrelationIdOrderByPartNoAsc(cid);
            if (pair.size() != 2) {
                log.warn("Expected 2 messages for {}, found {} — skipping", cid, pair.size());
                continue;
            }
            MessageEntity m1 = pair.get(0);
            MessageEntity m2 = pair.get(1);

            var joined = new JoinedRecordEntity(cid, m1.getContent(), m2.getContent(), m1.getAmount().add(m2.getAmount()));
            joinedRecordRepository.save(joined);

            m1.setProcessed(true);
            m2.setProcessed(true);
            messageRepository.save(m1);
            messageRepository.save(m2);

            log.info("Joined pair correlationId={} into joined_records id={}", cid, joined.getId());
        }
    }
}

