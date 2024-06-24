package com.compilou.regex.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResetPassowordUserDAO {

    private String currentPassword;

    private String newPassword;


    public ResetPassowordUserDAO(String currentPassword) {
        this.currentPassword = currentPassword;
    }


    public ResetPassowordUserDAO() {}
}
