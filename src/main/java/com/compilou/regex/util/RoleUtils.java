/*
package com.compilou.regex.util;

import com.compilou.regex.models.Role;
import com.compilou.regex.models.User;
import com.compilou.regex.models.enums.RoleName;
import com.compilou.regex.repositories.RoleRepository;
import com.compilou.regex.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleUtils {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoleUtils(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public RoleName getUserRole(String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Role role = roleRepository.findByName(user.getRoles().toString());
            if (role != null) {
                return role.getName();
            } else {
                throw new IllegalStateException("Role not found for user: " + userEmail);
            }
        } else {
            throw new IllegalStateException("User not found: " + userEmail);
        }
    }
}
 */