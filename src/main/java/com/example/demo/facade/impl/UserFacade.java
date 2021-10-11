package com.example.demo.facade.impl;


import com.example.demo.dto.UserProto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.request.NotEnoughRightsException;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.facade.UserCrudFacade;
import com.example.demo.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.dto.UserProto.UserComment;
import static com.example.demo.dto.UserProto.UserDto;
import static com.example.demo.dto.UserProto.UserNews;
import static com.example.demo.dto.UserProto.UserSimpleDto;
import static com.example.demo.dto.UserProto.UserSimpleDtoList;

/**
 * CRUD operation in the {@link User} Facade layer
 * Calls a method from {@link UserService}
 */
@Service
public class UserFacade implements UserCrudFacade {
    private final UserService userService;

    @Autowired
    public UserFacade(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserSimpleDtoList findAll() {
        return UserSimpleDtoList.newBuilder()
                .addAllUserDto(userService.findAll().stream()
                        .map(value -> UserSimpleDto.newBuilder()
                                .setId(value.getId())
                                .setUsername(value.getUsername())
                                .setPassword(value.getPassword())
                                .setActive(value.isActive())
                                .addAllRoles(value.getRoles().stream()
                                        .map(v -> UserProto.Role.valueOf(v.getAuthority()))
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userService.findById(id)
                .map(value -> UserDto.newBuilder()
                        .setId(value.getId())
                        .setUsername(value.getUsername())
                        .setPassword(value.getPassword())
                        .setActive(value.isActive())
                        .addAllRoles(value.getRoles().stream()
                                .map(v -> UserProto.Role.valueOf(v.getAuthority()))
                                .collect(Collectors.toList()))
                        .addAllNews(value.getNews().stream()
                                .map(v -> UserNews.newBuilder()
                                        .setId(v.getId())
                                        .setDate(v.getDate().toString())
                                        .setTitle(v.getTitle())
                                        .setText(v.getText())
                                        .build()
                                ).collect(Collectors.toList()))
                        .addAllComments(value.getComments().stream()
                                .map(v -> UserComment.newBuilder()
                                        .setId(v.getId())
                                        .setDate(v.getDate().toString())
                                        .setText(v.getText())
                                        .build()
                                ).collect(Collectors.toList()))
                        .build()
                );
    }

    @Override
    public void save(UserSimpleDto userDto, User user) {
        User userNew = new User();
        userNew.setUsername(userDto.getUsername());
        userNew.setPassword(userDto.getPassword());
        userNew.setActive(true);
        userNew.setRoles(userDto.getRolesList().stream()
                .map(value -> Role.valueOf(value.name()))
                .collect(Collectors.toSet()));

        if (user.getAuthorities().contains(Role.ADMIN)) {
            userService.save(userNew);
        } else {
            throw new NotEnoughRightsException("Only admins can create users");
        }
    }

    @Override
    public void update(Long id, UserSimpleDto userDto, User user) {
        if (userDto.getId() == user.getId() || user.getAuthorities().contains(Role.ADMIN)) {
            User userFromDto = new User();
            userFromDto.setId(userDto.getId());
            userFromDto.setUsername(userDto.getUsername());
            userFromDto.setPassword(userDto.getPassword());
            userFromDto.setActive(userDto.getActive());

            userFromDto.setRoles(userDto.getRolesList().stream()
                    .map(value -> Role.valueOf(value.name()))
                    .collect(Collectors.toSet()));

            userService.update(id, userFromDto);
        } else {
            throw new NotEnoughRightsException("User with id " + user.getId()
                    + " cannot update user with id " + userDto.getId());
        }
    }

    @Override
    public void deleteById(Long id, User user) {
        User userDb = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userDb.getId().equals(user.getId()) || user.getAuthorities().contains(Role.ADMIN)) {
            userService.deleteById(id);
        } else {
            throw new NotEnoughRightsException("User with id " + user.getId()
                    + " cannot delete user with id " + userDb.getId());
        }
    }
}