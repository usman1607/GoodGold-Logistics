package com.goodgold.logistics.controller;

import com.goodgold.logistics.model.*;
import com.goodgold.logistics.model.viewmodels.RegisterProductModel;
import com.goodgold.logistics.model.viewmodels.RegisterShipmentModel;
import com.goodgold.logistics.repository.ProductRepository;
import com.goodgold.logistics.repository.ShipmentRepository;
import com.goodgold.logistics.repository.UserRepository;
import com.goodgold.logistics.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ShipmentController {
    
    final
    ShipmentRepository shipmentRepository;
    final
    WarehouseRepository warehouseRepository;
    final
    ProductRepository productRepository;

    final
    UserRepository userRepository;
    final
    EmailController emailController;

    public ShipmentController(ShipmentRepository shipmentRepository, WarehouseRepository warehouseRepository, ProductRepository productRepository, UserRepository userRepository, EmailController emailController) {
        this.shipmentRepository = shipmentRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.emailController = emailController;
    }

    public String getSignedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if(principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }else {
            username = principal.toString();
        }
        return username;
    }

    @RequestMapping(value = "/shipments/list", method = RequestMethod.GET)
    public String shipments(Model model){
//        List<Shipment> shipments = (List<Shipment>) shipmentRepository.findAll();
//        List<Product> products = null;
//        for(Shipment s : shipments){
//            products.add(productRepository.findProductByShipmentId(s.getId()));
//        }
//        model.addAttribute("products", products);
        model.addAttribute("shipments", shipmentRepository.findAll());
        return "shipment/list";
    }

    @GetMapping("/shipments/details/{id}")
    public String shipmentDetails(@PathVariable("id") long id, Model model){
        Product product = productRepository.findProductByShipmentId(id);
        Shipment shipment = shipmentRepository.findById(id).get();

        String username = getSignedUser();
        User u = userRepository.findUserByUsername(username);
        List<String> roles = new ArrayList<>();
        for(Role r : u.getRoles()){
            roles.add(r.getName());
        }

        if(product.getUser().getUsername().equals(username) || roles.contains("STAFF")) {
            model.addAttribute("shipment", shipment);
            model.addAttribute("product", product);
            return "shipment/details";
        }else {
            return "error/403";
        }
    }

    @GetMapping("/shipments/updateStatus/{id}")
    public String shipmentUpdateStatus(@PathVariable("id") long id, Model model){
        model.addAttribute("shipment", shipmentRepository.findById(id).get());
        return "shipment/updateStatus";
    }

    @RequestMapping(value = "/shipments/newStatus", method = RequestMethod.POST)
    public String updateStatus(Model model, @RequestParam long id, @RequestParam String newStatus, @RequestParam String actualDeliveryDate) throws ParseException {

        Shipment s = shipmentRepository.findById(id).get();
        s.setStatus(newStatus);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Date ADDate = formatter.parse(actualDeliveryDate);
        s.setActualDeliveryDate(ADDate);

        shipmentRepository.save(s);

        return "redirect:/shipments/list";
    }

    @GetMapping(value = "/shipments/create/{id}")
    public String showCreateForm(@PathVariable("id") long id, Model model){
        model.addAttribute("product", productRepository.findProductByShipmentId(id));
        model.addAttribute("shipment", shipmentRepository.findById(id).get());
        return "shipment/create";
    }

    @PostMapping(value = "/shipments/add")
    public String createShipment(Model model, @RequestParam long id, RegisterProductModel registerProductModel, RegisterShipmentModel registerShipmentModel) throws ParseException {
        Product p = productRepository.findById(id).get();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");

        p.setQuantity(registerProductModel.getQuantity());

        if(!registerShipmentModel.getShippingLocation().equals("")){
            p.getShipment().setShippingLocation(registerShipmentModel.getShippingLocation());
        }
        p.getShipment().setStatus("In Transit");
        Date shipDate = formatter.parse(registerShipmentModel.getShippingDate());
        Date EDD = formatter.parse(registerShipmentModel.getExpectedDeliveryDate());
        p.getShipment().setShippingDate(shipDate);
        p.getShipment().setExpectedDeliveryDate(EDD);
        p.getShipment().setTrackingNo(registerShipmentModel.getTrackingNo());

        productRepository.save(p);
        shipmentRepository.save(p.getShipment());

        return "redirect:/products/details/"+id;
    }

    @RequestMapping(value = "/shipments/edit/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Product product = productRepository.findProductByShipmentId(id);
        Shipment shipment = shipmentRepository.findById(id).get();
        if(!shipment.getStatus().equals("In Transit")){
            return "/error/403";
        }else {
            model.addAttribute("product", product);
            model.addAttribute("shipment", shipment);
            return "shipment/edit";
        }
    }

    @RequestMapping(value = "/shipments/update", method = RequestMethod.POST)
    public String updateShipment(Model model, @RequestParam long id, @RequestParam String note, @RequestParam int quantity, @RequestParam String status, @RequestParam String actualDeliveryDate) throws ParseException {
        Product p = productRepository.findById(id).get();
        Shipment s = p.getShipment();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");

//        Warehouse w = warehouseRepository.findWarehouseByCode(code);
//        s.setWarehouse(w);
//        Date shipDate = formatter.parse(registerShipmentModel.getShippingDate());
//        Date EDD = formatter.parse(registerShipmentModel.getExpectedDeliveryDate());
//        s.setShippingDate(shipDate);
//        s.setExpectedDeliveryDate(EDD);
//        s.setShippingLocation(registerShipmentModel.getShippingLocation());
        s.setStatus(status);
        Date ADDate = formatter.parse(actualDeliveryDate);
        s.setActualDeliveryDate(ADDate);
        p.setQuantity(quantity);

        productRepository.save(p);
        shipmentRepository.save(s);
        String to = p.getUser().getUsername();
        String subject = "Product Delivered";
        String msg = "Dear "+p.getUser().getFirstName()+" "+p.getUser().getLastName()+",\n\n"+
        note+"\n\n"+
                "Details:\n"+
                "Product Name: "+ p.getName()+"\n"+
                "Product Quantity: "+p.getQuantity()+"\n"+
                "Actual Delivery Date: "+ADDate+"\n"+
                "Warehouse: "+s.getWarehouse().getCode()+"\n"+
                "Tracking No: "+s.getTrackingNo()+"\n\n"+

                "Thanks.\n"+
                "GGL Team.";

        emailController.sendSimpleMessage(to, subject, msg);

        return "redirect:/shipments/list";
    }
}
