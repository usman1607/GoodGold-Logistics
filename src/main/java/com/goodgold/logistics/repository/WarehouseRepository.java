package com.goodgold.logistics.repository;

import com.goodgold.logistics.model.Warehouse;
import org.springframework.data.repository.CrudRepository;

public interface WarehouseRepository extends CrudRepository<Warehouse, Integer> {
    Warehouse findWarehouseByCode(String code);
}
