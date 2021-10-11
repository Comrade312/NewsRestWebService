package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.facade.UserCrudFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.dto.UserProto.UserDto;
import static com.example.demo.dto.UserProto.UserSimpleDto;
import static com.example.demo.dto.UserProto.UserSimpleDtoList;

/**
 * Rest controller for operations with {@link User}
 */
@RestController
@RequestMapping(value = "/api/user")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class UserController {
    private final UserCrudFacade userFacade;

    @Autowired
    public UserController(UserCrudFacade userFacade) {
        this.userFacade = userFacade;
    }

    /**
     * Method which shows all available {@link User}
     *
     * @return {@link UserSimpleDtoList} of {@link UserSimpleDto}
     */
    @GetMapping
    public ResponseEntity<UserSimpleDtoList> findAllUser() {
        return new ResponseEntity<>(userFacade.findAll(), HttpStatus.OK);
    }

    /**
     * Method which shows {@link User} with specified id
     *
     * @param id {@link User} objects id for search
     * @return {@link UserDto} object or Http Code NOT_FOUND(404)
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id) {
        return userFacade.findById(id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    /**
     * Method which creates new {@link User}
     *
     * @param userNew {@link UserSimpleDto} dto object which needs to created
     * @param user    {@link User} that call method
     */
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserSimpleDto userNew,
                                           @AuthenticationPrincipal User user) {
        userFacade.save(userNew, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Method which updates {@link User}
     *
     * @param id      {@link User} from request url
     * @param userDto {@link UserSimpleDto} object dto to update
     * @param user    {@link User} that call method
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id,
                                           @RequestBody UserSimpleDto userDto,
                                           @AuthenticationPrincipal User user) {
        userFacade.update(id, userDto, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Method which deletes {@link User} object by id
     *
     * @param id   {@link User} object to delete
     * @param user {@link User} that call method
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,
                                           @AuthenticationPrincipal User user) {
        userFacade.deleteById(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
