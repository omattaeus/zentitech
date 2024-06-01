package com.compilou.regex.models.records;

import com.compilou.regex.models.enums.RoleName;

public record CreateUserDto(

        String email,
        String password,
        RoleName role
) {}
