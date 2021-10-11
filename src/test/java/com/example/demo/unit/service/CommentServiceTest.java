package com.example.demo.unit.service;

import com.example.demo.entity.Comment;
import com.example.demo.exception.comment.CommentNotFoundException;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.repo.CommentRepo;
import com.example.demo.service.impl.CommentService;
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
public class CommentServiceTest {
    @Autowired
    private CommentService commentComment;

    @MockBean
    private CommentRepo commentRepo;

    @Test
    public void findAllComment_ShouldReturnComment() {
        List<Comment> mockedList = mock(List.class);
        when(commentRepo.findAll()).thenReturn(mockedList);
        assertEquals(commentComment.findAll(), mockedList);
    }

    @Test
    public void findCommentByText_ShouldReturnComment() {
        List<Comment> mockedList = mock(List.class);
        when(commentRepo.findByText("tmp")).thenReturn(mockedList);
        assertEquals(commentComment.findByText("tmp"), mockedList);
    }

    @Test
    public void findCommentByTextContains_ShouldReturnComment() {
        List<Comment> mockedList = mock(List.class);
        when(commentRepo.findByTextContains("tmp")).thenReturn(mockedList);
        assertEquals(commentComment.findByTextContains("tmp"), mockedList);
    }

    @Test
    public void findCommentById_ShouldReturnComment() {
        Long id = 1L;
        Comment comment = mock(Comment.class);
        when(commentRepo.findById(eq(id))).thenReturn(Optional.of(comment));
        assertEquals(Optional.of(comment), commentComment.findById(id));
    }

    @Test
    public void createComment_Ok() {
        Comment commentMock = new Comment();
        commentComment.save(commentMock);
        verify(commentRepo, times(1)).save(eq(commentMock));
    }

    @Test
    public void updateComment_Ok() {
        Long id = 1L;
        Comment comment = mock(Comment.class);

        when(comment.getId()).thenReturn(id);
        when(commentRepo.existsById(eq(id))).thenReturn(true);

        commentComment.update(id, comment);
        verify(commentRepo, times(1)).save(eq(comment));
    }

    @Test
    public void updateNonExistComment_ShouldReturnCommentNotFoundException() {
        Long id = 1L;
        Comment comment = mock(Comment.class);
        when(comment.getId()).thenReturn(id);
        when(commentRepo.existsById(comment.getId())).thenReturn(false);

        assertThrows(CommentNotFoundException.class, () -> commentComment.update(id, comment));
    }

    @Test
    public void updateCommentWithConflictId_ShouldReturnBadRequestParametersException() {
        Long id = 1L;
        Comment comment = mock(Comment.class);
        when(comment.getId()).thenReturn(id);
        when(commentRepo.findById(eq(id))).thenReturn(Optional.of(comment));

        assertThrows(BadRequestParametersException.class, () -> commentComment.update(2L, comment));
    }

    @Test
    public void deleteComment_Ok() {
        Long id = 1L;
        when(commentRepo.existsById(eq(id))).thenReturn(true);
        commentComment.deleteById(id);
        verify(commentRepo, times(1)).deleteById(eq(id));
    }

    @Test
    public void deleteNonExistComment_ShouldReturnCommentNotFoundException() {
        Long id = 1L;
        when(commentRepo.findById(eq(id))).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentComment.deleteById(id));
    }
}
