package com.goodgold.logistics.repository;

import com.goodgold.logistics.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    //Product findById(long id);
    List<Product> findProductsByUserUsername(String username);
    List<Product> findProductsByUserId(long id);
    Product findProductByShipmentId(long id);
}
