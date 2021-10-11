package com.example.demo;

import com.example.demo.exception.comment.CommentNotFoundException;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.facade.impl.CommentFacade;
import com.example.demo.repo.CommentRepo;
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

import static com.example.demo.dto.CommentProto.CommentDto;
import static com.example.demo.dto.CommentProto.CommentSimpleDto;
import static com.example.demo.dto.CommentProto.CommentSimpleDtoList;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class CommentEndPointTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentFacade commentFacade;

    @Autowired
    private CommentRepo commentRepo;

    @Test
    public void getCommentList_ShouldReturnComment() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/comment"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf;charset=UTF-8"))
                .andReturn();

        assertEquals(CommentSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()), commentFacade.findAll());
    }


    @Test
    public void getCommentById_ShouldReturnCommentWithId() throws Exception {
        CommentDto commentDto = commentFacade.findById(1L).orElse(CommentDto.getDefaultInstance());

        MvcResult mvcResult = mockMvc.perform(get("/api/comment/1"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf;charset=UTF-8"))
                .andReturn();

        assertEquals(CommentDto.parseFrom(mvcResult.getResponse().getContentAsByteArray()), commentDto);
    }

    @Test
    public void getCommentsByText_ShouldReturnCommentWithText() throws Exception {
        CommentSimpleDtoList commentList = commentFacade.findByText("Puppies6 are threat");

        MvcResult mvcResult = mockMvc.perform(get("/api/comment?text=Puppies6 are threat"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf;charset=UTF-8"))
                .andReturn();

        assertEquals(2, CommentSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()).getCommentDtoCount());
        assertEquals(commentList, CommentSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()));
    }

    @Test
    public void getCommentsByTextLike_ShouldReturnCommentWithTextContains() throws Exception {
        CommentSimpleDtoList commentList = commentFacade.findByTextContains("Puppies");

        MvcResult mvcResult = mockMvc.perform(get("/api/comment?textLike=Puppies"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/x-protobuf;charset=UTF-8"))
                .andReturn();

        assertEquals(7, CommentSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()).getCommentDtoCount());
        assertEquals(commentList, CommentSimpleDtoList.parseFrom(mvcResult.getResponse().getContentAsByteArray()));
    }

    @Test
    public void getCommentByNonExistId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/comment/-1"))
                .andExpect(authenticated())
                .andExpect(status().isNotFound());
    }

    @Test
    public void createComment_ShouldReturnCreated() throws Exception {
        assertTrue(commentRepo.findByText("testcommentname").isEmpty());

        CommentSimpleDto commentDto = CommentSimpleDto.newBuilder()
                .setDate("2000-04-22")
                .setText("testcommentname")
                .setUserId(4L)
                .setNewsId(1L)
                .build();

        MockHttpServletRequestBuilder requestBuilder = post("/api/comment/")
                .contentType("application/x-protobuf;charset=UTF-8")
                .content(commentDto.toByteArray());

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andExpect(status().isCreated());

        assertEquals(1, commentRepo.findByText("testcommentname").size());
    }

    @Test
    public void updateComment_ShouldReturnOk() throws Exception {
        CommentSimpleDto commentDto = CommentSimpleDto.newBuilder()
                .setId(3L)
                .setDate("2000-04-22")
                .setText("Text for edit new")
                .setUserId(4L)
                .build();

        MockHttpServletRequestBuilder requestBuilder = put("/api/comment/3")
                .contentType("application/x-protobuf;charset=UTF-8")
                .content(commentDto.toByteArray());

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andExpect(status().isOk());

        assertEquals(1, commentRepo.findByText("Text for edit new").size());
        assertTrue(commentRepo.findByText("Text for edit").isEmpty());
    }

    @Test
    public void updateCommentWithConflictId_ShouldReturnBadRequestParametersException() throws Exception {
        CommentSimpleDto commentDto = CommentSimpleDto.newBuilder()
                .setId(3L)
                .setDate("2000-04-22")
                .setText("Title for edit new")
                .setUserId(4L)
                .build();

        MockHttpServletRequestBuilder requestBuilder = put("/api/comment/2")
                .contentType("application/x-protobuf;charset=UTF-8")
                .content(commentDto.toByteArray());

        mockMvc.perform(requestBuilder)
                .andExpect(authenticated())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestParametersException));
    }

    @Test
    public void deleteComment_ShouldReturnOk() throws Exception {
        assertEquals(1, commentRepo.findByText("Text for delete").size());

        mockMvc.perform(delete("/api/comment/6"))
                .andExpect(authenticated())
                .andExpect(status().isOk());

        assertTrue(commentRepo.findByText("Text for delete").isEmpty());
    }

    @Test
    public void deleteNonExistComment_ShouldReturnCommentNotFoundException() throws Exception {
        mockMvc.perform(delete("/api/comment/-1"))
                .andExpect(authenticated())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CommentNotFoundException));
    }

}
