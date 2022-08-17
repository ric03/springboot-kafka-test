package com.example.demoArtifact.kafka;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class KafkaConsumerTest {

    @SpyBean
    private KafkaConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;

    @Captor
    private ArgumentCaptor<List<String>> messageArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> topicArgumentCaptor;

    @Test
    public void embeddedKafka_whenSendingToSimpleProducer_thenMessageReceived() {

        //Producer
        producer.send("Kartoffelsalat");

        //consumer
        verify(consumer, timeout(1000).times(1))
                .listen(messageArgumentCaptor.capture(), topicArgumentCaptor.capture());

        List<String> batchPayload = messageArgumentCaptor.getValue();

        assertThat(batchPayload.size()).isEqualTo(1);
        assertThat(topicArgumentCaptor.getValue()).contains(TOPIC_NAME);
        assertThat(batchPayload.get(0)).isEqualTo("Kartoffelsalat");
    }
}