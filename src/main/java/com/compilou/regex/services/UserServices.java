package com.compilou.regex.services;

import com.compilou.regex.exceptions.CustomDataIntegrityViolationException;
import com.compilou.regex.exceptions.ResourceNotFoundException;
import com.compilou.regex.models.User;
import com.compilou.regex.repositories.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if (user == null) throw new IllegalArgumentException("User cannot be null!");

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new CustomDataIntegrityViolationException("Duplicated username or email!");
        }
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        if (existingUser.getUsername().equals(user.getUsername())) {
            Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
            if (userByUsername.isPresent()) {
                throw new CustomDataIntegrityViolationException("Duplicated username!");
            }
        }

        if (existingUser.getEmail().equals(user.getEmail())) {
            Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
            if (userByEmail.isPresent()) {
                throw new CustomDataIntegrityViolationException("Duplicated email!");
            }
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setCellphone(user.getCellphone());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        var entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        userRepository.delete(entity);
    }
}