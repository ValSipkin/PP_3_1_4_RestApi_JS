package ru.kata.spring.boot_security.demo.cotrollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.HashSet;
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
    public String adminPage(Principal principal, Model model) {
        List<User> userList = userService.findAll();
        model.addAttribute("userList", userList);
        model.addAttribute("currentUser", principal.getName());
        return "admin";
    }
    @PostMapping
    public String saveUser(@ModelAttribute("user") User user) {
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        for (int id: user.getRolesId()) {
            Role role =  roleService.findById(id);
            user.getRoles().add(role);
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @GetMapping("/addUser")
    public String newUserFormPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roleList", roleService.findAll());
        return "newUserForm";
    }

    @GetMapping("/updateUser/{id}")
    public String updateUserFormPage(@PathVariable("id") int id, Model model) {
        User user = userService.findById(id);
        user.setPassword(null);
        user.setRolesId(new int[user.getRoles().size()]);
        int i = 0;
        for (Role role : user.getRoles()) {
            user.getRolesId()[i++] = role.getId();
        }
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleService.findAll());
        return "updateUserForm";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUserFormPage(@PathVariable("id") int id, Model model) {
        User user = userService.findById(id);
        user.setPassword(null);
        user.setRolesId(new int[user.getRoles().size()]);
        int i = 0;
        for (Role role : user.getRoles()) {
            user.getRolesId()[i++] = role.getId();
        }
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleService.findAll());
        return "deleteUserForm";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        userService.deleteUser(user);
        return "redirect:/admin";
    }
}
