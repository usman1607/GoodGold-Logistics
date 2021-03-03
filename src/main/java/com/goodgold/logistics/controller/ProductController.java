package com.goodgold.logistics.controller;

import com.goodgold.logistics.model.viewmodels.RegisterProductModel;
import com.goodgold.logistics.model.viewmodels.RegisterShipmentModel;
import com.goodgold.logistics.service.ProductsServicesImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class ProductController {

    final
    ProductsServicesImpl productsServicesImpl;

    public ProductController(ProductsServicesImpl productsServicesImpl) {
        this.productsServicesImpl = productsServicesImpl;
    }

    //Get all products..
    @RequestMapping(path = "/products/list", method = RequestMethod.GET)
    public String products(Model model) {
       productsServicesImpl.getAllProducts(model);
       return "product/list";
    }

    //Display add product form
    @GetMapping("/products/create")
    public String create(Model model) {
        productsServicesImpl.showProductCreateForm(model);
        return "product/create";
    }

    //Add new product...
    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    public String add(@RequestParam long id, RegisterProductModel registerProductModel, RegisterShipmentModel registerShipmentModel) {
        productsServicesImpl.addProduct(id, registerProductModel, registerShipmentModel);
        return "redirect:/products/myList";
    }

    @GetMapping("/products/details/{id}")
    public String productDetails(@PathVariable("id") long id, Model model) {
        return  productsServicesImpl.productDetails(id, model);
    }

    @GetMapping("/products/approve/{id}")
    public String approveProduct(@PathVariable("id") long id, Model model) {
        productsServicesImpl.productApprove(id, model);
        return "redirect:/products/list";
    }

    @GetMapping("/products/disapprove/{id}")
    public String disapproveProduct(@PathVariable("id") long id, Model model) {
        productsServicesImpl.productDisapprove(id, model);
        return "redirect:/products/list";
    }

    //Get all the product(Prompt by the user) of a seller with the count...
    @GetMapping("/products/myList")
    public String myList(Model model) {
        return productsServicesImpl.myProductsList(model);
    }

    //Get all product belong to a user..
    @GetMapping("/users/products/{id}")
    public String userProducts(@PathVariable("id") long id, Model model) {
        return productsServicesImpl.userProductsList(id, model);
    }
}
