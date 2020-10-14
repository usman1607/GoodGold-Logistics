package com.goodgold.logistics.controller;

import com.goodgold.logistics.model.*;
import com.goodgold.logistics.model.viewmodels.RegisterProductModel;
import com.goodgold.logistics.model.viewmodels.RegisterShipmentModel;
import com.goodgold.logistics.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ProductController {

    final
    ProductRepository productRepository;
    final
    ShipmentRepository shipmentRepository;
    final
    UserRepository userRepository;
    final
    WarehouseRepository warehouseRepository;
    final
    CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, ShipmentRepository shipmentRepository, WarehouseRepository warehouseRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.shipmentRepository = shipmentRepository;
        this.warehouseRepository = warehouseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/products/list", method = RequestMethod.GET)
    public String products(Model model){
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("allProducts", productRepository.count());
        return "product/list";
    }

    @GetMapping("/products/create/{username}")
    public String create(@PathVariable("username") String username,  Model model){

        model.addAttribute("warehouses", warehouseRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("user", userRepository.findUserByUsername(username));
        return "product/create";
    }

    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    public String add(Model model, @RequestParam long id, RegisterProductModel registerProductModel, RegisterShipmentModel registerShipmentModel) {

        Product p = new Product();
        Shipment s = new Shipment();

//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");

        Warehouse w = warehouseRepository.findWarehouseByCode(registerShipmentModel.getWarehouseCode());
        s.setWarehouse(w);
        s.setShippingLocation(registerShipmentModel.getShippingLocation());
        s.setStatus("Not Yet Shipped");
//        Date shipDate = formatter.parse(registerShipmentModel.getShippingDate());
//        Date EDD = formatter.parse(registerShipmentModel.getExpectedDeliveryDate());
//        s.setShippingDate(shipDate);
//        s.setExpectedDeliveryDate(EDD);
//        s.setTrackingNo(registerShipmentModel.getTrackingNo());

        User user = userRepository.findById(id).get();
        Category c = categoryRepository.findCategoryByName(registerProductModel.getCategoryName());
        p.setName(registerProductModel.getName());
        p.setStatus("Not Yet Approved");
        p.setCategory(c);
        p.setShipment(s);
        p.setUser(user);
        p.setQuantity(registerProductModel.getQuantity());
        p.setDescription(registerProductModel.getDescription());

        shipmentRepository.save(s);
        productRepository.save(p);

        String username = user.getUsername();

        return "redirect:/products/myList/"+username;
    }

    @GetMapping("/products/details/{id}")
    public String productDetails(@PathVariable("id") long id, Model model){
        Product product = productRepository.findById(id).get();
        if(product.getStatus().equals("Not Yet Approved")){
            model.addAttribute("approval", "approval");
            model.addAttribute("product", product);
            return "product/details";
        }else
        model.addAttribute("product", product);
        return "product/details";
    }

    @GetMapping("/products/approve/{id}")
    public String approveProduct(@PathVariable("id") long id, Model model){
        Product product = productRepository.findById(id).get();
        product.setStatus("Approved");
        productRepository.save(product);

        return "product/list";
    }

    @GetMapping("/products/disapprove/{id}")
    public String disapproveProduct(@PathVariable("id") long id, Model model){
        Product product = productRepository.findById(id).get();
        product.setStatus("Disapproved");
        productRepository.save(product);

        return "product/list";
    }

    @GetMapping("/products/myList/{username}")
    public String myList(@PathVariable("username") String username, Model model){
        User u = userRepository.findUserByUsername(username);
        List<Product> products = productRepository.findProductsByUserUsername(username);
        model.addAttribute("products", products);

        for(Product p : products) {
            if (p.getShipment().getStatus().equals("Not Yet Shipped") && (p.getStatus().equals("Approved"))) {
                p.setShip("ship");
            }
        }
        long myProducts = productRepository.allProducts(u.getId());
        if(myProducts != 0){
            model.addAttribute("myProducts", myProducts);
        }else {
            model.addAttribute("noProduct", "There is no product added yet...");
        }
        return "product/list";
    }

    @GetMapping("/users/products/{id}")
    public String userProducts(@PathVariable("id") long id, Model model){
        model.addAttribute("products", productRepository.findProductsByUserId(id));
        return "product/list";
    }
}
