package com.example.platform.mapper;

import com.example.platform.dto.ProductCreatedEvent;
import com.example.platform.entity.Product;
import com.example.products.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    Product toEntity(ProductDto dto);
    ProductDto toDto(Product product);
    void updateEntityFromDto(ProductDto dto, @MappingTarget Product entity);

    ProductCreatedEvent toProductCreatedEvent(Product product);
}
