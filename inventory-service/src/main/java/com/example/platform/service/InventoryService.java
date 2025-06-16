package com.example.platform.service;

import com.example.platform.dto.InventoryResponseDTO;
import com.example.platform.entity.InventoryItem;
import com.example.platform.exception.InventoryException;
import com.example.platform.mapper.InventoryMapper;
import com.example.platform.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;


    public InventoryResponseDTO getInventory(Long productId) {
        InventoryItem item = inventoryRepository.findById(productId).orElseThrow(() ->
                new InventoryException("Inventory Item not found with id:" + productId));
        log.info("InventoryService:    Get inventory for productId {}: {}", productId, item.getAvailable());
        return inventoryMapper.toDTO(item);
    }


    @Transactional
    public InventoryResponseDTO reserve(Long productId, int quantity) {
        InventoryItem item = inventoryRepository.findById(productId).orElseThrow(() ->
                new InventoryException("Inventory Item not found with id:" + productId));

        if (item.getAvailable() < quantity) {
            throw new InventoryException("Not enough inventory for product " + productId);
        }

        item.setAvailable(item.getAvailable() - quantity);
        inventoryRepository.save(item);
        log.info("InventoryService:    Reserved {} units for productId {}, remaining {}", quantity, productId,
                item.getAvailable());
        return inventoryMapper.toDTO(item);
    }


    @Transactional
    public InventoryResponseDTO release(Long productId, int quantity) {
        InventoryItem item = inventoryRepository.findById(productId).orElseThrow(() ->
                new InventoryException("Inventory Item not found with id:" + productId));

        item.setAvailable(item.getAvailable() + quantity);
        inventoryRepository.save(item);
        log.info("InventoryService:    Released {} units for productId {}, new total {}", quantity, productId,
                item.getAvailable());
        return inventoryMapper.toDTO(item);
    }
}
