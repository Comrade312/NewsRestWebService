package com.example.demo.facade.impl;

import com.example.demo.entity.Comment;
import com.example.demo.entity.News;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.comment.CommentNotFoundException;
import com.example.demo.exception.news.NewsNotFoundException;
import com.example.demo.exception.request.NotEnoughRightsException;
import com.example.demo.facade.CommentCrudFacade;
import com.example.demo.service.impl.CommentService;
import com.example.demo.service.impl.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.dto.CommentProto.CommentDto;
import static com.example.demo.dto.CommentProto.CommentNews;
import static com.example.demo.dto.CommentProto.CommentSimpleDto;
import static com.example.demo.dto.CommentProto.CommentSimpleDtoList;

/**
 * CRUD operation in the {@link Comment} Facade layer
 * Calls a method from {@link CommentService}
 */
@Service
public class CommentFacade implements CommentCrudFacade {
    private final CommentService commentService;

    private final NewsService newsService;

    @Autowired
    public CommentFacade(CommentService commentService, NewsService newsService) {
        this.commentService = commentService;
        this.newsService = newsService;
    }

    @Override
    public CommentSimpleDtoList findAll() {
        return CommentSimpleDtoList.newBuilder()
                .addAllCommentDto(commentService.findAll().stream()
                        .map(value -> CommentSimpleDto.newBuilder()
                                .setId(value.getId())
                                .setDate(value.getDate().toString())
                                .setText(value.getText())
                                .setUserId(value.getUser().getId())
                                .setNewsId(value.getNews().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public CommentSimpleDtoList findAll(Integer page, Integer size) {
        return CommentSimpleDtoList.newBuilder()
                .addAllCommentDto(commentService.findAll(page, size).stream()
                        .map(value -> CommentSimpleDto.newBuilder()
                                .setId(value.getId())
                                .setDate(value.getDate().toString())
                                .setText(value.getText())
                                .setUserId(value.getUser().getId())
                                .setNewsId(value.getNews().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public Optional<CommentDto> findById(Long id) {
        return commentService.findById(id)
                .map(value -> CommentDto.newBuilder()
                        .setId(value.getId())
                        .setDate(value.getDate().toString())
                        .setText(value.getText())
                        .setUserId(value.getUser().getId())
                        .setNews(CommentNews.newBuilder()
                                .setId(value.getNews().getId())
                                .setDate(value.getNews().getDate().toString())
                                .setTitle(value.getNews().getTitle())
                                .setText(value.getNews().getText())
                                .setUserId(value.getNews().getUser().getId())
                                .build())
                        .build()
                );
    }

    @Override
    public CommentSimpleDtoList findByText(String text) {
        return CommentSimpleDtoList.newBuilder()
                .addAllCommentDto(commentService.findByText(text).stream()
                        .map(value -> CommentSimpleDto.newBuilder()
                                .setId(value.getId())
                                .setDate(value.getDate().toString())
                                .setText(value.getText())
                                .setUserId(value.getUser().getId())
                                .setNewsId(value.getNews().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public CommentSimpleDtoList findByTextContains(String text) {
        return CommentSimpleDtoList.newBuilder()
                .addAllCommentDto(commentService.findByTextContains(text).stream()
                        .map(value -> CommentSimpleDto.newBuilder()
                                .setId(value.getId())
                                .setDate(value.getDate().toString())
                                .setText(value.getText())
                                .setUserId(value.getUser().getId())
                                .setNewsId(value.getNews().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public void save(CommentSimpleDto commentDto, User user) {
        Comment comment = new Comment();
        comment.setDate(LocalDateTime.now());
        comment.setText(commentDto.getText());

        News news = newsService.findById(commentDto.getNewsId())
                .orElseThrow(() -> new NewsNotFoundException(commentDto.getNewsId()));

        comment.setNews(news);
        comment.setUser(user);

        commentService.save(comment);
    }

    @Override
    public void update(Long id, CommentSimpleDto commentDto, User user) {
        //Only owner or admin could update comment
        if (commentDto.getUserId() == user.getId() || user.getAuthorities().contains(Role.ADMIN)) {
            Comment comment = commentService.findById(commentDto.getId())
                    .orElseThrow(() -> new CommentNotFoundException(commentDto.getId()));
            comment.setText(commentDto.getText());

            commentService.update(id, comment);
        } else {
            throw new NotEnoughRightsException("User with id " + user.getId()
                    + " cannot update comment of user with id " + commentDto.getUserId());
        }
    }

    @Override
    public void deleteById(Long id, User user) {
        Comment comment = commentService.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));

        //Only owner or admin could delete comment
        if (comment.getUser().getId().equals(user.getId()) || user.getAuthorities().contains(Role.ADMIN)) {
            commentService.deleteById(id);
        } else {
            throw new NotEnoughRightsException("User with id " + user.getId()
                    + " cannot delete comment of user with id " + comment.getUser().getId());
        }
    }
}