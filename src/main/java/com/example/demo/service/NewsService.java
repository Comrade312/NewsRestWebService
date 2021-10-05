package com.example.demo.service;

import com.example.demo.entity.News;
import com.example.demo.exception.news.NewsNotFoundException;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.repo.NewsRepo;
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
public class NewsService {
    @Autowired
    private NewsRepo newsRepo;

    /**
     * Find all {@link News} objects
     *
     * @return list of {@link News} objects.
     */
    public List<News> findAll() {
        return newsRepo.findAll();
    }

    /**
     * Find all {@link News} objects by pages
     *
     * @param page page number
     * @param size page size
     * @return list of {@link News} objects for provided page.
     */
    public List<News> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date"));
        return newsRepo.findAll(pageable).getContent();
    }

    /**
     * Find {@link News} object by id
     *
     * @param id {@link News} object to find object by id.
     * @return {@link News} object wrapped into {@link Optional}
     */
    public Optional<News> findById(Long id) {
        return newsRepo.findById(id);
    }

    /**
     * Find {@link News} objects by text
     *
     * @param title {@link News} object to find object by text
     * @return list of {@link News} that have provided text.
     */
    public List<News> findByTitle(String title) {
        return newsRepo.findByTitle(title);
    }

    /**
     * Find {@link News} objects by text partially contains param
     *
     * @param title {@link News} object to find object by text
     * @return list of {@link News} that have provided text.
     */
    public List<News> findByTitleContains(String title) {
        return newsRepo.findByTitleContains(title);
    }

    /**
     * Find {@link News} objects by text
     *
     * @param text {@link News} object to find object by text
     * @return list of {@link News} that have provided text.
     */
    public List<News> findByText(String text) {
        return newsRepo.findByText(text);
    }

    /**
     * Find {@link News} objects by text partially contains param
     *
     * @param text {@link News} object to find object by text
     * @return list of {@link News} that have provided text.
     */
    public List<News> findByTextContains(String text) {
        return newsRepo.findByTextContains(text);
    }

    /**
     * Save {@link News} object to save
     *
     * @param news {@link News} object to save
     */
    public void save(News news) {
        newsRepo.save(news);
    }

    /**
     * Update {@link News} object
     *
     * @param id   {@link News} from request url
     * @param news {@link News} object to update
     * @throws BadRequestParametersException when id from url not equal with id from {@link News} object
     * @throws NewsNotFoundException         when there is no {@link News} object in database with provided id
     */
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

    /**
     * Delete {@link News} object by id
     *
     * @param id {@link News} object to delete
     * @throws NewsNotFoundException when there is no {@link News} object in database with provided id
     */
    public void deleteById(Long id) {
        if (newsRepo.existsById(id)) {
            newsRepo.deleteById(id);
        } else {
            throw new NewsNotFoundException(id);
        }
    }
}
