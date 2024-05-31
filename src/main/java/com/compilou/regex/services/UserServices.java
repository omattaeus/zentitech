package com.compilou.regex.services;

import com.compilou.regex.exceptions.ResourceNotFoundException;
import com.compilou.regex.models.User;
import com.compilou.regex.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {

    private final UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
    }

    public User create(User user) {
        if (user == null) throw new RuntimeException("User cannot be null!");
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        var entity = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setUsername(user.getUsername());
        entity.setFullName(user.getFullName());
        entity.setEmail(user.getEmail());
        entity.setCellphone(user.getCellphone());

        return userRepository.save(entity);
    }

    public void deleteUser(Long id) {
        var entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        userRepository.delete(entity);
    }
}