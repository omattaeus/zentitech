package com.compilou.regex.models.records;

public record LoginUserRequestDto(
        String email,
        String password
) {}