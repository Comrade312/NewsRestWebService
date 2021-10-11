package com.example.demo.service;

import com.example.demo.entity.News;
import com.example.demo.exception.news.NewsNotFoundException;
import com.example.demo.exception.request.BadRequestParametersException;

import java.util.List;
import java.util.Optional;

/**
 * Interface for CRUD operations with {@link News}
 */
public interface NewsCrudService {
    /**
     * Find all {@link News} objects
     *
     * @return list of {@link News} objects.
     */
    List<News> findAll();

    /**
     * Find all {@link News} objects by pages
     *
     * @param page page number
     * @param size page size
     * @return list of {@link News} objects for provided page.
     */
    List<News> findAll(Integer page, Integer size);

    /**
     * Find {@link News} object by id
     *
     * @param id {@link News} object to find object by id.
     * @return {@link News} object wrapped into {@link Optional}
     */
    Optional<News> findById(Long id);

    /**
     * Find {@link News} objects by text
     *
     * @param title {@link News} object to find object by text
     * @return list of {@link News} that have provided text.
     */
    List<News> findByTitle(String title);

    /**
     * Find {@link News} objects by text partially contains param
     *
     * @param title {@link News} object to find object by text
     * @return list of {@link News} that have provided text.
     */
    List<News> findByTitleContains(String title);

    /**
     * Find {@link News} objects by text
     *
     * @param text {@link News} object to find object by text
     * @return list of {@link News} that have provided text.
     */
    List<News> findByText(String text);

    /**
     * Find {@link News} objects by text partially contains param
     *
     * @param text {@link News} object to find object by text
     * @return list of {@link News} that have provided text.
     */
    List<News> findByTextContains(String text);

    /**
     * Save {@link News} object to save
     *
     * @param news {@link News} object to save
     */
    void save(News news);

    /**
     * Update {@link News} object
     *
     * @param id   {@link News} from request url
     * @param news {@link News} object to update
     * @throws BadRequestParametersException when id from url not equal with id from {@link News} object
     * @throws NewsNotFoundException         when there is no {@link News} object in database with provided id
     */
    void update(Long id, News news);

    /**
     * Delete {@link News} object by id
     *
     * @param id {@link News} object to delete
     * @throws NewsNotFoundException when there is no {@link News} object in database with provided id
     */
    void deleteById(Long id);
}
