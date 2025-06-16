package com.example.platform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendPaymentEvent(String event) {
        kafkaTemplate.send("payment-events", event);
    }
}
