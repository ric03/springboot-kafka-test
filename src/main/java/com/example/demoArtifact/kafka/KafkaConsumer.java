package com.example.demoArtifact.kafka;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(topics = "#{'${spring.kafka.topic.name}'.split(',')}")
    public void listen(List<String> recordBatch, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("------------------------------------------");
        log.info("Topic={}, Payload size={}", topic, recordBatch.size());
        recordBatch.forEach(record -> {
            log.info("Bank model Json: " + record);
        });
        log.info("------------------------------------------");
    }
}
