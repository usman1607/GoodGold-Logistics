package com.goodgold.logistics.controller;

import com.goodgold.logistics.model.Role;
import com.goodgold.logistics.model.User;
import com.goodgold.logistics.repository.RoleRepository;
import com.goodgold.logistics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RoleController {
    final
    UserRepository userRepository;
    final
    RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/roles/list", method = RequestMethod.GET)
    public String roles(Model model){
        model.addAttribute("roles", roleRepository.findAll());
        return "role/list";
    }

    @GetMapping("/roles/create")
    public String create(Model model){
        return "role/list";
    }

    @RequestMapping(value = "/roles/add", method = RequestMethod.POST)
    public String add(Model model, @RequestParam String name) {

        Role role = new Role(name);
        roleRepository.save(role);
        return "redirect:/roles/list";
    }

    @RequestMapping(value = "/roles/edit/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") int id, Model model) {

        model.addAttribute("role", roleRepository.findById(id).get());
        return "role/edit";
    }

    @RequestMapping(value = "/roles/update", method = RequestMethod.POST)
    public String updateRole(Model model, @RequestParam int id, @RequestParam String name) {

        Role role= roleRepository.findById(id).get();
        role.setName(name);

        roleRepository.save(role);

        return "redirect:/roles/list";
    }

    @RequestMapping(value = "/roles/delete/{id}", method = RequestMethod.GET)
    public String remove(@PathVariable("id") int id, Model model) {

        Role role = roleRepository.findById(id).get();

        roleRepository.delete(role);
        return "redirect:/roles/list";
    }

    @RequestMapping(value = "/staffs/removeRole/{id}", method = RequestMethod.GET)
    public String showAssignRoleForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("staff", userRepository.findById(id).get());
        return "role/removeRole";
    }

    @RequestMapping(value = "/staffs/removeOneRole", method = RequestMethod.POST)
    public String updateRole(Model model, @RequestParam long id, @RequestParam String name) {

        User s = userRepository.findById(id).get();
        Role role = roleRepository.findByName(name).get();

        List<Role> user_roles = s.getRoles();
        List<Integer> uR_id = new ArrayList<>();
        for(Role r : user_roles){
            uR_id.add(r.getId());
        }
        if(uR_id.contains(role.getId())){
            user_roles.remove(role);
            s.setRoles(user_roles);
            userRepository.save(s);
            return "redirect:/staffs/list";
        }else {
            model.addAttribute("dontHave", "The staff does not have the selected role.");
            return "role/removeRole";
        }
    }
}