package com.example.platform.service;

import com.example.platform.dto.ProductCreatedEvent;
import com.example.platform.exception.ProductNotFoundException;
import com.example.platform.entity.Product;
import com.example.platform.exception.ProductValidationException;
import com.example.platform.mapper.ProductMapper;
import com.example.platform.repository.ProductRepository;
import com.example.products.ProductDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final Validator validator;
    private final ProductKafkaProducer productKafkaProducer;

    @Transactional
    public void createProduct(ProductDto dto) {
        validateProduct(dto);
        String sku = dto.getSku();
        if (productRepository.findBySku(sku).isPresent()) {
            log.error("Duplicate product sku detected: {}", sku);
            throw new ProductValidationException("Product with sku " + sku + " already exists");
        }
        Product product = productMapper.toEntity(dto);
        product.setCreatedAt(OffsetDateTime.now());
        product.setUpdatedAt(OffsetDateTime.now());
        productRepository.save(product);
        log.info("ProductService:   Creating new product with id: {}", dto.getId());

        ProductCreatedEvent productCreatedEvent = productMapper.toProductCreatedEvent(product);
        productKafkaProducer.sendProductCreated(productCreatedEvent);
    }


    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(" Product with id={} not found", id);
                    return new ProductNotFoundException("Product not found with id: " + id);
                });
        ProductDto result = productMapper.toDto(product);
        log.info("ProductService:   Found product by id {}: {}", id, result.getName());
        return result;
    }


    @Transactional(readOnly = true)
    public ProductDto getProductBySku(String sku) {
        log.info("ProductService:   Fetching product by sku={}", sku);
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> {
                    log.error("ProductService:   Product with id={} not found", sku);
                    return new ProductNotFoundException("Product not found with sku: " + sku);
                });
        ProductDto result = productMapper.toDto(product);
        log.info("ProductService:   Found product by sku {}: {}", sku, result.getName());
        return result;
    }


    @Transactional
    public ProductDto updateProduct(ProductDto dto) {
        validateProduct(dto);
        Long id = dto.getId();
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with id={} not found", id);
                    return new ProductNotFoundException("Product not found with id: " + id);
                });
        String sku = dto.getSku();
        if (!existing.getSku().equals(sku)) {
            productRepository.findBySku(sku)
                    .orElseThrow(() -> {
                        log.error("Product sku {} not found", sku);
                        return new ProductValidationException("Product not uniq, found with sku: " + sku);
                    });
        }
        productMapper.updateEntityFromDto(dto, existing);
        existing.setUpdatedAt(OffsetDateTime.now());
        productRepository.update(existing);
        ProductDto result = productMapper.toDto(existing);
        log.info("ProductService:   Product updated with id: {}", id);
        return result;
    }


    @Transactional
    public void deleteProduct(Long id) {
        log.info("ProductService:   Product deleted: id={}", id);
        productRepository.delete(id);
    }


    @Transactional(readOnly = true)
    public List<ProductDto> getListProducts(int limit, int offset) {
        log.info("ProductService:   Listing products with limit={}, offset={}", limit, offset);
        List<ProductDto> result = productRepository.findAll(limit, offset)
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        log.info("ProductService:   Returned {} products", result.size());
        return result;
    }


    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByName(String name) {
        log.info("ProductService:   Searching products by name='{}'", name);
        List<ProductDto> result = productRepository.findByNameContaining(name)
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        log.info("ProductService:   Found {} products for name='{}'", result.size(), name);
        return result;
    }


    public void validateProduct(ProductDto dto) {
        Set<ConstraintViolation<ProductDto>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));

            throw new ValidationException("Validation failed: " + errorMessage);
        }
    }
}
