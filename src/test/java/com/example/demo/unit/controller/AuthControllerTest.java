package com.example.demo.unit.controller;

import com.example.demo.controller.AuthController;
import com.example.demo.facade.AuthFacade;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static com.example.demo.dto.RegistrationRequestProto.RegistrationRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerTest {
    @MockBean
    private AuthFacade authFacade;

    @Autowired
    private AuthController authController;

    @Test
    public void registerUser_ShouldReturnCreated() {
        RegistrationRequestDto registrationRequest = RegistrationRequestDto.getDefaultInstance();

        ResponseEntity<Void> responseEntity = authController.registration(registrationRequest);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Mockito.verify(authFacade, times(1)).registration(registrationRequest);
    }
}