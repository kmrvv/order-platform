package com.example.platform.client;

import com.example.platform.dto.InventoryUpdateRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service", url = "http://localhost:8082")
public interface InventoryClient {
    @PostMapping("/inventory/{productId}/reserve")
    void reserve(@PathVariable Long productId, @RequestBody InventoryUpdateRequestDTO inventoryUpdateRequestDTO);
}
