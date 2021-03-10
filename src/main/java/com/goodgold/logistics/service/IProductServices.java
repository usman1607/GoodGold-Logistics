package com.goodgold.logistics.service;

import com.goodgold.logistics.model.Product;
import com.goodgold.logistics.model.viewmodels.RegisterProductModel;
import com.goodgold.logistics.model.viewmodels.RegisterShipmentModel;
import org.springframework.ui.Model;

public interface IProductServices {
    Model getAllProducts(Model model);
    Model showProductCreateForm(Model model);
    Product addProduct(long id, RegisterProductModel registerProductModel, RegisterShipmentModel registerShipmentModel);
    String productDetails(long id, Model model);
    void productApprove(long id, Model model);
    void productDisapprove(long id, Model model);
    String myProductsList(Model model);
    String userProductsList(long id, Model model);
}
