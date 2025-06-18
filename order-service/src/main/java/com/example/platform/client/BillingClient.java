package com.example.platform.client;

import com.example.grpc.BillingServiceGrpc;
import com.example.grpc.ChargeRequest;
import com.example.grpc.ChargeResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BillingClient {

    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public boolean charge(Long orderId, BigDecimal amount) {
        ChargeRequest request = ChargeRequest.newBuilder()
                .setOrderId(orderId)
                .setAmount(amount.doubleValue())
                .build();

        ChargeResponse response = blockingStub.charge(request);
        return response.getSuccess();
    }
}
