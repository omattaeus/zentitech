package com.compilou.regex.util;

public class AuthUtil {

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/auth",
            "/auth/login",
            "/auth/login-user",
            "/auth/register",
            "/auth/register-user"
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