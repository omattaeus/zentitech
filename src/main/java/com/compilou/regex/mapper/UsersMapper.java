package com.compilou.regex.mapper;

import com.compilou.regex.mapper.request.UsersRequest;
import com.compilou.regex.mapper.response.UsersResponse;
import com.compilou.regex.models.Users;
import jakarta.validation.Valid;

public class UsersMapper {

    public static Users toUsers(@Valid UsersRequest request){

        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Users users = new Users();
        users.setUsername(request.getUsername());
        users.setFullName(request.getFullName());
        users.setEmail(request.getEmail());
        users.setCpfCnpj(request.getCpfCnpj());
        users.setBirthday(request.getBirthday());
        users.setCellphone(request.getCellphone());
        users.setComunication(request.getComunication());

        return users;
    }

    public static UsersResponse toUsersResponse(Users users){

        if (users == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        UsersResponse response = new UsersResponse();
        response.setId(users.getId());
        response.setUsername(users.getUsername());
        response.setFullName(users.getFullName());
        response.setEmail(users.getEmail());
        response.setCpfCnpj(users.getCpfCnpj());
        response.setBirthday(users.getBirthday());
        response.setCellphone(users.getCellphone());
        response.setComunication(users.getComunication());

        return response;
    }
}