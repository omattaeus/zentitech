package com.compilou.regex.services.auth;

import com.compilou.regex.configuration.SecurityConfiguration;
import com.compilou.regex.exceptions.ResourceNotFoundException;
import com.compilou.regex.models.Role;
import com.compilou.regex.models.User;
import com.compilou.regex.models.UserDetailsImpl;
import com.compilou.regex.models.enums.RoleName;
import com.compilou.regex.models.records.CreateUserRequestDto;
import com.compilou.regex.models.records.LoginUserRequestDto;
import com.compilou.regex.models.records.RecoveryJwtTokenDto;
import com.compilou.regex.repositories.RoleRepository;
import com.compilou.regex.repositories.UserRepository;
import com.compilou.regex.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.compilou.regex.util.OtpUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final SecurityConfiguration securityConfiguration;

    public UserService(AuthenticationManager authenticationManager,
                       JwtTokenService jwtTokenService,
                       UserRepository userRepository, EmailService emailService,
                       RoleRepository roleRepository, SecurityConfiguration securityConfiguration) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.roleRepository = roleRepository;
        this.securityConfiguration = securityConfiguration;
    }

    public RecoveryJwtTokenDto authenticateUser(LoginUserRequestDto loginUserRequestDto) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserRequestDto.email(),
                        loginUserRequestDto.password());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    @Transactional
    public CreateUserRequestDto createUser(CreateUserRequestDto createUserRequestDto) {

        Role customerRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER);
        if (customerRole == null) {
            throw new IllegalStateException("Role 'ROLE_CUSTOMER' não encontrada no sistema.");
        }

        User newUser = new User();
        newUser.setFullName(createUserRequestDto.fullName());
        newUser.setEmail(createUserRequestDto.email());
        newUser.setPassword(securityConfiguration.passwordEncoder().encode(createUserRequestDto.password()));
        newUser.setRoles(Set.of(customerRole));
        newUser.setActive(true);

        userRepository.save(newUser);
        return createUserRequestDto;
    }

    public String verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com este e-mail: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setActive(true);
            userRepository.save(user);
            return "OTP verified, you can login";
        }
        return "Please regenerate OTP and try again";
    }

    public String resetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP or Email"));

        if (Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() > (1 * 60)) {
            return "OTP expired, please regenerate OTP";
        }

        user.setPassword(newPassword);
        user.setOtp(null);
        user.setOtpGeneratedTime(null);
        userRepository.save(user);

        return "Success";
    }

    public void activateCustomerRole(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.activateCustomerRole();
            userRepository.save(user);
        } else {
            throw new RuntimeException("Usuário não encontrado com ID: " + userId);
        }
    }

    public void updateResetPasswordToken(String token, String email) throws ResourceNotFoundException {
        User user = userRepository.findByEmailResetPassword(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("Could not find any user with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }
}