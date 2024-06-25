package com.compilou.regex.services;

import com.compilou.regex.controllers.UsersController;
import com.compilou.regex.exceptions.CustomDataIntegrityViolationException;
import com.compilou.regex.exceptions.ResourceNotFoundException;
import com.compilou.regex.mapper.DozerMapper;
import com.compilou.regex.models.Users;
import com.compilou.regex.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersServices {

    private final UsersRepository usersRepository;
    private final PagedResourcesAssembler<Users> assembler;

    public UsersServices(UsersRepository usersRepository, PagedResourcesAssembler<Users> assembler) {
        this.usersRepository = usersRepository;
        this.assembler = assembler;
    }

    public boolean existsByEmail(String email) {
        return usersRepository.existsByEmail(email);
    }

    public PagedModel<EntityModel<Users>> findAllUsers(Pageable pageable) {

        var usersPage = usersRepository.findAllUsers(pageable);
        var usersPageVo = usersPage.map(p -> DozerMapper.parseObject(p, Users.class));

        usersPageVo.map(
                p -> p.add(linkTo(methodOn(UsersController.class)
                        .findUsersById(p.getId())).withSelfRel()));

        Link link = linkTo(methodOn(UsersController.class)
                .findAllUsers(pageable.getPageNumber(), pageable.getPageSize(), "asc"))
                .withSelfRel();

        return assembler.toModel(usersPageVo, link);
    }

    public Page<Users> findAll(Pageable pageable) {
        return usersRepository.findAll(pageable);
    }

    public Page<Users> findUsersByUsernames(String firstname, Pageable pageable) {
        Page<Users> usersPage = usersRepository.findUsersByUsernames(firstname, pageable);

        return usersPage.map(user -> {
            Users userVo = DozerMapper.parseObject(user, Users.class);
            userVo.add(linkTo(methodOn(UsersController.class).findUsersById(userVo.getId())).withSelfRel());
            return userVo;
        });
    }
    public Users findUserById(Long id) {
        var entity = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));

        var users = DozerMapper.parseObject(entity, Users.class);
        users.add(linkTo(methodOn(UsersController.class).findUsersById(id)).withSelfRel());

        return users;
    }

    public Users create(Users user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null!");

        try {
            var entity = DozerMapper.parseObject(user, Users.class);
            var users =  DozerMapper.parseObject(usersRepository.save(entity), Users.class);
            users.add(linkTo(methodOn(UsersController.class).findUsersById(users.getId())).withSelfRel());

            return users;
        } catch (DataIntegrityViolationException e) {
            throw new CustomDataIntegrityViolationException("Duplicated username or email!");
        }
    }

    @Transactional
    public Users createHtml(Users user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null!");
        try {
            return usersRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new CustomDataIntegrityViolationException("Duplicated username or email!");
        }
    }

    @Transactional
    public Users updateUser(Users user) {
        Users existingUser = usersRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        if (!existingUser.getUsername().equals(user.getUsername())) {
            Optional<Users> userByUsername = usersRepository.findByUsername(user.getUsername());
            if (userByUsername.isPresent()) {
                throw new CustomDataIntegrityViolationException("Duplicated username!");
            }
        }

        if (!existingUser.getEmail().equals(user.getEmail())) {
            Optional<Users> userByEmail = usersRepository.findByEmail(user.getEmail());
            if (userByEmail.isPresent()) {
                throw new CustomDataIntegrityViolationException("Duplicated email!");
            }
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setCellphone(user.getCellphone());

        var users = DozerMapper.parseObject(usersRepository.save(existingUser), Users.class);
        users.add(linkTo(methodOn(UsersController.class).findUsersById(users.getId())).withSelfRel());

        return users;
    }

    public void deleteUser(Long id) {
        var entity = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        usersRepository.delete(entity);
    }
}