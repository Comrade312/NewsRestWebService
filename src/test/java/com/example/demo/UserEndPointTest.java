package com.example.demo;

import com.example.demo.dto.UserProto;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.user.UsernameReservedException;
import com.example.demo.facade.impl.UserFacade;
import com.example.demo.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.example.demo.dto.UserProto.UserDto;
import static com.example.demo.dto.UserProto.UserSimpleDto;
import static com.example.demo.dto.UserProto.UserSimpleDtoList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/sql/user_list_before.sql", "/sql/news_list_before.sql", "/sql/comment_list_before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/comment_list_after.sql", "/sql/news_list_after.sql", "/sql/user_list_after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
@WithUserDetails("admin")
public class UserEndPointTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserRepo userRepo;

    @Test
    public void getUsersList_ShouldReturnUsers() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/user"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf;charset=UTF-8"))
                .andReturn();

        assertEquals(userFacade.findAll(),
                UserSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()));
    }


    @Test
    public void getUserById_ShouldReturnUserWithId() throws Exception {
        UserDto userDto = userFacade.findById(1L).orElse(UserDto.getDefaultInstance());

        MvcResult mvcResult = mockMvc.perform(get("/api/user/1"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf;charset=UTF-8"))
                .andReturn();

        assertEquals(UserDto.parseFrom(mvcResult.getResponse().getContentAsByteArray()), userDto);
    }

    @Test
    public void getUserByNonExistId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/user/-1"))
                .andExpect(authenticated())
                .andExpect(status().isNotFound());
    }

    @Test
    public void createUser_ShouldReturnCreated() throws Exception {
        assertFalse(userRepo.existsByUsername("testusername"));

        UserProto.UserSimpleDto userDto = UserProto.UserSimpleDto.newBuilder()
                .setUsername("testusername")
                .setPassword("123")
                .setActive(true)
                .addRoles(UserProto.Role.ADMIN)
                .build();

        mockMvc.perform(post("/api/user/")
                .contentType("application/x-protobuf;charset=UTF-8").content(userDto.toByteArray()))
                .andExpect(authenticated())
                .andExpect(status().isCreated());

        assertTrue(userRepo.existsByUsername("testusername"));
    }

    @Test
    public void createUserWithExistUsername_ShouldReturnUsernameReservedException() throws Exception {
        UserSimpleDto userDto = UserSimpleDto.newBuilder()
                .setUsername("admin")
                .setPassword("123")
                .setActive(true)
                .addRoles(UserProto.Role.ADMIN)
                .build();

        MockHttpServletRequestBuilder requestBuilder = post("/api/user/")
                .contentType("application/x-protobuf;charset=UTF-8")
                .content(userDto.toByteArray());

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameReservedException));
    }

    @Test
    public void updateUser_ShouldReturnOk() throws Exception {
        UserSimpleDto userDto = UserSimpleDto.newBuilder()
                .setId(3L)
                .setUsername("adminForEditNew")
                .setPassword("123")
                .setActive(true)
                .addRoles(UserProto.Role.ADMIN)
                .build();

        MockHttpServletRequestBuilder requestBuilder = put("/api/user/3")
                .contentType("application/x-protobuf;charset=UTF-8")
                .content(userDto.toByteArray());

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andExpect(status().isOk());

        assertTrue(userRepo.existsByUsername("adminForEditNew"));
        assertFalse(userRepo.existsByUsername("adminForEdit"));
    }

    @Test
    public void updateUserWithConflictId_ShouldReturnBadRequestParametersException() throws Exception {
        UserSimpleDto userDto = UserSimpleDto.newBuilder()
                .setId(3L)
                .setUsername("adminForEditNew")
                .setPassword("123")
                .setActive(true)
                .addRoles(UserProto.Role.ADMIN)
                .build();

        MockHttpServletRequestBuilder requestBuilder = put("/api/user/2")
                .contentType("application/x-protobuf;charset=UTF-8")
                .content(userDto.toByteArray());

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestParametersException));
    }

    @Test
    public void updateUserWithExistUsername_ShouldReturnUsernameReservedException() throws Exception {
        UserSimpleDto userDto = UserSimpleDto.newBuilder()
                .setId(3L)
                .setUsername("admin")
                .setPassword("123")
                .setActive(true)
                .addRoles(UserProto.Role.ADMIN)
                .build();

        MockHttpServletRequestBuilder requestBuilder = put("/api/user/3")
                .contentType("application/x-protobuf;charset=UTF-8")
                .content(userDto.toByteArray());

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameReservedException));
    }

    @Test
    public void deleteUser_ShouldReturnOk() throws Exception {
        assertTrue(userRepo.existsByUsername("adminForDelete"));

        mockMvc.perform(delete("/api/user/2"))
                .andExpect(authenticated())
                .andExpect(status().isOk());

        assertFalse(userRepo.existsByUsername("adminForDelete"));
    }

    @Test
    public void deleteUser_ShouldReturnO1k() throws Exception {
        assertTrue(userRepo.existsByUsername("sub1"));

        mockMvc.perform(delete("/api/user/6"))
                .andExpect(authenticated())
                .andExpect(status().isOk());

        assertFalse(userRepo.existsByUsername("sub1"));
    }

    @Test
    public void deleteNonExistUser_ShouldReturnUserNotFoundException() throws Exception {
        mockMvc.perform(delete("/api/user/-1"))
                .andExpect(authenticated())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));
    }
}
