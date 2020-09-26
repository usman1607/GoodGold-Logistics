package com.goodgold.logistics.controller;

import com.goodgold.logistics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
public class IndexController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/layout")
    public String layout(Model model){
        return "/layout";
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

//    @PostMapping("/loginSecure")
//    public String loginSecure(@RequestAttribute("username") String username, @RequestAttribute("password")  String password) {
//
//        //does the authentication
//        final Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        username,
//                        password
//                )
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return "/index";
//    }

    @GetMapping("/admins/dashboard/{username}")
    public String admin(@PathVariable("username") String username, Model model){
        model.addAttribute("user", userRepository.findUserByUsername(username));
        return "/admin/dashboard";
    }
}
