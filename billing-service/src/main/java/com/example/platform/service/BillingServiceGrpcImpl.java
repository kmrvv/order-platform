package com.example.platform.service;

import com.example.grpc.BillingServiceGrpc;
import com.example.grpc.ChargeRequest;
import com.example.grpc.ChargeResponse;
import com.example.platform.dto.PaymentRequest;
import com.example.platform.mapper.PaymentMapper;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@GrpcService
@RequiredArgsConstructor
public class BillingServiceGrpcImpl extends BillingServiceGrpc.BillingServiceImplBase {
    private final BillingService billingService;
    private final PaymentMapper paymentMapper;

    @Override
    public void charge(ChargeRequest request, StreamObserver<ChargeResponse> responseObserver) {
        try {
            PaymentRequest paymentRequest = paymentMapper.toPaymentRequest(request.getOrderId(),
                    BigDecimal.valueOf(request.getAmount()));

            billingService.processPayment(paymentRequest);

            ChargeResponse response = ChargeResponse.newBuilder()
                    .setSuccess(true)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            ChargeResponse response = ChargeResponse.newBuilder()
                    .setSuccess(false)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}