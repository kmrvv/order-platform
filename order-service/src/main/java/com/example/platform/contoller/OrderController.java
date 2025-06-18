package com.example.platform.contoller;

import com.example.platform.dto.CreateOrderItem;
import com.example.platform.entity.Order;
import com.example.platform.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody List<CreateOrderItem> items) {
        Order order = orderService.createOrder(items);
        return ResponseEntity.ok(order);
    }
}
