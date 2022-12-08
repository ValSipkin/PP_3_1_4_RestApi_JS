package ru.kata.spring.boot_security.demo.cotrollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @GetMapping
    public String test(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        List<User> userList = userService.findAll();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userList", userList);
        model.addAttribute("newUser", new User());
        model.addAttribute("allRoles", roleService.findAll());

        return "admin";
    }
    @PostMapping("/new")
    public String addNewUser(@ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("editUser") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @DeleteMapping("/{id}")
    public String deleteUser(@ModelAttribute("deleteUser") User user) {
        userService.deleteUser(user);
        return  "redirect:/admin";
    }
}
