package com.demo.caching.service;

import com.demo.caching.model.ProductRequest;
import com.demo.caching.model.ProductResponse;
import com.demo.caching.model.Products;
import com.demo.caching.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<List<ProductResponse>> getAllProducts() {

        log.info("calling method to fetch all products::");
        Optional<List<Products>> products = productRepository.getAllProducts();

        if(products.isPresent() && !products.get().isEmpty()) {
            List<ProductResponse> productResponses = new ArrayList<>();

            for(Products p : products.get()) {
                ProductResponse pr = new ProductResponse();
                BeanUtils.copyProperties(p, pr);
                productResponses.add(pr);
            }

            return Optional.of(productResponses);
        }
        return Optional.empty();
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {

        log.info("calling method to create a product::");
        Products products = Products
                            .builder()
                            .name(productRequest.getName())
                            .description(productRequest.getDescription())
                            .amount(productRequest.getAmount())
                            .build();

        products = productRepository.save(products);

        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(products, productResponse);

        return productResponse;

    }

    @Override
    public void deleteProductWithId(String id) {

        Optional<Products> idExists  = productRepository.findById(id);

        if(idExists.isPresent()){
            productRepository.deleteById(id);
        }
    }

    @Override
    public void updateProduct(ProductRequest productRequest) {

        int id = productRequest.getUuid();
        Optional<Products> idExists = productRepository.findById(String.valueOf(id));

        Products products = idExists.orElse(null);

        if(products!=null) {

            if (productRequest.getName() != null) {
                products.setName(productRequest.getName());
            }

            if (productRequest.getDescription() != null) {
                products.setDescription(productRequest.getDescription());
            }

            if (productRequest.getAmount() >= 0) {
                products.setAmount(productRequest.getAmount());
            }
            productRepository.save(products);
        }

    }
}
