package com.example.demo.service.impl;

import com.example.demo.entity.News;
import com.example.demo.exception.news.NewsNotFoundException;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.repo.NewsRepo;
import com.example.demo.service.NewsCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Provides CRUD operation with {@link News}
 * Calls a method from {@link NewsRepo}
 */
@Service
public class NewsService implements NewsCrudService {
    private final NewsRepo newsRepo;

    @Autowired
    public NewsService(NewsRepo newsRepo) {
        this.newsRepo = newsRepo;
    }

    @Override
    public List<News> findAll() {
        return newsRepo.findAll();
    }

    @Override
    public List<News> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date"));
        return newsRepo.findAll(pageable).getContent();
    }

    @Override
    public Optional<News> findById(Long id) {
        return newsRepo.findById(id);
    }

    @Override
    public List<News> findByTitle(String title) {
        return newsRepo.findByTitle(title);
    }

    @Override
    public List<News> findByTitleContains(String title) {
        return newsRepo.findByTitleContains(title);
    }

    @Override
    public List<News> findByText(String text) {
        return newsRepo.findByText(text);
    }

    @Override
    public List<News> findByTextContains(String text) {
        return newsRepo.findByTextContains(text);
    }

    @Override
    public void save(News news) {
        newsRepo.save(news);
    }

    @Override
    public void update(Long id, News news) {
        if (news != null && id.equals(news.getId())) {
            if (newsRepo.existsById(news.getId())) {
                newsRepo.save(news);
            } else {
                throw new NewsNotFoundException(news.getId());
            }
        } else {
            throw new BadRequestParametersException("Error in data:" +
                    " path variable id must be not null and equal to employee id");
        }
    }

    @Override
    public void deleteById(Long id) {
        if (newsRepo.existsById(id)) {
            newsRepo.deleteById(id);
        } else {
            throw new NewsNotFoundException(id);
        }
    }
}
