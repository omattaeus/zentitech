package com.compilou.regex.util;

public class AuthUtil {

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED_SITE = {
            "/resources/**",
            "/static**",
            "/static/**",
            "/static/styles/**",
            "/static/image/**",
            "/styles/**",
            "/image/**",
            "/templates/**",
            "/templates/auth/**",
            "/templates/email/**",
            "/templates/js/**",
            "/templates/principal/**",
            "/ads.txt"
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
            "/admin/delete/{id}",
            "/admin/id/{id}",
            "/admin/update",
            "/admin/find/by/{firstName}",
            "/admin/api/sms/send",
            "/admin/success",
            "/admin/all",
            "/admin/all-users",
            "/admin/all**",
            "/admin/create",
            "/admin/create-html",
            "/admin/create-user",
            "/admin/search",
            "/admin/update-html",
            "/admin/update"
    };
}