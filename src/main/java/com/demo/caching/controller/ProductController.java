package com.demo.caching.controller;

import com.demo.caching.model.ProductRequest;
import com.demo.caching.model.ProductResponse;
import com.demo.caching.service.ProductService;
import com.demo.caching.service.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
@Slf4j
public class ProductController {

    public final ProductServiceImpl productService;

    @Autowired
    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @Cacheable(value = "productCache")
    @GetMapping("/products")
    public List<ProductResponse> getAllProducts() {
        try {
            Optional<List<ProductResponse>> productData = productService.getAllProducts();

            if (productData.isPresent() && !productData.get().isEmpty()) {
                log.info("Product data found!");
                return productData.get();
            } else {
                log.info("Product data not found.");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("Exception occurred while retrieving the product data {}", e.getMessage());
            return Collections.emptyList();
        }
    }


    @CacheEvict(value = "productCache",allEntries = true)
    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductRequest productRequest){
        try{
            var product = productService.createProduct(productRequest);

            if(product==null){
                throw new Exception("Unable to create the product!!");
            }
            return new ResponseEntity<>("Product created Successfully!!", HttpStatus.CREATED);

        }catch(Exception e){
            log.error("Exception Occurred while creating product!! {}",e.getMessage());
            return new ResponseEntity<>("Exception Occurred while creating product. Please check!!!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id){

        try{
            productService.deleteProductWithId(id);
            return new ResponseEntity<>("Product Deleted!!!",HttpStatus.OK);

        }catch(Exception e){
            log.error("Exception Occurred while deleting product data!!! {}",e.getMessage());
            return new ResponseEntity<>("Exception Occurred while deleting the product data!!",HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProduct(@Valid @RequestBody  ProductRequest productRequest){

        try{
            productService.updateProduct(productRequest);
            return new ResponseEntity<>("Product data updated successfully!!",HttpStatus.CREATED);

        }catch(Exception e){
            log.error("Exception Occurred while updating product data!!! {}",e.getMessage());
            return new ResponseEntity<>("Exception Occurred while deleting the product data!!",HttpStatus.FORBIDDEN);
        }
    }
}
