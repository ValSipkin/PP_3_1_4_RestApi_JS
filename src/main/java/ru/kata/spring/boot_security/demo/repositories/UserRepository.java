package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select user from User user left join fetch user.roles where user.username = :username")
    User findByUsername(@Param("username") String username);
    @Query(value = "select user from User user left join fetch user.roles where user.id = :id")
    User getById(@Param("id") int id);
    List<User> findAll();
}
