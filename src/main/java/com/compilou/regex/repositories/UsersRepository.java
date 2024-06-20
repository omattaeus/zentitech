package com.compilou.regex.repositories;

import com.compilou.regex.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByCpfCnpj(String CpfCnpj);
    @Query("SELECT u FROM Users u ORDER BY u.id ASC")
    Page<Users> findAllUsers(Pageable page);
    @Query("SELECT p FROM Users p WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :firstName, '%')) ORDER BY p.id ASC")
    Page<Users> findUsersByUsernames(@Param("firstName") String firstName, Pageable pageable);
}
