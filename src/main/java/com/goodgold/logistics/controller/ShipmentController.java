package com.goodgold.logistics.controller;

import com.goodgold.logistics.model.Category;
import com.goodgold.logistics.model.Shipment;
import com.goodgold.logistics.model.Warehouse;
import com.goodgold.logistics.model.viewmodels.RegisterShipmentModel;
import com.goodgold.logistics.repository.ProductRepository;
import com.goodgold.logistics.repository.ShipmentRepository;
import com.goodgold.logistics.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ShipmentController {
    
    final
    ShipmentRepository shipmentRepository;
    final
    WarehouseRepository warehouseRepository;
    final
    ProductRepository productRepository;

    public ShipmentController(ShipmentRepository shipmentRepository, WarehouseRepository warehouseRepository, ProductRepository productRepository) {
        this.shipmentRepository = shipmentRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
    }

    @RequestMapping(value = "/shipments/list", method = RequestMethod.GET)
    public String shipments(Model model){
        model.addAttribute("shipments", shipmentRepository.findAll());
        return "shipment/list";
    }

    @GetMapping("/shipments/details/{id}")
    public String shipmentDetails(@PathVariable("id") long id, Model model){
        model.addAttribute("shipment", shipmentRepository.findById(id).get());
        model.addAttribute("product", productRepository.findProductByShipmentId(id));
        return "shipment/details";
    }

    @GetMapping("/shipments/updateStatus/{id}")
    public String shipmentUpdateStatus(@PathVariable("id") long id, Model model){
        model.addAttribute("shipment", shipmentRepository.findById(id).get());
        return "shipment/updateStatus";
    }

    @RequestMapping(value = "/shipments/newStatus", method = RequestMethod.POST)
    public String updateStatus(Model model, @RequestParam long id, @RequestParam String newStatus) {

        Shipment s = shipmentRepository.findById(id).get();
        s.setStatus(newStatus);

        shipmentRepository.save(s);

        return "redirect:/shipments/list";
    }

    @RequestMapping(value = "/shipments/edit/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") long id, Model model) {

        model.addAttribute("shipment", shipmentRepository.findById(id).get());
        return "shipment/edit";
    }

    @RequestMapping(value = "/shipments/update", method = RequestMethod.POST)
    public String updateShipment(Model model, @RequestParam long id, @RequestParam String code, @RequestParam String status, RegisterShipmentModel registerShipmentModel) throws ParseException {

        Shipment s= shipmentRepository.findById(id).get();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");

        Warehouse w = warehouseRepository.findWarehouseByCode(code);
        s.setWarehouse(w);
        s.setShippingLocation(registerShipmentModel.getShippingLocation());
        s.setStatus(status);
        Date shipDate = formatter.parse(registerShipmentModel.getShippingDate());
        Date EDD = formatter.parse(registerShipmentModel.getExpectedDeliveryDate());
        s.setShippingDate(shipDate);
        s.setExpectedDeliveryDate(EDD);
        Date ADDate = formatter.parse(registerShipmentModel.getActualDeliveryDate());
        s.setActualDeliveryDate(ADDate);

        shipmentRepository.save(s);

        return "redirect:/shipments/list";
    }
}
