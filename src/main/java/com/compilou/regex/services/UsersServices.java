package com.compilou.regex.services;

import com.compilou.regex.exceptions.CustomDataIntegrityViolationException;
import com.compilou.regex.exceptions.ResourceNotFoundException;
import com.compilou.regex.models.Users;
import com.compilou.regex.repositories.UsersRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServices {

    private final UsersRepository usersRepository;

    public UsersServices(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> findAllUsers() {
        return usersRepository.findAll();
    }

    public Users findUserById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
    }

    public Users create(Users user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null!");

        try {
            return usersRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new CustomDataIntegrityViolationException("Duplicated username or email!");
        }
    }

    public Users updateUser(Users user) {
        Users existingUser = usersRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        if (existingUser.getUsername().equals(user.getUsername())) {
            Optional<Users> userByUsername = usersRepository.findByUsername(user.getUsername());
            if (userByUsername.isPresent()) {
                throw new CustomDataIntegrityViolationException("Duplicated username!");
            }
        }

        if (existingUser.getEmail().equals(user.getEmail())) {
            Optional<Users> userByEmail = usersRepository.findByEmail(user.getEmail());
            if (userByEmail.isPresent()) {
                throw new CustomDataIntegrityViolationException("Duplicated email!");
            }
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setCellphone(user.getCellphone());

        return usersRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        var entity = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        usersRepository.delete(entity);
    }
}