package com.example.demo.facade;

import com.example.demo.entity.User;
import com.example.demo.exception.request.NotEnoughRightsException;

import java.util.Optional;

import static com.example.demo.dto.UserProto.UserDto;
import static com.example.demo.dto.UserProto.UserSimpleDto;
import static com.example.demo.dto.UserProto.UserSimpleDtoList;

/**
 * Interface for CRUD operations with {@link User} in facade layer
 */
public interface UserCrudFacade {
    /**
     * Method which returns all available {@link User}
     * and convert it into {@link UserSimpleDto}
     *
     * @return {@link UserSimpleDtoList} of {@link UserSimpleDto}
     */
    UserSimpleDtoList findAll();

    /**
     * Method which returns {@link User} with specified id
     * and convert it into {@link UserDto}
     *
     * @param id {@link User} objects id for search
     * @return {@link UserDto} object wrap into {@link Optional}
     */
    Optional<UserDto> findById(Long id);

    /**
     * Method which creates new {@link User}.
     * Converts {@link UserSimpleDto} to {@link User} and then save it
     *
     * @param userDto {@link UserSimpleDto} dto object which needs to created
     * @param user    {@link User} that call method
     */
    void save(UserSimpleDto userDto, User user);

    /**
     * Method which updates {@link User}.
     * Converts {@link UserSimpleDto} to {@link User} and then update it
     *
     * @param id      {@link User} from request url
     * @param userDto {@link UserSimpleDto} object dto to update
     * @param user    {@link User} that call method
     * @throws NotEnoughRightsException if user dont have right to update news
     */
    void update(Long id, UserSimpleDto userDto, User user);

    /**
     * Method which deletes {@link User} object by id
     *
     * @param id   {@link User} object to delete
     * @param user {@link User} that call method
     * @throws NotEnoughRightsException if user dont have right to delete user
     */
    void deleteById(Long id, User user);
}
