package com.compilou.regex.models.records;

public record CreateUserRequestDto(
        String fullName,
        String email,
        String password
) {}