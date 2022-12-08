package ru.kata.spring.boot_security.demo.cotrollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String test(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        String currentUserRoles = "";
        for (Role role : currentUser.getRoles()) {
            currentUserRoles += role.getName() + " ";
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentUserRoles", currentUserRoles);

        return "user";
    }
}