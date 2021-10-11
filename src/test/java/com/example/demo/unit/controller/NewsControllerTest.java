package com.example.demo.unit.controller;

import com.example.demo.controller.NewsController;
import com.example.demo.entity.User;
import com.example.demo.facade.impl.NewsFacade;
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

import static com.example.demo.dto.NewsProto.NewsDto;
import static com.example.demo.dto.NewsProto.NewsSimpleDto;
import static com.example.demo.dto.NewsProto.NewsSimpleDtoList;
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
public class NewsControllerTest {
    @MockBean
    private NewsFacade newsFacade;

    @Autowired
    private NewsController newsController;

    @Test
    public void getAllNews_ShouldReturnNews() {
        when(newsFacade.findAll()).thenReturn(NewsSimpleDtoList.getDefaultInstance());
        ResponseEntity<NewsSimpleDtoList> responseEntity = newsController.findAllNews();
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isInitialized());
    }

    @Test
    public void getNewsById_ShouldReturnNewsById() {
        Long id = 1L;
        when(newsFacade.findById(eq(id))).thenReturn(Optional.ofNullable(NewsDto.getDefaultInstance()));
        ResponseEntity<NewsDto> responseEntity = newsController.findNewsById(id);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isInitialized());
    }

    @Test
    public void getNewsByTitle_ShouldReturnNews() {
        String text = "str";
        when(newsFacade.findByTitle(eq(text))).thenReturn(NewsSimpleDtoList.getDefaultInstance());
        ResponseEntity<NewsSimpleDtoList> responseEntity = newsController.findNewsByTitle(text);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isInitialized());
    }

    @Test
    public void getNewsByTitleContains_ShouldReturnNews() {
        String text = "str";
        when(newsFacade.findByTitleContains(eq(text))).thenReturn(NewsSimpleDtoList.getDefaultInstance());
        ResponseEntity<NewsSimpleDtoList> responseEntity = newsController.findNewsByTitleLike(text);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isInitialized());
    }

    @Test
    public void getNewsByText_ShouldReturnNewsBy() {
        String text = "str";
        when(newsFacade.findByText(eq(text))).thenReturn(NewsSimpleDtoList.getDefaultInstance());
        ResponseEntity<NewsSimpleDtoList> responseEntity = newsController.findNewsByText(text);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isInitialized());
    }

    @Test
    public void getNewsByTextContains_ShouldReturnNews() {
        String text = "str";
        when(newsFacade.findByTextContains(eq(text))).thenReturn(NewsSimpleDtoList.getDefaultInstance());
        ResponseEntity<NewsSimpleDtoList> responseEntity = newsController.findNewsByTextLike(text);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isInitialized());
    }

    @Test
    public void getById_ShouldReturnNotFound() {
        Long id = -1L;
        when(newsFacade.findById(eq(id))).thenReturn(Optional.empty());
        ResponseEntity<NewsDto> responseEntity = newsController.findNewsById(id);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void createNews_ShouldReturnCreated() {
        User user = mock(User.class);

        NewsSimpleDto newsDto = NewsSimpleDto.getDefaultInstance();

        ResponseEntity<Void> responseEntity = newsController.createNews(newsDto, user);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Mockito.verify(newsFacade, times(1)).save(newsDto, user);
    }

    @Test
    public void updateNews_shouldReturnOk() {
        Long id = 1L;
        User user = mock(User.class);

        NewsSimpleDto newsDto = NewsSimpleDto.getDefaultInstance();

        ResponseEntity<Void> responseEntity = newsController.updateNews(id, newsDto, user);
        Mockito.verify(newsFacade, times(1)).update(id, newsDto, user);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteNews_ShouldReturnOk() {
        Long id = 1L;
        User user = mock(User.class);
        ResponseEntity<Void> responseEntity = newsController.deleteNews(id, user);
        Mockito.verify(newsFacade, times(1)).deleteById(id, user);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
}