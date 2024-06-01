package com.compilou.regex.models;

import com.compilou.regex.interfaces.CellPhoneValidator;
import com.compilou.regex.interfaces.EmailValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.Mapping;

import java.util.Objects;

@Entity
@Table(name = "users")
@JsonPropertyOrder({ "id", "username", "fullname",  "email", "cellphone" })
public class Users extends RepresentationModel<Users> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

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

    public Users() {}

    public Users(Long key, String username, String fullName, String email, String cellphone) {
        this.key = key;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.cellphone = cellphone;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Users users = (Users) o;
        return Objects.equals(key, users.key) && Objects.equals(username, users.username) && Objects.equals(fullName, users.fullName) && Objects.equals(email, users.email) && Objects.equals(cellphone, users.cellphone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, username, fullName, email, cellphone);
    }
}