package com.example.demo.unit.service;

import com.example.demo.entity.User;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.user.UsernameReservedException;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.impl.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @Test
    public void findAllUsers_ShouldReturnUsers() {
        List<User> mockedList = mock(List.class);
        when(userRepo.findAll()).thenReturn(mockedList);
        assertEquals(userService.findAll(), mockedList);
    }

    @Test
    public void findUserById_ShouldReturnUser() {
        Long id = 1L;
        User user = mock(User.class);
        when(userRepo.findById(eq(id))).thenReturn(Optional.of(user));
        assertEquals(Optional.of(user), userService.findById(id));
    }

    @Test
    public void createUser_Ok() {
        User userMock = new User();
        userMock.setPassword("123");

        userService.save(userMock);
        verify(userRepo, times(1)).save(eq(userMock));
    }

    @Test
    public void createUserWithExistUsername_ShouldReturnUsernameReservedException() {
        User userMock = new User();
        userMock.setPassword("123");
        when(userRepo.existsByUsername(userMock.getUsername())).thenReturn(true);

        assertThrows(UsernameReservedException.class, () -> userService.save(userMock));
    }

    @Test
    public void updateUser_Ok() {
        Long id = 1L;
        User user = mock(User.class);

        when(user.getId()).thenReturn(id);
        when(user.getUsername()).thenReturn("str");
        when(userRepo.findById(eq(id))).thenReturn(Optional.of(user));

        userService.update(id, user);
        verify(userRepo, times(1)).save(eq(user));
    }

    @Test
    public void updateNonExistUser_ShouldReturnUserNotFoundException() {
        Long id = 1L;
        User user = mock(User.class);
        when(userRepo.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(id, user));
    }

    @Test
    public void updateUserWithExistUsername_ShouldReturnUsernameReservedException() {
        Long id = 1L;
        User user = mock(User.class);
        User userDb = mock(User.class);

        when(user.getId()).thenReturn(id);
        when(user.getUsername()).thenReturn("str");
        when(userDb.getUsername()).thenReturn("str1");
        when(userRepo.findById(eq(id))).thenReturn(Optional.of(userDb));
        when(userRepo.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(UsernameReservedException.class, () -> userService.update(id, user));
    }

    @Test
    public void updateUserWithConflictId_ShouldReturnBadRequestParametersException() {
        Long id = 1L;
        User user = mock(User.class);
        when(user.getId()).thenReturn(id);
        when(userRepo.findById(eq(id))).thenReturn(Optional.of(user));

        assertThrows(BadRequestParametersException.class, () -> userService.update(2L, user));
    }

    @Test
    public void deleteUser_Ok() {
        Long id = 1L;
        when(userRepo.existsById(eq(id))).thenReturn(true);
        userService.deleteById(id);
        verify(userRepo, times(1)).deleteById(eq(id));
    }

    @Test
    public void deleteNonExistUser_ShouldReturnUserNotFoundException() {
        Long id = 1L;
        when(userRepo.findById(eq(id))).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteById(id));
    }
}
