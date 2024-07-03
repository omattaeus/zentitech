package com.compilou.regex.models;

import com.compilou.regex.models.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "user")
@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Campo Nome Completo não pode estar vazio!")
    @Column(name = "full_name")
    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(name = "active")
    private boolean active;
    private String otp;
    private LocalDateTime otpGeneratedTime;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name="users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles;

    public void activateCustomerRole() {
        Role customerRole = new Role();
        customerRole.setName(RoleName.ROLE_CUSTOMER);

        this.roles.add(customerRole);
    }
}