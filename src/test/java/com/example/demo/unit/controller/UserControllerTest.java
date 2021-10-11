package com.example.demo.unit.controller;

import com.example.demo.controller.UserController;
import com.example.demo.entity.User;
import com.example.demo.facade.impl.UserFacade;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;
import java.util.Optional;

import static com.example.demo.dto.UserProto.UserDto;
import static com.example.demo.dto.UserProto.UserSimpleDto;
import static com.example.demo.dto.UserProto.UserSimpleDtoList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@WithMockUser(authorities = {"ADMIN"})
public class UserControllerTest {
    @MockBean
    private UserFacade userFacade;

    @Autowired
    private UserController userController;

    @Test
    public void getAllUsers_ShouldReturnUsers() {
        when(userFacade.findAll()).thenReturn(UserSimpleDtoList.getDefaultInstance());
        ResponseEntity<UserSimpleDtoList> responseEntity = userController.findAllUser();
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isInitialized());
    }

    @Test
    public void getUserById_ShouldReturnUserById() {
        Long id = 1L;
        when(userFacade.findById(eq(id))).thenReturn(Optional.ofNullable(UserDto.getDefaultInstance()));
        ResponseEntity<UserDto> responseEntity = userController.findUserById(id);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isInitialized());
    }

    @Test
    public void getById_ShouldReturnNotFound() {
        Long id = -1L;
        when(userFacade.findById(eq(id))).thenReturn(Optional.empty());
        ResponseEntity<UserDto> responseEntity = userController.findUserById(id);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void createUser_ShouldReturnCreated() {
        User user = mock(User.class);

        UserSimpleDto userDto = UserSimpleDto.getDefaultInstance();

        ResponseEntity<Void> responseEntity = userController.createUser(userDto, user);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Mockito.verify(userFacade, times(1)).save(userDto, user);
    }

    @Test
    public void updateUser_shouldReturnOk() {
        Long id = 1L;
        User user = mock(User.class);

        UserSimpleDto userDto = UserSimpleDto.getDefaultInstance();

        ResponseEntity<Void> responseEntity = userController.updateUser(id, userDto, user);
        Mockito.verify(userFacade, times(1)).update(id, userDto, user);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteUser_ShouldReturnOk() {
        Long id = 1L;
        User user = mock(User.class);
        ResponseEntity<Void> responseEntity = userController.deleteUser(id, user);
        Mockito.verify(userFacade, times(1)).deleteById(id, user);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
}