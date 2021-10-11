package com.example.demo.unit.controller;

import com.example.demo.controller.CommentController;
import com.example.demo.entity.User;
import com.example.demo.facade.impl.CommentFacade;
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

import static com.example.demo.dto.CommentProto.CommentDto;
import static com.example.demo.dto.CommentProto.CommentSimpleDto;
import static com.example.demo.dto.CommentProto.CommentSimpleDtoList;
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
public class CommentControllerTest {
    @MockBean
    private CommentFacade commentFacade;

    @Autowired
    private CommentController commentController;

    @Test
    public void getAllComment_ShouldReturnComment() {
        when(commentFacade.findAll()).thenReturn(CommentSimpleDtoList.getDefaultInstance());
        ResponseEntity<CommentSimpleDtoList> responseEntity = commentController.findAllComment();
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isInitialized());
    }

    @Test
    public void getCommentById_ShouldReturnCommentById() {
        Long id = 1L;
        when(commentFacade.findById(eq(id))).thenReturn(Optional.ofNullable(CommentDto.getDefaultInstance()));
        ResponseEntity<CommentDto> responseEntity = commentController.findCommentById(id);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isInitialized());
    }

    @Test
    public void getCommentByText_ShouldReturnCommentBy() {
        String text = "str";
        when(commentFacade.findByText(eq(text))).thenReturn(CommentSimpleDtoList.getDefaultInstance());
        ResponseEntity<CommentSimpleDtoList> responseEntity = commentController.findCommentByText(text);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isInitialized());
    }

    @Test
    public void getCommentByTextContains_ShouldReturnComment() {
        String text = "str";
        when(commentFacade.findByTextContains(eq(text))).thenReturn(CommentSimpleDtoList.getDefaultInstance());
        ResponseEntity<CommentSimpleDtoList> responseEntity = commentController.findCommentByTextLike(text);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isInitialized());
    }

    @Test
    public void getById_ShouldReturnNotFound() {
        Long id = -1L;
        when(commentFacade.findById(eq(id))).thenReturn(Optional.empty());
        ResponseEntity<CommentDto> responseEntity = commentController.findCommentById(id);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void createComment_ShouldReturnCreated() {
        User user = mock(User.class);

        CommentSimpleDto commentDto = CommentSimpleDto.getDefaultInstance();

        ResponseEntity<Void> responseEntity = commentController.createComment(commentDto, user);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Mockito.verify(commentFacade, times(1)).save(commentDto, user);
    }

    @Test
    public void updateComment_shouldReturnOk() {
        Long id = 1L;
        User user = mock(User.class);

        CommentSimpleDto commentDto = CommentSimpleDto.getDefaultInstance();

        ResponseEntity<Void> responseEntity = commentController.updateComment(id, commentDto, user);
        Mockito.verify(commentFacade, times(1)).update(id, commentDto, user);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteComment_ShouldReturnOk() {
        Long id = 1L;
        User user = mock(User.class);
        ResponseEntity<Void> responseEntity = commentController.deleteComment(id, user);
        Mockito.verify(commentFacade, times(1)).deleteById(id, user);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
}