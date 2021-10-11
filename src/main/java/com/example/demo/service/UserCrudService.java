package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.exception.user.UserNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Interface for CRUD operations with {@link User}
 */
public interface UserCrudService {
    /**
     * Find {@link User} object by id
     *
     * @param id {@link User} object to find object by id.
     * @return {@link User} object wrapped into {@link Optional}
     */
    Optional<User> findById(Long id);

    /**
     * Find all {@link User} objects
     *
     * @return list of {@link User} objects.
     */
    List<User> findAll();

    /**
     * Save {@link User} object to save
     *
     * @param user {@link User} object to save
     */
    void save(User user);

    /**
     * Update {@link User} object
     *
     * @param id   {@link User} from request url
     * @param user {@link User} object to update
     * @throws BadRequestParametersException when id from url not equal with id from {@link User} object
     * @throws UserNotFoundException         when there is no {@link User} object in database with provided id
     */
    void update(Long id, User user);

    /**
     * Delete {@link User} object by id
     *
     * @param id {@link User} object to delete
     * @throws UserNotFoundException when there is no {@link User} object in database with provided id
     */
    void deleteById(Long id);
}
