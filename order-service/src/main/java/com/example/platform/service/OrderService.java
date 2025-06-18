package com.example.platform.service;

import com.example.platform.client.BillingClient;
import com.example.platform.client.InventoryClient;
import com.example.platform.client.ProductClient;
import com.example.platform.dto.CreateOrderItem;
import com.example.platform.dto.InventoryUpdateRequestDTO;
import com.example.platform.entity.Order;
import com.example.platform.entity.OrderItem;
import com.example.platform.mapper.OrderMapper;
import com.example.platform.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final BillingClient billingClient;
    private final InventoryClient inventoryClient;
    private final ProductClient productClient;


    public Order createOrder(List<CreateOrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        Order order = orderMapper.toOrder(OffsetDateTime.now(), total, "CREATED");
        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateOrderItem item : items) {
            BigDecimal price = productClient.getPrice(item.getProductId());
            inventoryClient.reserve(item.getProductId(), new InventoryUpdateRequestDTO(item.getQuantity()));
            total = total.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));

            OrderItem orderItem = orderMapper.toOrderItem(item, price);
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setTotalAmount(total);
        orderRepository.save(order, orderItems);

        boolean paymentSuccess = billingClient.charge(order.getId(), total);
        order.setStatus(paymentSuccess ? "PAID" : "FAILED");
        orderRepository.updateStatus(order);

        return order;
    }

}
