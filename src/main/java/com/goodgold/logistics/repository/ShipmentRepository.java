package com.goodgold.logistics.repository;

import com.goodgold.logistics.model.Shipment;
import org.springframework.data.repository.CrudRepository;

public interface ShipmentRepository extends CrudRepository<Shipment, Long> {
    //Shipment findById(long id);
}
