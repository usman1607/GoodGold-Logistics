package com.goodgold.logistics.controller;

import com.goodgold.logistics.model.*;
import com.goodgold.logistics.model.viewmodels.RegisterProductModel;
import com.goodgold.logistics.model.viewmodels.RegisterShipmentModel;
import com.goodgold.logistics.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    final
    EmailController emailController;

    public ProductController(ProductRepository productRepository, ShipmentRepository shipmentRepository, WarehouseRepository warehouseRepository, CategoryRepository categoryRepository, UserRepository userRepository, EmailController emailController) {
        this.productRepository = productRepository;
        this.shipmentRepository = shipmentRepository;
        this.warehouseRepository = warehouseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.emailController = emailController;
    }

    public String getSignedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    @RequestMapping(path = "/products/list", method = RequestMethod.GET)
    public String products(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("allProducts", productRepository.count());

        return "product/list";
    }


    @GetMapping("/products/create")
    public String create(Model model) {

        String username = getSignedUser();
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

        return "redirect:/products/myList";
    }

    @GetMapping("/products/details/{id}")
    public String productDetails(@PathVariable("id") long id, Model model) {
        Product product = productRepository.findById(id).get();

        String username = getSignedUser();
        User u = userRepository.findUserByUsername(username);
        List<String> roles = new ArrayList<>();
        for (Role r : u.getRoles()) {
            roles.add(r.getName());
        }

        if (product.getUser().getUsername().equals(username) || roles.contains("STAFF")) {
            if (product.getStatus().equals("Not Yet Approved")) {
                model.addAttribute("approval", "approval");
                model.addAttribute("product", product);
                return "product/details";
            } else
                model.addAttribute("product", product);
            return "product/details";

        } else {
            return "error/403";
        }
    }

    @GetMapping("/products/approve/{id}")
    public String approveProduct(@PathVariable("id") long id, Model model) {
        Product product = productRepository.findById(id).get();
        product.setStatus("Approved");
        productRepository.save(product);

        String receiver = product.getUser().getUsername();
        String subject = "Product Approved";
        String msg = "Dear" + product.getUser().getFirstName() + " " + product.getUser().getLastName() + ",\n" +
                "We are pleased to tell you that your product had been approved by our warehouse team. You can now go" +
                " ahead to ship the product and send us the shipment details.\n Thanks.\n GGL Team.";
        emailController.sendSimpleMessage(receiver, subject, msg);

        return "redirect:/products/list";
    }

    @GetMapping("/products/disapprove/{id}")
    public String disapproveProduct(@PathVariable("id") long id, Model model) {
        Product product = productRepository.findById(id).get();
        Shipment s = product.getShipment();
        product.setStatus("Disapproved");
        productRepository.save(product);
        shipmentRepository.delete(s);

        String receiver = product.getUser().getUsername();
        String subject = "Product Disapproved";
        String msg = "Dear" + product.getUser().getFirstName() + " " + product.getUser().getLastName() + ",\n" +
                "We are sorry to inform you that your product had been disapproved by our warehouse team, due to some reason." +
                " We regret any pain or inconvenience this might cause you.\n Thanks.\n GGL Team.";
        emailController.sendSimpleMessage(receiver, subject, msg);

        return "redirect:/products/list";
    }

    @GetMapping("/products/myList")
    public String myList(Model model) {
        String username = getSignedUser();
        User u = userRepository.findUserByUsername(username);
        List<Product> products = productRepository.findProductsByUserUsername(username);
        model.addAttribute("products", products);

        for (Product p : products) {
            if (p.getShipment().getStatus().equals("Not Yet Shipped") && (p.getStatus().equals("Approved"))) {
                p.setShip("ship");
            }
        }
        long myProducts = productRepository.allProducts(u.getId());
        if (myProducts != 0) {
            model.addAttribute("myProducts", myProducts);
        } else {
            model.addAttribute("noProduct", "There is no product added yet...");
        }
        return "product/list";
    }

    @GetMapping("/users/products/{id}")
    public String userProducts(@PathVariable("id") long id, Model model) {
        model.addAttribute("products", productRepository.findProductsByUserId(id));
        return "product/list";
    }
}
