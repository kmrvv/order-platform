package com.example.platform.service;

import com.example.platform.dto.ProductCreatedEvent;
import com.example.platform.entity.InventoryItem;
import com.example.platform.mapper.InventoryMapper;
import com.example.platform.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCreatedListener {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @KafkaListener(topics = "${spring.kafka.topics.product-created}", groupId = "${spring.kafka.group-id}")
    public void handleProductCreated(ProductCreatedEvent event) {
        log.info("Received ProductCreated event: {}", event);

        InventoryItem item = inventoryMapper.toInventoryItem(event, 0);
        inventoryRepository.save(item);
    }
}
