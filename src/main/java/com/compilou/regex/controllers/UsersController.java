package com.compilou.regex.controllers;

import com.compilou.regex.models.Users;
import com.compilou.regex.services.EmailService;
import com.compilou.regex.services.UsersServices;
import com.compilou.regex.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@Tag(name = "Users", description = "Endpoints for Managing Users")
@RequestMapping("/regex")
@Validated
public class UsersController {

    @Autowired
    private UsersServices usersServices;
    @Autowired
    private EmailService emailService;

    public UsersController() {}

    public UsersController(UsersServices usersServices, EmailService emailService) {
        this.usersServices = usersServices;
        this.emailService = emailService;
    }

    @GetMapping(value = "/all",
            consumes = { MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
            produces = { MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
    @Operation(summary = "Finds all Users", description = "Finds all Users",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = Users.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PagedModel<EntityModel<Users>>> findAllUsers(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "username"));

        return ResponseEntity.ok(usersServices.findAllUsers(pageable));
    }

    @GetMapping(value = "/find/by/{firstName}",
            consumes = { MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
            produces = { MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
    @Operation(summary = "Finds Users By Usernames", description = "Finds Users By Usernames",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = Users.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PagedModel<EntityModel<Users>>> findUsersByUsernames(
            @PathVariable(value = "firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "username"));

        return ResponseEntity.ok(usersServices.findUsersByUsernames(firstName, pageable));
    }

    @GetMapping(value = "/id/{id}",
            consumes = { MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
            produces = { MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
    @Operation(summary = "Finds Users By Id", description = "Finds Users By Id",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = Users.class))
                                    )
                            }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public Users findUsersById(@PathVariable(value = "id") Long id) {
        return usersServices.findUserById(id);
    }

    @PostMapping(value = "/create",
            consumes = { MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
            produces = { MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
    @Operation(summary = "Adds a new User",
            description = "Adds a new User by passing in a JSON, representation of the user!",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Users.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @ResponseStatus(CREATED)
    public Map<String, String> create(@Valid @RequestBody Users user) throws MessagingException, UnsupportedEncodingException {

        Map<String, String> response = new HashMap<>();

        try {
            Users createUser = usersServices.create(user);

            if (createUser != null) {
                emailService.sendMailCreate(createUser);
                response.put("message", "Usuário criado com sucesso!");
            } else {
                response.put("message", "Falha ao criar usuário.");
            }
        } catch (Exception e) {
            response.put("message", "Erro ao criar usuário: " + e.getMessage());
            throw new MessagingException("Não foi possível enviar o email!", e);
        }
        return response;
    }

    @PutMapping(value = "/update",
            consumes = { MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
            produces = { MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
    @Operation(summary = "Updates a User",
            description = "Updates a User by passing in a JSON, representation of the user!",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Users.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public Map<String, String> update(@Valid @RequestBody Users user) throws MessagingException, UnsupportedEncodingException {

        Map<String, String> response = new HashMap<>();

        try {
            Users updatedUser = usersServices.updateUser(user);

            if (updatedUser != null) {
                emailService.sendMailUpdate(updatedUser);
                response.put("message", "Usuário atualizado com sucesso!");
            } else {
                response.put("message", "Falha ao atualizar o usuário.");
            }
        } catch (Exception e) {
            response.put("message", "Erro ao atualizar o usuário: " + e.getMessage());
            throw new MessagingException("Não foi possível enviar o email!", e);
        }
        return response;
    }

    @DeleteMapping(value = "/delete/{id}",
            consumes = { MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
            produces = { MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
    @Operation(summary = "Deletes a User",
            description = "Deletes a User by passing in a JSON, representation of the user!",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @ResponseStatus(value = NO_CONTENT)
    public void delete(@PathVariable(value = "id") Long id) {
        usersServices.deleteUser(id);
    }
}