package com.goodgold.logistics.service;

import com.goodgold.logistics.controller.EmailController;
import com.goodgold.logistics.model.*;
import com.goodgold.logistics.model.viewmodels.RegisterProductModel;
import com.goodgold.logistics.model.viewmodels.RegisterShipmentModel;
import com.goodgold.logistics.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductsServicesImpl implements IProductServices {

    final
    ProductRepository productRepository;
    final
    WarehouseRepository warehouseRepository;
    final
    CategoryRepository categoryRepository;
    final
    UserRepository userRepository;
    final
    ShipmentRepository shipmentRepository;
    final
    EmailController emailController;

    public ProductsServicesImpl(ProductRepository productRepository, WarehouseRepository warehouseRepository, CategoryRepository categoryRepository, UserRepository userRepository, ShipmentRepository shipmentRepository, EmailController emailController) {
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.shipmentRepository = shipmentRepository;
        this.emailController = emailController;
    }

    //Get the signed in userName...
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

    @Override
    public Model getAllProducts(Model model) {

        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("allProducts", productRepository.count());
        return model;
    }

    @Override
    public Model showProductCreateForm(Model model) {
        String username = getSignedUser();
        model.addAttribute("warehouses", warehouseRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("user", userRepository.findUserByUsername(username));
        return model;
    }

    @Override
    public Product addProduct(long id, RegisterProductModel registerProductModel, RegisterShipmentModel registerShipmentModel) {

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

        return p;
    }

    @Override
    public String productDetails(long id, Model model) {

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

    @Override
    public void productApprove(long id, Model model) {
        Product product = productRepository.findById(id).get();
        product.setStatus("Approved");
        productRepository.save(product);

        String receiver = product.getUser().getUsername();
        String subject = "Product Approved";
        String msg = "Dear" + product.getUser().getFirstName() + " " + product.getUser().getLastName() + ",\n" +
                "We are pleased to tell you that your product had been approved by our warehouse team. You can now go" +
                " ahead to ship the product and send us the shipment details.\n Thanks.\n GGL Team.";
        emailController.sendSimpleMessage(receiver, subject, msg);
    }

    @Override
    public void productDisapprove(long id, Model model) {
        Product product = productRepository.findById(id).get();
        Shipment s = product.getShipment();
        product.setStatus("Disapproved");
        productRepository.save(product);

        s.setStatus("Product Disapproved");
        shipmentRepository.save(s);

        String receiver = product.getUser().getUsername();
        String subject = "Product Disapproved";
        String msg = "Dear" + product.getUser().getFirstName() + " " + product.getUser().getLastName() + ",\n" +
                "We are sorry to inform you that your product had been disapproved by our warehouse team, due to some reason." +
                " We regret any pain or inconvenience this might cause you.\n Thanks.\n GGL Team.";
        emailController.sendSimpleMessage(receiver, subject, msg);
    }

    @Override
    public String myProductsList(Model model) {
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

    @Override
    public String userProductsList(long id, Model model) {
        model.addAttribute("products", productRepository.findProductsByUserId(id));
        return "product/list";
    }
}
