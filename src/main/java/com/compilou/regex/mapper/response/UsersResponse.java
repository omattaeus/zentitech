package com.compilou.regex.mapper.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String cpfCnpj;
    private Date birthday;
    private String cellphone;
    private  String comunication;
}
