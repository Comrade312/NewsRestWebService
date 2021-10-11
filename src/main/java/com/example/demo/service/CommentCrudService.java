package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.News;
import com.example.demo.exception.comment.CommentNotFoundException;
import com.example.demo.exception.request.BadRequestParametersException;

import java.util.List;
import java.util.Optional;

/**
 * Interface for CRUD operations with {@link Comment}
 */
public interface CommentCrudService {
    /**
     * Find {@link Comment} object by id
     *
     * @param id {@link Comment} object to find object by id.
     * @return {@link Comment} object wrapped into {@link Optional}
     */
    Optional<Comment> findById(Long id);

    /**
     * Find all {@link Comment} objects
     *
     * @return list of {@link Comment} objects.
     */
    List<Comment> findAll();

    /**
     * Find all {@link Comment} objects by pages
     *
     * @param page page number
     * @param size page size
     * @return list of {@link Comment} objects for provided page.
     */
    List<Comment> findAll(Integer page, Integer size);

    /**
     * Find {@link Comment} objects by pages,
     * that tied to {@link News} object with provided id
     *
     * @param newsId {@link News} object to find by news
     * @param page   page number
     * @param size   page size
     * @return list of {@link Comment} objects by pages, that tied to {@link News} object.
     */
    List<Comment> findByNewsId(Long newsId, Integer page, Integer size);

    /**
     * Find {@link Comment} objects by text
     *
     * @param text {@link Comment} object to find object by text
     * @return list of {@link Comment} that have provided text.
     */
    List<Comment> findByText(String text);

    /**
     * Find {@link Comment} objects by text partially contains param
     *
     * @param text {@link Comment} object to find object by text
     * @return list of {@link Comment} that have provided text.
     */
    List<Comment> findByTextContains(String text);

    /**
     * Save {@link Comment} object to save
     *
     * @param comment {@link Comment} object to save
     */
    void save(Comment comment);

    /**
     * Update {@link Comment} object
     *
     * @param id      {@link Comment} from request url
     * @param comment {@link Comment} object to update
     * @throws BadRequestParametersException when id from url not equal with id from {@link Comment} object
     * @throws CommentNotFoundException      when there is no {@link Comment} object in database with provided id
     */
    void update(Long id, Comment comment);

    /**
     * Delete {@link Comment} object by id
     *
     * @param id {@link Comment} object to delete
     * @throws CommentNotFoundException when there is no {@link Comment} object in database with provided id
     */
    void deleteById(Long id);
}
