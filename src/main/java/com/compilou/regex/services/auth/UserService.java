package com.compilou.regex.services.auth;

import com.compilou.regex.configuration.SecurityConfiguration;
import com.compilou.regex.models.Role;
import com.compilou.regex.models.User;
import com.compilou.regex.models.UserDetailsImpl;
import com.compilou.regex.models.records.CreateUserRequestDto;
import com.compilou.regex.models.records.LoginUserRequestDto;
import com.compilou.regex.models.records.RecoveryJwtTokenDto;
import com.compilou.regex.repositories.RoleRepository;
import com.compilou.regex.repositories.UserRepository;
import com.compilou.regex.services.auth.JwtTokenService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenService jwtTokenService;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecurityConfiguration securityConfiguration;

    public UserService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, UserRepository userRepository, RoleRepository roleRepository, SecurityConfiguration securityConfiguration) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.securityConfiguration = securityConfiguration;
    }


    public RecoveryJwtTokenDto authenticateUser(LoginUserRequestDto loginUserRequestDto) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserRequestDto.email(), loginUserRequestDto.password());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    public void createUser(CreateUserRequestDto createUserRequestDto) {
        Role role = roleRepository.findByName(createUserRequestDto.role());
        if (role == null) {
            role = new Role();
            role.setName(createUserRequestDto.role());
            roleRepository.save(role);
        }

        User newUser = new User();
        newUser.setEmail(createUserRequestDto.email());
        newUser.setPassword(securityConfiguration.passwordEncoder().encode(createUserRequestDto.password()));
        newUser.setRoles(List.of(role));

        try {
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Duplicated username or email!");
        }
    }
}