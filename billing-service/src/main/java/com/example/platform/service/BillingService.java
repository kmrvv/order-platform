package com.example.platform.service;

import com.example.platform.dto.PaymentRequest;
import com.example.platform.entity.Payment;
import com.example.platform.mapper.PaymentMapper;
import com.example.platform.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {
    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;
    private final PaymentMapper paymentMapper;

    public void processPayment(PaymentRequest request) {
        log.info("Processing payment for orderId={}, amount={}", request.getOrderId(), request.getAmount());
        String status = simulatePayment() ? "SUCCESS" : "FAILED";
        Payment payment = paymentMapper.toPayment(request, status, OffsetDateTime.now());

        payment = paymentRepository.save(payment);
        log.info("Payment {} stored with id={}", status, payment.getId());

        String event = String.format("{\"paymentId\": \"%s\", \"orderId\": \"%s\", \"status\": \"%s\"}",
                payment.getId(), request.getOrderId(), status);

        paymentEventProducer.sendPaymentEvent(event);
        log.info("Payment event published: {}", event);
    }

    private boolean simulatePayment() {
        return ThreadLocalRandom.current().nextInt(100) < 90;
    }
}
