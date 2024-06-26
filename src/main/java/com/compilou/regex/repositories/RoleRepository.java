package com.compilou.regex.repositories;

import com.compilou.regex.models.Role;
import com.compilou.regex.models.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
    Role findByName(String name);
}