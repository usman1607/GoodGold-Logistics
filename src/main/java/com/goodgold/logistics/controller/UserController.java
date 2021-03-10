package com.goodgold.logistics.controller;

import com.goodgold.logistics.model.viewmodels.RegisterUserModel;
import com.goodgold.logistics.service.UserServicesImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    final
    UserServicesImpl userServicesImpl;

    public UserController(UserServicesImpl userServicesImpl) {
        this.userServicesImpl = userServicesImpl;
    }

    @GetMapping("/users/create")
    public String create(Model model) {
        return "user/register";
    }

    @PostMapping(value = "/users/register")
    public String register(Model model, RedirectAttributes redirectAttributes, RegisterUserModel registerUserModel) {
        return userServicesImpl.register(model, redirectAttributes, registerUserModel);
    }

    @RequestMapping(value = "/users/moreInfo/{id}", method = RequestMethod.GET)
    public String users(@PathVariable("id") long id, Model model) {
        return userServicesImpl.users(id, model);
    }

    @RequestMapping(value = "/sellers/list", method = RequestMethod.GET)
    public String sellers(Model model) {
        return userServicesImpl.sellers(model);
    }

//    @RequestMapping(value = "/myPage/page/{username}", method = RequestMethod.GET)
//    public String myPage(@PathVariable("username") String username, Model model){
//        model.addAttribute("user", userRepository.findUserByUsername(username));
//        return "admin/dashboard";
//    }

    @RequestMapping(value = "/myPage", method = RequestMethod.GET)
    public String myPage(Model model, Authentication authentication) {
        return userServicesImpl.myPage(model, authentication);
    }

    @RequestMapping(value = "/users/details/{id}", method = RequestMethod.GET)
    public String sellerDetails(@PathVariable("id") long id, Model model) {
        return userServicesImpl.sellerDetails(id, model);
    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public String showUpdateForm(Model model, Authentication authentication) {
        return userServicesImpl.showUpdateForm(model, authentication);
    }

    @PostMapping("/users/update")
    public String updateSellerProfile(Model model, @RequestParam long id, RegisterUserModel registerUserModel) {
        return userServicesImpl.updateSellerProfile(model, id, registerUserModel);
    }

    @GetMapping("/users/changePass")
    public String changePassword(Model model, Authentication authentication) {
        return userServicesImpl.changePassword(model, authentication);
    }

    @PostMapping("/users/updatePassword")
    public String updatePassword(Model model, @RequestParam long id, @RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes) {
        return userServicesImpl.updatePassword(model, id, oldPassword, newPassword, confirmPassword, redirectAttributes);
    }

    @RequestMapping(value = "/users/delete/{id}", method = RequestMethod.GET)
    public String removeUser(@PathVariable("id") long id, Model model) {
        return userServicesImpl.removeUser(id, model);
    }

    @RequestMapping(value = "/staffs/list", method = RequestMethod.GET)
    public String staffs(Model model) {
        return userServicesImpl.staffs(model);
    }

    @RequestMapping(value = "/staffs/details/{id}", method = RequestMethod.GET)
    public String staffDetails(@PathVariable("id") long id, Model model) {
        return userServicesImpl.staffDetails(id, model);
    }

    @GetMapping("/staffs/create")
    public String createStaff(Model model) {
        return "staff/create";
    }

    @RequestMapping(value = "/staffs/add", method = RequestMethod.POST)
    public String addStaff(Model model, RedirectAttributes redirectAttributes, RegisterUserModel registerUserModel) {
        return userServicesImpl.addStaff(model, redirectAttributes, registerUserModel);
    }

    @RequestMapping(value = "/staffs/assignRole/{id}", method = RequestMethod.GET)
    public String showAssignRoleForm(@PathVariable("id") long id, Model model) {
        return userServicesImpl.showAssignRoleForm(id, model);
    }

    @RequestMapping(value = "/staffs/assignNewRole", method = RequestMethod.POST)
    public String updateRole(Model model, @RequestParam long id, @RequestParam String name) {
        return userServicesImpl.updateRole(model, id, name);
    }

    @RequestMapping(value = "/staffs/delete/{id}", method = RequestMethod.GET)
    public String removeStaff(@PathVariable("id") long id, Model model) {
        return userServicesImpl.removeStaff(id, model);
    }

//    @RequestMapping(value = "/username", method = RequestMethod.GET)
//    @ResponseBody
//    public String currentUserName(Authentication authentication) {
//        return authentication.getName();
//    }

}
