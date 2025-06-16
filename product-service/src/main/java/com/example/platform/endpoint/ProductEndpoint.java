package com.example.platform.endpoint;


import com.example.platform.service.ProductService;
import com.example.products.CreateProductRequest;
import com.example.products.CreateProductResponse;
import com.example.products.DeleteProductRequest;
import com.example.products.DeleteProductResponse;
import com.example.products.GetProductBySkuRequest;
import com.example.products.GetProductBySkuResponse;
import com.example.products.GetProductRequest;
import com.example.products.GetProductResponse;
import com.example.products.ListProductsRequest;
import com.example.products.ListProductsResponse;
import com.example.products.ProductDto;
import com.example.products.SearchProductsRequest;
import com.example.products.SearchProductsResponse;
import com.example.products.UpdateProductRequest;
import com.example.products.UpdateProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEndpoint {
    private static final String NAMESPACE = "http://example.com/products";
    private final ProductService productService;

    @PayloadRoot(namespace = NAMESPACE, localPart = "createProductRequest")
    @ResponsePayload
    public CreateProductResponse createProduct(@RequestPayload CreateProductRequest request) {
        log.info("SOAP: Creating product: {}", request.getProduct().getSku());
        productService.createProduct(request.getProduct());
        CreateProductResponse response = new CreateProductResponse();
        response.setProduct(request.getProduct());
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE, localPart = "getProductRequest")
    @ResponsePayload
    public GetProductResponse getProductById(@RequestPayload GetProductRequest req) {
        log.info("SOAP: getProduct id={}", req.getId());
        ProductDto dto = productService.getProductById(req.getId());
        GetProductResponse resp = new GetProductResponse();
        resp.setProduct(dto);
        return resp;
    }


    @PayloadRoot(namespace = NAMESPACE, localPart = "getProductBySkuRequest")
    @ResponsePayload
    public GetProductBySkuResponse getProductBySku(@RequestPayload GetProductBySkuRequest req) {
        log.info("SOAP: getProductBySku sku={}", req.getSku());
        ProductDto dto = productService.getProductBySku(req.getSku());
        GetProductBySkuResponse resp = new GetProductBySkuResponse();
        resp.setProduct(dto);
        return resp;
    }


    @PayloadRoot(namespace = NAMESPACE, localPart = "updateProductRequest")
    @ResponsePayload
    public UpdateProductResponse updateProduct(@Valid @RequestPayload UpdateProductRequest req) {
        log.info("SOAP: updateProduct with id: {}", req.getProduct().getId());
        ProductDto updated = productService.updateProduct(req.getProduct());
        UpdateProductResponse resp = new UpdateProductResponse();
        resp.setProduct(updated);
        return resp;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "deleteProductRequest")
    @ResponsePayload
    public DeleteProductResponse deleteProduct(@RequestPayload DeleteProductRequest req) {
        log.info("SOAP: deleteProduct id={}", req.getId());
        productService.deleteProduct(req.getId());
        DeleteProductResponse resp = new DeleteProductResponse();
        resp.setStatus("SUCCESS");
        return resp;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "listProductsRequest")
    @ResponsePayload
    public ListProductsResponse getListProducts(@RequestPayload ListProductsRequest req) {
        log.info("SOAP: listProducts limit={}, offset={}", req.getLimit(), req.getOffset());
        List<ProductDto> list = productService.getListProducts(req.getLimit(), req.getOffset());
        ListProductsResponse resp = new ListProductsResponse();
        resp.getProducts().addAll(list);
        return resp;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "searchProductsRequest")
    @ResponsePayload
    public SearchProductsResponse getProductsByName(@RequestPayload SearchProductsRequest req) {
        log.info("SOAP: searchProducts namePattern={}", req.getNamePattern());
        List<ProductDto> list = productService.getProductsByName(req.getNamePattern());
        SearchProductsResponse resp = new SearchProductsResponse();
        resp.getProducts().addAll(list);
        return resp;
    }
}
