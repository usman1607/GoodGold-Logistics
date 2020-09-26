package com.goodgold.logistics.controller;

import com.goodgold.logistics.model.Category;
import com.goodgold.logistics.model.Role;
import com.goodgold.logistics.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoryController {

    final
    CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping(value = "/categories/list", method = RequestMethod.GET)
    public String categories(Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        return "category/list";
    }

    @GetMapping("/categories/create")
    public String create(Model model){
        return "category/list";
    }

    @RequestMapping(value = "/categories/add", method = RequestMethod.POST)
    public String add(Model model, @RequestParam String name) {

        Category category = new Category(name);
        categoryRepository.save(category);
        return "redirect:/categories/list";
    }

    @RequestMapping(value = "/categories/edit/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") int id, Model model) {

        model.addAttribute("category", categoryRepository.findById(id).get());
        return "category/edit";
    }

    @RequestMapping(value = "/categories/update", method = RequestMethod.POST)
    public String updateCategory(Model model, @RequestParam int id, @RequestParam String name) {

        Category category= categoryRepository.findById(id).get();
        category.setName(name);

        categoryRepository.save(category);

        return "redirect:/categories/list";
    }

    @RequestMapping(value = "/categories/delete/{id}", method = RequestMethod.GET)
    public String remove(@PathVariable("id") int id, Model model) {

        Category category = categoryRepository.findById(id).get();

        categoryRepository.delete(category);
        return "redirect:/categories/list";
    }
}
