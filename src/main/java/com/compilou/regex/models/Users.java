package com.compilou.regex.models;

import com.compilou.regex.interfaces.CellPhoneValidator;
import com.compilou.regex.interfaces.EmailValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.Mapping;

import java.util.Objects;


@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({ "id", "username", "fullname",  "email", "cellphone" })
public class Users extends RepresentationModel<Users> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username cannot be null!")
    @Column(name = "username", unique = true)
    @Size(min = 6, max = 12, message = "The username must be 6 to 12 characters long")
    private String username;

    @NotEmpty(message = "The Full Name cannot be null!")
    @Column(name = "full_name")
    @JsonProperty("fullname")
    private String fullName;

    @NotNull(message = "Email cannot be null!")
    @Column(name = "email", unique = true)
    @EmailValidator
    private String email;

    @NotNull(message = "Cellphone cannot be null!")
    @Column(name = "cellphone")
    @CellPhoneValidator
    private String cellphone;
}