package com.example.platform.controller;

import com.example.platform.dto.PaymentRequest;
import com.example.platform.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingController {
    private final BillingService billingService;

    @PostMapping("/charge")
    public ResponseEntity<String> charge(@Valid @RequestBody PaymentRequest request) {
        billingService.processPayment(request);
        return ResponseEntity.ok("Payment processed");
    }
}
