package com.example.platform.controller;

import com.example.platform.dto.InventoryResponseDTO;
import com.example.platform.dto.InventoryUpdateRequestDTO;
import com.example.platform.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    public InventoryResponseDTO getInventory(@PathVariable Long productId) {
        log.info("getInventory productId: {}", productId);
        return inventoryService.getInventory(productId);
    }

    @PostMapping("/{productId}/reserve")
    public InventoryResponseDTO reserve(@PathVariable Long productId,
                                        @RequestBody @Valid InventoryUpdateRequestDTO inventoryUpdateRequestDTO) {
        log.info("reserve productId: {}", productId);
        return inventoryService.reserve(productId, inventoryUpdateRequestDTO.getQuantity());
    }

    @PostMapping("/{productId}/release")
    public InventoryResponseDTO release(@PathVariable Long productId,
                                        @RequestBody @Valid InventoryUpdateRequestDTO inventoryUpdateRequestDTO) {
        log.info("release productId: {}", productId);
        return inventoryService.release(productId, inventoryUpdateRequestDTO.getQuantity());
    }
}
