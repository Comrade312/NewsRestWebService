package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.News;
import com.example.demo.exception.comment.CommentNotFoundException;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.repo.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Provides CRUD operation with {@link Comment}
 * Calls a method from {@link CommentRepo}
 */
@Service
public class CommentService {
    @Autowired
    private CommentRepo commentRepo;

    /**
     * Find {@link Comment} object by id
     *
     * @param id {@link Comment} object to find object by id.
     * @return {@link Comment} object wrapped into {@link Optional}
     */
    public Optional<Comment> findById(Long id) {
        return commentRepo.findById(id);
    }

    /**
     * Find all {@link Comment} objects
     *
     * @return list of {@link Comment} objects.
     */
    public List<Comment> findAll() {
        return commentRepo.findAll();
    }

    /**
     * Find all {@link Comment} objects by pages
     *
     * @param page page number
     * @param size page size
     * @return list of {@link Comment} objects for provided page.
     */
    public List<Comment> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date"));
        return commentRepo.findAll(pageable).getContent();
    }

    /**
     * Find {@link Comment} objects by pages,
     * that tied to {@link News} object with provided id
     *
     * @param newsId {@link News} object to find by news
     * @param page   page number
     * @param size   page size
     * @return list of {@link Comment} objects by pages, that tied to {@link News} object.
     */
    public List<Comment> findByNewsId(Long newsId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date"));
        return commentRepo.findByNewsId(newsId, pageable).getContent();
    }

    /**
     * Find {@link Comment} objects by text
     *
     * @param text {@link Comment} object to find object by text
     * @return list of {@link Comment} that have provided text.
     */
    public List<Comment> findByText(String text) {
        return commentRepo.findByText(text);
    }

    /**
     * Find {@link Comment} objects by text partially contains param
     *
     * @param text {@link Comment} object to find object by text
     * @return list of {@link Comment} that have provided text.
     */
    public List<Comment> findByTextContains(String text) {
        return commentRepo.findByTextContains(text);
    }

    /**
     * Save {@link Comment} object to save
     *
     * @param comment {@link Comment} object to save
     */
    public void save(Comment comment) {
        commentRepo.save(comment);
    }

    /**
     * Update {@link Comment} object
     *
     * @param id      {@link Comment} from request url
     * @param comment {@link Comment} object to update
     * @throws BadRequestParametersException when id from url not equal with id from {@link Comment} object
     * @throws CommentNotFoundException      when there is no {@link Comment} object in database with provided id
     */
    public void update(Long id, Comment comment) {
        if (comment != null && id.equals(comment.getId())) {
            if (commentRepo.existsById(comment.getId())) {
                commentRepo.save(comment);
            } else {
                throw new CommentNotFoundException(comment.getId());
            }
        } else {
            throw new BadRequestParametersException("Error in data:" +
                    " path variable id must be not null and equal to employee id");
        }
    }

    /**
     * Delete {@link Comment} object by id
     *
     * @param id {@link Comment} object to delete
     * @throws CommentNotFoundException when there is no {@link Comment} object in database with provided id
     */
    public void deleteById(Long id) {
        if (commentRepo.existsById(id)) {
            commentRepo.deleteById(id);
        } else {
            throw new CommentNotFoundException(id);
        }
    }
}