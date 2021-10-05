package com.example.demo.repo;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data Access Object class.
 * Provides CRUD and other operations with {@link User} objects.
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    /**
     * Find {@link User} object by it's id in data storage
     *
     * @param username {@link User} object's username for search
     * @return found {@link User} wrapped in {@link Optional}.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if username is taken
     *
     * @param username string for search
     * @return true, if username is already taken
     */
    Boolean existsByUsername(String username);
}
