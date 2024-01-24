package com.hivebuddyteam.hivebuddyapplication.repository;

import com.hivebuddyteam.hivebuddyapplication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByUsername(String name);
}
