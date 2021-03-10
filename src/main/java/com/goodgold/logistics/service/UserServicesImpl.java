package com.goodgold.logistics.service;

import com.goodgold.logistics.model.Role;
import com.goodgold.logistics.model.User;
import com.goodgold.logistics.model.viewmodels.RegisterUserModel;
import com.goodgold.logistics.repository.RoleRepository;
import com.goodgold.logistics.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServicesImpl implements IUserServices {

    final
    PasswordEncoder passwordEncoder;
    final UserRepository userRepository;
    final
    RoleRepository roleRepository;

    public UserServicesImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public String register(Model model, RedirectAttributes redirectAttributes, RegisterUserModel registerUserModel) {
        //String regex="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{8,20}$";
        //Pattern p = Pattern.compile(regex);
        // Matcher m = p.matcher(registerUserModel.getPassword());
        if (!registerUserModel.getPassword().equals(registerUserModel.getConfirmPassword())) {
            redirectAttributes.addAttribute("passError", "Password does not match ");
        } else if (userRepository.existsByUsername(registerUserModel.getUserName())) {
            redirectAttributes.addAttribute("emailError", "this email already exist");
        } else if ((!registerUserModel.getUserName().contains("@")) || (!registerUserModel.getUserName().contains("."))) {
            redirectAttributes.addAttribute("emailError", "the email is not a valid email address");
        } else if (registerUserModel.getAge() < 18) {
            redirectAttributes.addAttribute("ageError", "You must be 18+ to register");
        } else if (registerUserModel.getPassword().isBlank() || registerUserModel.getPassword().isEmpty()) {
            redirectAttributes.addAttribute("passError", "Password can not be empty or blank ");
        }

        //       else if( !m.matches()){
//            redirectAttributes.addAttribute("error","Password is not strong enough");
//        }
        else {
            User u = new User();
            u.setUsername(registerUserModel.getUserName());
            u.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));
            Optional<Role> optionalRole = roleRepository.findByName("SELLER");
            if (optionalRole.isPresent()) {
                Role role = optionalRole.get();
                List<Role> roleList = new ArrayList<>();
                roleList.add(role);
                u.setRoles(roleList);
            }

            u.setFirstName(registerUserModel.getFirstName());
            u.setLastName(registerUserModel.getLastName());
            u.setAge(registerUserModel.getAge());
            u.setSex(registerUserModel.getSex());
            u.setAddress(registerUserModel.getAddress());
            u.setCity(registerUserModel.getCity());
            u.setState(registerUserModel.getState());
            u.setCountry(registerUserModel.getCountry());
            u.setPhoneNo(registerUserModel.getPhoneNo());
            u.setTitle("Seller");

            userRepository.save(u);
            redirectAttributes.addAttribute("success", "You have successfully registered");
            redirectAttributes.addAttribute("successful", true);
            return "redirect:/users/create";
        }
        return "redirect:/users/create";
    }

    @Override
    public String users(long id, Model model) {
        User u = userRepository.findById(id).get();
        List<String> roles = new ArrayList<>();
        for (Role r : u.getRoles()) {
            roles.add(r.getName());
        }
        model.addAttribute("user", u);
        model.addAttribute("roles", roles);
        return "user/details";
    }

    @Override
    public String sellers(Model model) {
        model.addAttribute("sellers", userRepository.findUsersByTitleEquals("Seller"));
        return "user/SellersList";
    }

    @Override
    public String myPage(Model model, Authentication authentication) {
        String username = authentication.getName(); //getSignedUser();
        User u = userRepository.findUserByUsername(username);
        List<String> roles = new ArrayList<>();
        for (Role r : u.getRoles()) {
            roles.add(r.getName());
        }

        model.addAttribute("user", u);
        model.addAttribute("roles", roles);
        return "admin/dashboard";
    }

    @Override
    public String sellerDetails(long id, Model model) {
        model.addAttribute("user", userRepository.findUserById(id));
        return "/user/details";
    }

    @Override
    public String showUpdateForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("user", userRepository.findUserByUsername(username));
        return "user/edit";
    }

    @Override
    public String updateSellerProfile(Model model, long id, RegisterUserModel registerUserModel) {
        User u = userRepository.findById(id).get();

        u.setFirstName(registerUserModel.getFirstName());
        u.setLastName(registerUserModel.getLastName());
        u.setAddress(registerUserModel.getAddress());
        u.setCity(registerUserModel.getCity());
        u.setState(registerUserModel.getState());
        u.setCountry(registerUserModel.getCountry());
        u.setPhoneNo(registerUserModel.getPhoneNo());

        userRepository.save(u);
        String username = u.getUsername();

        return "redirect:/myPage";
    }

    @Override
    public String changePassword(Model model, Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("user", userRepository.findUserByUsername(username));
        return "user/editPassword";
    }

    @Override
    public String updatePassword(Model model, long id, String oldPassword, String newPassword, String confirmPassword, RedirectAttributes redirectAttributes) {
        User u = userRepository.findById(id).get();
        String username = u.getUsername();

//        String old = passwordEncoder.encode(oldPassword);
//        boolean isPasswordMatch = passwordEncoder.matches(oldPassword, u.getPassword());
        if (!passwordEncoder.matches(oldPassword, u.getPassword())) {
            redirectAttributes.addAttribute("error", "Password is not correct...");
        } else if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addAttribute("passError", "Password does not match ");
        } else if (newPassword.isBlank() || newPassword.isEmpty()) {
            redirectAttributes.addAttribute("passError", "New Password can not be empty or blank ");
        } else {
            String new_p = passwordEncoder.encode(newPassword);
            u.setPassword(new_p);
            userRepository.save(u);
            return "redirect:/myPage";
        }

        return "redirect:/users/changePass";
    }

    @Override
    public String removeUser(long id, Model model) {
        User user = userRepository.findById(id).get();
        userRepository.delete(user);
        return "redirect:/sellers/list";
    }

    @Override
    public String staffs(Model model) {
        model.addAttribute("staffs", userRepository.findUsersByTitleEquals("Staff"));
        return "staff/list";
    }

    @Override
    public String staffDetails(long id, Model model) {
        model.addAttribute("user", userRepository.findById(id).get());
        return "/staff/staffDetails";
    }

    @Override
    public String addStaff(Model model, RedirectAttributes redirectAttributes, RegisterUserModel registerUserModel) {
        if (!registerUserModel.getPassword().equals(registerUserModel.getConfirmPassword())) {
            redirectAttributes.addAttribute("passError", "Password does not match ");
        } else if (userRepository.existsByUsername(registerUserModel.getUserName())) {
            redirectAttributes.addAttribute("emailError", "this email already exist");
        } else if ((!registerUserModel.getUserName().contains("@")) || (!registerUserModel.getUserName().contains("."))) {
            redirectAttributes.addAttribute("emailError", "the email is not a valid email address");
        } else if (registerUserModel.getPassword().isBlank() || registerUserModel.getPassword().isEmpty()) {
            redirectAttributes.addAttribute("passError", "Password can not be empty or blank ");
        } else {
            User u = new User();
            u.setUsername(registerUserModel.getUserName());
            u.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));
            Optional<Role> optionalRole = roleRepository.findByName("STAFF");
            if (optionalRole.isPresent()) {
                Role role = optionalRole.get();
                List<Role> roleList = new ArrayList<>();
                roleList.add(role);
                u.setRoles(roleList);
            }

            u.setFirstName(registerUserModel.getFirstName());
            u.setLastName(registerUserModel.getLastName());
            u.setAge(registerUserModel.getAge());
            u.setSex(registerUserModel.getSex());
            u.setAddress(registerUserModel.getAddress());
            u.setCity(registerUserModel.getCity());
            u.setState(registerUserModel.getState());
            u.setCountry(registerUserModel.getCountry());
            u.setPhoneNo(registerUserModel.getPhoneNo());
            u.setJobTitle(registerUserModel.getJobTitle());
            u.setTitle("Staff");

            userRepository.save(u);
            redirectAttributes.addAttribute("success", "You have successfully added a staff");
            return "redirect:/staffs/create";
        }


        return "redirect:/staffs/create";
    }

    @Override
    public String showAssignRoleForm(long id, Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("staff", userRepository.findById(id).get());
        return "staff/assignRole";
    }

    @Override
    public String updateRole(Model model, long id, String name) {
        User s = userRepository.findById(id).get();
        Role role = roleRepository.findByName(name).get();

        List<Role> roleList = s.getRoles();
        roleList.add(role);
        s.setRoles(roleList);

        userRepository.save(s);

        return "redirect:/staffs/list";
    }

    @Override
    public String removeStaff(long id, Model model) {
        User s = userRepository.findById(id).get();

        userRepository.delete(s);
        return "redirect:/staffs/list";
    }
}
