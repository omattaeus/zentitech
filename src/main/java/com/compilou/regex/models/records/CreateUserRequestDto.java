package com.compilou.regex.models.records;

import com.compilou.regex.models.enums.RoleName;

public record CreateUserRequestDto(

        String email,
        String password,
        RoleName role
) {}