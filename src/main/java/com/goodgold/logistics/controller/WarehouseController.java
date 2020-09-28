package com.goodgold.logistics.controller;

import com.goodgold.logistics.model.Warehouse;
import com.goodgold.logistics.model.viewmodels.RegisterWarehouseModel;
import com.goodgold.logistics.repository.WarehouseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WarehouseController {

    final
    WarehouseRepository warehouseRepository;

    public WarehouseController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @RequestMapping(value = "/warehouses/list", method = RequestMethod.GET)
    public String warehouses(Model model){
        model.addAttribute("warehouses", warehouseRepository.findAll());
        return "warehouse/list";
    }

    @GetMapping("/warehouses/create")
    public String create(Model model){
        return "warehouse/list";
    }

    @RequestMapping(value = "/warehouses/add", method = RequestMethod.POST)
    public String add(Model model, RegisterWarehouseModel registerWarehouseModel) {

        Warehouse w = new Warehouse();

        w.setCode(registerWarehouseModel.getCode());
        w.setAddress(registerWarehouseModel.getAddress());
        w.setCity(registerWarehouseModel.getCity());
        w.setState(registerWarehouseModel.getState());
        w.setCountry(registerWarehouseModel.getCountry());
        w.setCapacity(registerWarehouseModel.getCapacity());

        warehouseRepository.save(w);
        return "redirect:/warehouses/list";
    }

    @GetMapping("/warehouses/details/{id}")
    public String warehouseDetails(@PathVariable("id") int id, Model model){
        model.addAttribute("warehouse", warehouseRepository.findById(id).get());
        return "warehouse/details";
    }

    @RequestMapping(value = "/warehouses/edit/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") int id, Model model) {

        model.addAttribute("warehouse", warehouseRepository.findById(id).get());
        return "warehouse/edit";
    }

    @RequestMapping(value = "/warehouses/update", method = RequestMethod.POST)
    public String updateRole(Model model, @RequestParam int id, RegisterWarehouseModel registerWarehouseModel) {

        Warehouse w= warehouseRepository.findById(id).get();

        w.setCode(registerWarehouseModel.getCode());
        w.setAddress(registerWarehouseModel.getAddress());
        w.setCity(registerWarehouseModel.getCity());
        w.setState(registerWarehouseModel.getState());
        w.setCountry(registerWarehouseModel.getCountry());
        w.setCapacity(registerWarehouseModel.getCapacity());

        warehouseRepository.save(w);

        return "redirect:/warehouses/list";
    }

    @RequestMapping(value = "/warehouses/delete/{id}", method = RequestMethod.GET)
    public String remove(@PathVariable("id") int id, Model model) {

        Warehouse w = warehouseRepository.findById(id).get();

        warehouseRepository.delete(w);
        return "redirect:/warehouses/list";
    }
}
