package com.compilou.regex.repositories;

import com.compilou.regex.models.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class UsersRepositoryTest {

    @Autowired
    private UsersRepository repository;

    private Users users;

    @BeforeEach
    void setup(){
        users = new Users(
                1L,
                "teste1234",
                "Teste",
                "teste@testando.com",
                "(12) 91334-1234"
        );
    }

    @DisplayName("Given Users Object When Save Then Return Saved Users")
    @Test
    void testGivenUsersObject_WhenSave_ThenReturnSavedUsers(){

        Users savedAnimais = repository.save(users);

        Assertions.assertNotNull(savedAnimais);
    }

    @DisplayName("Given Animais List When Find All Then Return Animals List")
    @Test
    void testGivenUsersList_WhenFindAll_ThenReturnUsersList(){

        Users users2 = new Users(
                2L,
                "testnado12",
                "Teste2",
                "teste2@testando.com",
                "(12) 95782-1234");

        repository.save(users);
        repository.save(users2);

        List<Users> usersList = repository.findAll();

        Assertions.assertNotNull(usersList);
        Assertions.assertEquals(2, usersList.size());
    }
}
