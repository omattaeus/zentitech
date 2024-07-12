package com.compilou.regex.util;

public class AuthUtil {

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED_SITE = {
            "/resources/**",
            "/static**",
            "/static/**",
            "/static/styles/**",
            "/static/image/**",
            "/static/img/**",
            "/styles/**",
            "/image/**",
            "/templates/**",
            "/templates/auth/**",
            "/templates/email/**",
            "/static/js/**",
            "/templates/principal/**",
    };

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/auth",
            "/",
            "/login",
            "/login-users",
            "/register",
            "/register-user",
            "/verify-account",
            "/regenerate-otp",
            "/reset-password",
            "/reset-password**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/send-reset-email",
            "/send-reset-email**",
            "/verify-account",
            "/verify-account**",
            "/webhook/stripe",
            "/payment/**",
            "/payment**",
            "/payment/checkout",
            "/payment/teste",
            "/create-payment-intent",
            "/ads.txt",
            "/forgot_password",
            "/reset_password"
    };

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {
            "/users/test"
    };

    public static final String[] ENDPOINTS_CUSTOMER = {
            "/users/test/customer",
            "/users/id/{id}",
            "/users/update",
            "/users/find/by/{firstName}",
            "/api/sms/send",
            "/users/success",
            "/users/all",
            "/users/all-users",
            "/users/all**",
            "/users/create",
            "/users/create-html",
            "/users/create-user",
            "/users/search",
            "/users/update-html",
            "/users/update"
    };

    public static final String[] ENDPOINTS_ADMIN = {
            "/admin/test/administrator",
            "/admin/delete/{id}"
    };
}