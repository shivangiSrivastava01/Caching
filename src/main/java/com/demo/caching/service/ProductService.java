package com.demo.caching.service;

import com.demo.caching.model.ProductRequest;
import com.demo.caching.model.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {

    Optional<List<ProductResponse>> getAllProducts();

    ProductResponse createProduct(ProductRequest productRequest);

    void deleteProductWithId(String id);

    void updateProduct(ProductRequest productRequest);

}
