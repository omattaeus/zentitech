package com.compilou.regex.util;

public class AuthUtil {

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED_SITE = {
            "/resources/**",
            "/static/**",
            "/static/styles/**",
            "/static/image/**",
            "/styles/**",
            "/image/**",
            "/templates/**",
            "/templates/auth/**",
            "/templates/email/**",
            "/templates/js/**",
            "/templates/principal/**"
    };

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/auth",
            "/",
            "/login",
            "/register",
            "/register-user",
            "/verify-account",
            "/regenerate-otp",
            "/reset-password",
            "/reset-password**",
            "/swagger-ui/**",
            "/send-reset-email",
            "/send-reset-email**",
            "/verify-account",
            "/verify-account**"
    };

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {
            "/users/test"
    };

    public static final String [] ENDPOINTS_CUSTOMER = {
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
            "/users/update-html",
            "/users/search"
    };

    public static final String [] ENDPOINTS_ADMIN = {
            "/users/test/administrator",
            "/users/delete/{id}"
    };
}