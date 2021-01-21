package com.goodgold.logistics.repository;

import com.goodgold.logistics.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    //Product findById(long id);
    List<Product> findProductsByUserUsername(String username);

    List<Product> findProductsByUserId(long id);

    List<Product> findProductsByUserIdAndShipmentStatus(long id, String status);

    List<Product> findProductsByShipmentStatus(String status);

    Product findProductByShipmentId(long id);

    @Query(value = "SELECT COUNT(*) FROM product WHERE product.user_id = :id", nativeQuery = true)
    long allProducts(long id);
}
