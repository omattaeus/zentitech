package com.compilou.regex.mapper.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "id", "username", "fullname",  "email", "cpfcnpj",
        "birthday", "cellphone", "comunication"})
public class UsersRequest {

        @NotNull(message = "Username cannot be null")
        @Size(min = 6, max = 12, message = "The username must be 6 to 12 characters long")
        private String username;

        @NotNull(message = "Full Name cannot be null")
        @JsonProperty("fullname")
        private String fullName;

        @NotNull(message = "Email cannot be null")
        private String email;

        @NotNull(message = "CPF/CNPJ cannot be null")
        @JsonProperty("cpfcnpj")
        private String cpfCnpj;

        @NotNull(message = "Birthday cannot be null")
        private String birthday;

        @NotNull(message = "Cellphone cannot be null")
        private String cellphone;

        @NotNull(message = "Comunication cannot be null")
        private String comunication;
}
