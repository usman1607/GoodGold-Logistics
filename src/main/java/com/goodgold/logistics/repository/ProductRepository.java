package com.goodgold.logistics.repository;

import com.goodgold.logistics.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Product findById(long id);
}
