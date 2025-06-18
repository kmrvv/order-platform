package com.example.platform.config;

import com.example.grpc.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BillingClientConfig {

    @Bean
    public ManagedChannel billingChannel() {
        return ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .keepAliveWithoutCalls(true)
                .usePlaintext()
                .build();
    }

    @Bean
    public BillingServiceGrpc.BillingServiceBlockingStub billingStub(ManagedChannel channel) {
        return BillingServiceGrpc.newBlockingStub(channel);
    }
}
