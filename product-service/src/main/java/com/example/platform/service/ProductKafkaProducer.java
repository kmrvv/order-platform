package com.example.platform.service;

import com.example.platform.dto.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductKafkaProducer {
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @Value("${spring.kafka.topics.product-created}")
    private String topic;

    public void sendProductCreated(ProductCreatedEvent event) {
        kafkaTemplate.send(topic, event);
        log.info("ProductKafkaProducer:     Sent product created event: {}", event);
    }
}
