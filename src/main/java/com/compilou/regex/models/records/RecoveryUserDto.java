package com.compilou.regex.models.records;

import com.compilou.regex.models.Role;

import java.util.List;

public record RecoveryUserDto(

        Long id,
        String email,
        List<Role> roles

) {}