package com.example.demo;

import com.example.demo.exception.user.UsernameReservedException;
import com.example.demo.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.example.demo.dto.RegistrationRequestProto.RegistrationRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/sql/user_list_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/user_list_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class AuthEndPointTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Test
    public void registerUser_ShouldReturnCreated() throws Exception {
        RegistrationRequestDto registrationRequest = RegistrationRequestDto.newBuilder()
                .setUsername("testuser")
                .setPassword("123")
                .build();

        MockHttpServletRequestBuilder requestBuilder = post("/api/auth/register")
                .contentType("application/x-protobuf;charset=UTF-8")
                .content(registrationRequest.toByteArray());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());

        assertTrue(userRepo.existsByUsername("testuser"));
        assertEquals(8, userRepo.findAll().size());
    }

    @Test
    public void registerUserWithExistUsername_ShouldReturnUsernameReservedException() throws Exception {
        RegistrationRequestDto registrationRequest = RegistrationRequestDto.newBuilder()
                .setUsername("admin")
                .setPassword("123")
                .build();

        MockHttpServletRequestBuilder requestBuilder = post("/api/auth/register")
                .contentType("application/x-protobuf;charset=UTF-8")
                .content(registrationRequest.toByteArray());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameReservedException));
    }

}
