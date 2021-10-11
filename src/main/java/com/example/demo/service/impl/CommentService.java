package com.example.demo.service.impl;

import com.example.demo.entity.Comment;
import com.example.demo.exception.comment.CommentNotFoundException;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.repo.CommentRepo;
import com.example.demo.service.CommentCrudService;
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
public class CommentService implements CommentCrudService {
    private final CommentRepo commentRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepo.findById(id);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepo.findAll();
    }

    @Override
    public List<Comment> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date"));
        return commentRepo.findAll(pageable).getContent();
    }

    @Override
    public List<Comment> findByNewsId(Long newsId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date"));
        return commentRepo.findByNewsId(newsId, pageable).getContent();
    }

    @Override
    public List<Comment> findByText(String text) {
        return commentRepo.findByText(text);
    }

    @Override
    public List<Comment> findByTextContains(String text) {
        return commentRepo.findByTextContains(text);
    }

    @Override
    public void save(Comment comment) {
        commentRepo.save(comment);
    }

    @Override
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

    @Override
    public void deleteById(Long id) {
        if (commentRepo.existsById(id)) {
            commentRepo.deleteById(id);
        } else {
            throw new CommentNotFoundException(id);
        }
    }
}