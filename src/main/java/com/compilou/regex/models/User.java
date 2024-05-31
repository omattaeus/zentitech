package com.compilou.regex.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username cannot be null!")
    @Column(name = "username", unique = true)
    @Size(min = 6, max = 12, message = "The username must be 6 to 12 characters long")
    private String username;

    @NotEmpty(message = "The Full Name cannot be null!")
    @Column(name = "full_name")
    private String fullName;

    @NotNull(message = "Email cannot be null!")
    @Column(name = "email", unique = true)
    @Email(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Invalid email!"
    )
    private String email;

    @NotNull(message = "Cellphone cannot be null!")
    @Column(name = "cellphone", unique = true)
    @Pattern(
            regexp = "\\(([0-9]{2})\\)\\s([0-9]{5})\\-[0-9]{4}",
            message = "Invalid cellphone!"
    )
    private String cellphone;

    public User() {}

    public User(Long id, String username, String fullName, String email, String cellphone) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.cellphone = cellphone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}