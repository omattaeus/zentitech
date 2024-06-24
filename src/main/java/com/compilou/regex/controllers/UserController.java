package com.compilou.regex.controllers;

import com.compilou.regex.models.User;
import com.compilou.regex.models.records.CreateUserRequestDto;
import com.compilou.regex.models.records.LoginUserRequestDto;
import com.compilou.regex.models.records.RecoveryJwtTokenDto;
import com.compilou.regex.repositories.UserRepository;
import com.compilou.regex.services.auth.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoints for registering user by JWT")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login-user-jwt")
    @Operation(summary = "Authenticates a User",
            description = "Authenticates a User by passing in a JSON, representation of the user!",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = User.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody LoginUserRequestDto loginUserRequestDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserRequestDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("create-jwt")
    @Operation(summary = "Create a new User",
            description = "Create a new User by passing in a JSON, representation of the user!",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = User.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        userService.createUser(createUserRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/test")
    @Operation(summary = "Authenticated User, ADMIN e CUSTOMER", description = "Authenticated Users, ADMIN e CUSTOMER",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                                    )
                            }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<String> getAuthenticationTest() {
        return new ResponseEntity<>("Authenticated successfully!", HttpStatus.OK);
    }

    @GetMapping("/test/customer")
    @Operation(summary = "Authenticated User, CUSTOMER", description = "Authenticated User, CUSTOMER",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                                    )
                            }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<String> getCustomerAuthenticationTest() {
        return new ResponseEntity<>(
                "Client authenticated successfully!", HttpStatus.OK);
    }

    @GetMapping("/test/administrator")
    @Operation(summary = "Authenticated User, ADMINISTRATOR", description = "Authenticated User, ADMINISTRATOR",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                                    )
                            }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<String> getAdminAuthenticationTest() {
        return new ResponseEntity<>(
                "Administrator successfully authenticated!", HttpStatus.OK);
    }

    @GetMapping("/login-user")
    @Operation(summary = "Login - Page", description = "Login - Page",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                                    )
                            }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public String loginPage(Model model, String error) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        return "/auth/login";
    }

    @GetMapping("/register")
    @Operation(summary = "Register a User - Page", description = "Register a User - Page",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                                    )
                            }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public String registerPage() {
        return "/auth/register";
    }

    @PostMapping("/register-user")
    @Operation(summary = "Register a User", description = "Register a User",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                                    )
                            }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public String createUserHtml(CreateUserRequestDto createUserRequestDto, Model model) {
        userService.createUser(createUserRequestDto);
        model.addAttribute("message", "User registered successfully");
        return "redirect:/auth/login-user";
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticated User", description = "Authenticated User",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                                    )
                            }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public String authenticateUserHtml(LoginUserRequestDto loginUserRequestDto,
                                       Model model, HttpServletResponse response) {
        try {
            RecoveryJwtTokenDto token = userService.authenticateUser(loginUserRequestDto);
            Cookie cookie = new Cookie("token", token.token());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/users/create-html";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid email or password");
            return "Bad Request!";
        }
    }

    @PostMapping("/verify-account")
    public String verifyAccount(@RequestParam String email, @RequestParam String otp, Model model) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));

        if (!otp.equals(user.getOtp())) {
            model.addAttribute("error", "Código OTP inválido.");
            return "redirect:/auth/login";
        }

        if (Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() >= 60) {
            model.addAttribute("error", "O código OTP expirou. Por favor, gere um novo código.");
            return "redirect:/auth/login";
        }

        user.setActive(true);
        user.setOtp(null);
        user.setOtpGeneratedTime(null);
        userRepository.save(user);

        return "redirect:/auth/login";
    }

    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
    }

    @GetMapping("/send-reset-email")
    public String showSendResetEmailForm() {
        return "/auth/reset";
    }

    @PostMapping("/send-reset-email")
    public String sendResetEmail2(@RequestParam String email, Model model) {
        String result = userService.sendResetEmail(email);
        if (!"Password reset email sent, please check your inbox".equals(result)) {
            model.addAttribute("error", result);
            return "/auth/reset";
        }
        model.addAttribute("message", "Email de reset de senha enviado com sucesso!");
        return "/auth/login";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String email, @RequestParam String otp, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("otp", otp);
        return "/auth/reset";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String otp,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "As senhas não coincidem!");
            model.addAttribute("email", email);
            model.addAttribute("otp", otp);
            return "/auth/reset";
        }

        String result = userService.resetPassword(email, otp, newPassword);
        if (!"Success".equals(result)) {
            model.addAttribute("error", result);
            model.addAttribute("email", email);
            model.addAttribute("otp", otp);
            return "/auth/reset";
        }

        return "redirect:/auth/login";
    }

    @PostMapping("/regenerate-otp")
    public String regenerateOtp(@RequestParam String email, Model model) {
        String result = userService.regenerateOtp(email);
        if (!"Success".equals(result)) {
            model.addAttribute("error", result);
        }
        return "/auth/reset";
    }
}