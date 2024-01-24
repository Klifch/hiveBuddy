package com.hivebuddyteam.hivebuddyapplication.repository;

import com.hivebuddyteam.hivebuddyapplication.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByName(String name);


}
