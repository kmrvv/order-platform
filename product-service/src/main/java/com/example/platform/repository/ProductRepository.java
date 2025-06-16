package com.example.platform.repository;

import com.example.platform.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(Long id);
    void update(Product product);
    void delete(Long id);
    Optional<Product> findBySku(String sku);
    List<Product> findAll(@Param("limit") int limit, @Param("offset") int offset);
    List<Product> findByNameContaining(@Param("namePattern") String namePattern);
}
