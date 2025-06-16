package com.example.platform.mapper;

import com.example.platform.dto.PaymentRequest;
import com.example.platform.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.OffsetDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "id", ignore = true)
    Payment toPayment(PaymentRequest paymentRequest, String status, OffsetDateTime createdAt);
}
