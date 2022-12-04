package ru.kata.spring.boot_security.demo.cotrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    {
        passwordEncoder = new BCryptPasswordEncoder();
    }
    @Autowired //можно не писать :)
    public AppController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/admin")
    public String adminPage(Principal principal, Model model) {
        List<User> userList = userService.findAll();
        model.addAttribute("userList", userList);
        model.addAttribute("currentUser", principal.getName());
        return "admin";
    }

    @GetMapping("/user")
    public String userPage(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("currentUser", principal.getName());
        return "user";
    }
    @GetMapping("/admin/addUser")
    public String newUserFormPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roleList", roleService.findAll());
        return "newUserForm";
    }
    @PostMapping("/admin")
    public String saveUser(@ModelAttribute("user") User user) {
        String password = user.getPassword();
        password = passwordEncoder.encode(password);
        user.setPassword(password);

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        for (int id: user.getRolesId()) {
            Role role =  roleService.findById(id);
            user.getRoles().add(role);
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @GetMapping("/admin/updateUser/{id}")
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

    @GetMapping("/admin/deleteUser/{id}")
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
    @PostMapping("/admin/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        userService.deleteUser(user);
        return "redirect:/admin";
    }
}
