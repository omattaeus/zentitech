package com.compilou.regex.configuration;

import com.compilou.regex.models.User;
import com.compilou.regex.models.enums.RoleName;
import com.compilou.regex.repositories.RoleRepository;
import com.compilou.regex.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    @Value("${admin.email}")
    private String admin;
    @Value("${admin.password}")
    private String password;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final SecurityConfiguration securityConfiguration;

    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           SecurityConfiguration securityConfiguration) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.securityConfiguration = securityConfiguration;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = roleRepository.findByName(RoleName.ROLE_ADMINISTRATOR);
        var userAdmin = userRepository.findByEmail(admin);

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("Admin já existe!");
                },
                () -> {
                    var user = new User();
                    user.setEmail(admin);
                    user.setPassword(securityConfiguration.passwordEncoder().encode(password));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );

    }
}