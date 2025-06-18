package com.example.platform.client;

import com.example.products.GetProductRequest;
import com.example.products.GetProductResponse;
import com.example.products.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductClient {
    private final WebServiceTemplate webServiceTemplate;

    @Value("${soap.product-service.uri}")
    private String productServiceUri;

    public BigDecimal getPrice(Long productId) {
        GetProductRequest request = new GetProductRequest();
        request.setId(productId);

        GetProductResponse response = (GetProductResponse) webServiceTemplate
                .marshalSendAndReceive(productServiceUri, request);

        ProductDto product = response.getProduct();

        if (product == null || product.getPrice() == null) {
            throw new IllegalArgumentException("Product not found or price is null for id: " + productId);
        }

        return product.getPrice();
    }
}
