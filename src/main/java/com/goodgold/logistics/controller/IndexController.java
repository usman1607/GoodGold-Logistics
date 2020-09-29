package com.goodgold.logistics.controller;

import com.goodgold.logistics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/layout")
    public String layout(Model model){
        return "/layout";
    }

    @GetMapping("/layout2")
    public String layout2(Model model){
        return "/layout2";
    }

    @GetMapping("/about")
    public String about(Model model){
        return "/about";
    }

    @GetMapping("/contact")
    public String contact(Model model){
        return "/contact";
    }

    @GetMapping("/")
    public String index(Model model){

        return "/index";
    }

    @GetMapping("/login")
    public String login(){
        return "/login";
    }

    @GetMapping("/admins/dashboard/{username}")
    public String admin(@PathVariable("username") String username, Model model){
        model.addAttribute("user", userRepository.findUserByUsername(username));
        return "/admin/dashboard";
    }
    //https:/
    @GetMapping("/goodgoldlogistics.herokuapp.com")
    public String indexGgl(Model model){

        return "/index";
    }
}
