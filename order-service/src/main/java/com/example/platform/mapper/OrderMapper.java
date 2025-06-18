package com.example.platform.mapper;

import com.example.platform.dto.CreateOrderItem;
import com.example.platform.entity.Order;
import com.example.platform.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    @Mapping(target = "price", source = "price")
    OrderItem toOrderItem(CreateOrderItem order, BigDecimal price);

    @Mapping(target = "id", ignore = true)
    Order toOrder(OffsetDateTime createdAt, BigDecimal totalAmount, String status);
}
