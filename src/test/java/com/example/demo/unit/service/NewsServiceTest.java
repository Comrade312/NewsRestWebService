package com.example.demo.unit.service;

import com.example.demo.entity.News;
import com.example.demo.exception.news.NewsNotFoundException;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.repo.NewsRepo;
import com.example.demo.service.NewsService;
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
public class NewsServiceTest {
    @Autowired
    private NewsService newsNews;

    @MockBean
    private NewsRepo newsRepo;

    @Test
    public void findAllNews_ShouldReturnNews() {
        List<News> mockedList = mock(List.class);
        when(newsRepo.findAll()).thenReturn(mockedList);
        assertEquals(newsNews.findAll(), mockedList);
    }

    @Test
    public void findNewsByTitle_ShouldReturnNews() {
        List<News> mockedList = mock(List.class);
        when(newsRepo.findByTitle("tmp")).thenReturn(mockedList);
        assertEquals(newsNews.findByTitle("tmp"), mockedList);
    }

    @Test
    public void findNewsByTitleContains_ShouldReturnNews() {
        List<News> mockedList = mock(List.class);
        when(newsRepo.findByTitleContains("tmp")).thenReturn(mockedList);
        assertEquals(newsNews.findByTitleContains("tmp"), mockedList);
    }

    @Test
    public void findNewsByText_ShouldReturnNews() {
        List<News> mockedList = mock(List.class);
        when(newsRepo.findByText("tmp")).thenReturn(mockedList);
        assertEquals(newsNews.findByText("tmp"), mockedList);
    }

    @Test
    public void findNewsByTextContains_ShouldReturnNews() {
        List<News> mockedList = mock(List.class);
        when(newsRepo.findByTextContains("tmp")).thenReturn(mockedList);
        assertEquals(newsNews.findByTextContains("tmp"), mockedList);
    }

    @Test
    public void findNewsById_ShouldReturnNews() {
        Long id = 1L;
        News news = mock(News.class);
        when(newsRepo.findById(eq(id))).thenReturn(Optional.of(news));
        assertEquals(Optional.of(news), newsNews.findById(id));
    }

    @Test
    public void createNews_Ok() {
        News newsMock = new News();
        newsNews.save(newsMock);
        verify(newsRepo, times(1)).save(eq(newsMock));
    }

    @Test
    public void updateNews_Ok() {
        Long id = 1L;
        News news = mock(News.class);

        when(news.getId()).thenReturn(id);
        when(newsRepo.existsById(eq(id))).thenReturn(true);

        newsNews.update(id, news);
        verify(newsRepo, times(1)).save(eq(news));
    }

    @Test
    public void updateNonExistNews_ShouldReturnNewsNotFoundException() {
        Long id = 1L;
        News news = mock(News.class);
        when(news.getId()).thenReturn(id);
        when(newsRepo.existsById(news.getId())).thenReturn(false);

        assertThrows(NewsNotFoundException.class, () -> newsNews.update(id, news));
    }

    @Test
    public void updateNewsWithConflictId_ShouldReturnBadRequestParametersException() {
        Long id = 1L;
        News news = mock(News.class);
        when(news.getId()).thenReturn(id);
        when(newsRepo.findById(eq(id))).thenReturn(Optional.of(news));

        assertThrows(BadRequestParametersException.class, () -> newsNews.update(2L, news));
    }

    @Test
    public void deleteNews_Ok() {
        Long id = 1L;
        when(newsRepo.existsById(eq(id))).thenReturn(true);
        newsNews.deleteById(id);
        verify(newsRepo, times(1)).deleteById(eq(id));
    }

    @Test
    public void deleteNonExistNews_ShouldReturnNewsNotFoundException() {
        Long id = 1L;
        when(newsRepo.findById(eq(id))).thenReturn(Optional.empty());
        assertThrows(NewsNotFoundException.class, () -> newsNews.deleteById(id));
    }
}
