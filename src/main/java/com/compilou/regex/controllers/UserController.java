package com.compilou.regex.controllers;

import com.compilou.regex.models.User;
import com.compilou.regex.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regex")
@Validated
public class UserController {

    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping(value = "/all")
    public List<User> findAllUsers() {
        return userServices.findAllUsers();
    }

    @GetMapping(value = "/id/{id}")
    public User findUsersById(@PathVariable(value = "id") Long id) {
        return userServices.findUserById(id);
    }

    @PostMapping(value = "/create")
    public User create(@Valid @RequestBody User user) {
        return userServices.create(user);
    }

    @PutMapping(value = "/update")
    public User update(@Valid @RequestBody User user) {
        return userServices.updateUser(user);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
        userServices.deleteUser(id);
    }
}