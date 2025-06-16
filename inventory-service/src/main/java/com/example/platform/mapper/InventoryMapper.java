package com.example.platform.mapper;

import com.example.platform.dto.InventoryResponseDTO;
import com.example.platform.entity.InventoryItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InventoryMapper {

    InventoryResponseDTO toDTO(InventoryItem inventoryItem);

}
