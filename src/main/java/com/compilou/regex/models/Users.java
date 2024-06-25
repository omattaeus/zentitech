package com.compilou.regex.models;

import com.compilou.regex.interfaces.CellPhoneValidator;
import com.compilou.regex.interfaces.CpfCnpjValidator;
import com.compilou.regex.interfaces.EmailValidator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({ "id", "username", "fullname",  "email", "cpfcnpj",
                            "birthday", "cellphone", "comunication"})
public class Users extends RepresentationModel<Users> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O nome de usuário não pode ser nulo!")
    @Column(name = "username", unique = true)
    @Size(min = 6, max = 12, message = "O nome de usuário deve ter de 6 a 12 caracteres")
    private String username;

    @NotEmpty(message = "O Nome Completo não pode ser nulo!")
    @Column(name = "full_name")
    @JsonProperty("fullname")
    private String fullName;

    @NotNull(message = "O e-mail não pode ser nulo!")
    @Column(name = "email", unique = true)
    @EmailValidator
    private String email;

    @Column(name = "cpfcnpj", unique = true)
    @NotNull(message = "CPF/CNPJ não pode ser nulo!")
    @JsonProperty("cpfcnpj")
    @CpfCnpjValidator(message = "CPF/CNPJ está incorreto!")
    private String cpfCnpj;

    @Column(name = "birthday")
    @NotNull(message = "A data de nascimento não pode ser nulo!")
    private String birthday;

    @NotNull(message = "O celular não pode ser nulo!")
    @Column(name = "cellphone")
    @CellPhoneValidator
    private String cellphone;

    @Column(name = "comunication")
    @NotNull(message = "A comunicação não pode ser nula!")
    private String comunication;
}