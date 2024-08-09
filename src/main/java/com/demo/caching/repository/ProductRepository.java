package com.demo.caching.repository;

import com.demo.caching.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products,String> {

    @Query(value ="select * from products_data", nativeQuery = true)
    Optional<List<Products>> getAllProducts();
}
