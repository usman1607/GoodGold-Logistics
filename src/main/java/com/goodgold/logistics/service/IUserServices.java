package com.goodgold.logistics.service;

import com.goodgold.logistics.model.viewmodels.RegisterUserModel;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface IUserServices {
    String register(Model model, RedirectAttributes redirectAttributes, RegisterUserModel registerUserModel);
    String users(long id, Model model);
    String sellers(Model model);
    String myPage(Model model, Authentication authentication);
    String sellerDetails(long id, Model model);
    String showUpdateForm(Model model, Authentication authentication);
    String updateSellerProfile(Model model, long id, RegisterUserModel registerUserModel);
    String changePassword(Model model, Authentication authentication);
    String updatePassword(Model model, long id, String oldPassword, String newPassword, String confirmPassword, RedirectAttributes redirectAttributes);
    String removeUser(long id, Model model);
    String staffs(Model model);
    String staffDetails(long id, Model model);
    String addStaff(Model model, RedirectAttributes redirectAttributes, RegisterUserModel registerUserModel);
    String showAssignRoleForm(long id, Model model);
    String updateRole(Model model, long id, String name);
    String removeStaff(long id, Model model);
}
