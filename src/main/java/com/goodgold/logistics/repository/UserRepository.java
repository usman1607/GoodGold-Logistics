package com.goodgold.logistics.repository;

import com.goodgold.logistics.model.Role;
import com.goodgold.logistics.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByUsername(String username);
    //List<User> findUsersByRolesContaining(String role);
    List<User> findUsersByTitleEquals(String title);
    boolean   existsByUsername(String username);
    User findUserByUsername(String username);
    User findUserById(long id);
}
