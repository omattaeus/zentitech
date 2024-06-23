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
import com.compilou.regex.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.compilou.regex.util.OtpUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final SecurityConfiguration securityConfiguration;

    public UserService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, UserRepository userRepository, EmailService emailService, RoleRepository roleRepository, SecurityConfiguration securityConfiguration) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.emailService = emailService;
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

    public String verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setActive(true);
            userRepository.save(user);
            return "OTP verified, you can login";
        }
        return "Please regenerate OTP and try again";
    }

    public String regenerateOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = OtpUtil.generateOtp();
        try {
            emailService.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send OTP, please try again");
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        return "Email sent... please verify account within 1 minute";
    }

    public String resetPassword(String otp, String newPassword) {
        User user = userRepository.findByOtp(otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));
        if (Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setPassword(newPassword);
            userRepository.save(user);
            return "Password reset successfully";
        }
        return "OTP expired, please regenerate OTP";
    }

    public String sendResetEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = OtpUtil.generateOtp();
        try {
            emailService.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send reset email, please try again");
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        return "Password reset email sent, please check your inbox";
    }
}