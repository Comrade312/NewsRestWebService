package com.example.demo.facade.impl;

import com.example.demo.entity.Comment;
import com.example.demo.entity.News;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.news.NewsNotFoundException;
import com.example.demo.exception.request.NotEnoughRightsException;
import com.example.demo.facade.NewsCrudFacade;
import com.example.demo.service.impl.CommentService;
import com.example.demo.service.impl.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.dto.NewsProto.NewsComment;
import static com.example.demo.dto.NewsProto.NewsDto;
import static com.example.demo.dto.NewsProto.NewsSimpleDto;
import static com.example.demo.dto.NewsProto.NewsSimpleDtoList;

/**
 * CRUD operation in the {@link News} Facade layer
 * Calls a method from {@link NewsService}
 */
@Service
public class NewsFacade implements NewsCrudFacade {
    private final NewsService newsService;

    private final CommentService commentService;

    @Autowired
    public NewsFacade(NewsService newsService, CommentService commentService) {
        this.newsService = newsService;
        this.commentService = commentService;
    }

    @Override
    public NewsSimpleDtoList findAll() {
        return NewsSimpleDtoList.newBuilder()
                .addAllNewsDto(newsService.findAll().stream()
                        .map(value -> NewsSimpleDto.newBuilder()
                                .setId(value.getId())
                                .setDate(value.getDate().toString())
                                .setTitle(value.getTitle())
                                .setText(value.getText())
                                .setUserId(value.getUser().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public NewsSimpleDtoList findAll(Integer page, Integer size) {
        return NewsSimpleDtoList.newBuilder()
                .addAllNewsDto(newsService.findAll(page, size).stream()
                        .map(value -> NewsSimpleDto.newBuilder()
                                .setId(value.getId())
                                .setDate(value.getDate().toString())
                                .setText(value.getText())
                                .setUserId(value.getUser().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public Optional<NewsDto> findById(Long id) {
        return newsService.findById(id)
                .map(value -> NewsDto.newBuilder()
                        .setId(value.getId())
                        .setDate(value.getDate().toString())
                        .setTitle(value.getTitle())
                        .setText(value.getText())
                        .addAllComments(
                                value.getComments().stream()
                                        .map(v -> NewsComment.newBuilder()
                                                .setId(v.getId())
                                                .setDate(v.getDate().toString())
                                                .setText(v.getText())
                                                .setUserId(v.getUser().getId())
                                                .build())
                                        .collect(Collectors.toList()))
                        .build()
                );
    }

    @Override
    public Optional<NewsDto> findByIdWithPageComment(Long id, Integer page, Integer size) {
        List<Comment> comments = commentService.findByNewsId(id, page, size);
        return newsService.findById(id)
                .map(value -> NewsDto.newBuilder()
                        .setId(value.getId())
                        .setDate(value.getDate().toString())
                        .setTitle(value.getTitle())
                        .setText(value.getText())
                        .addAllComments(
                                comments.stream()
                                        .map(v -> NewsComment.newBuilder()
                                                .setId(v.getId())
                                                .setDate(v.getDate().toString())
                                                .setText(v.getText())
                                                .setUserId(v.getUser().getId())
                                                .build())
                                        .collect(Collectors.toList()))
                        .build()
                );
    }

    @Override
    public NewsSimpleDtoList findByTitle(String title) {
        return NewsSimpleDtoList.newBuilder()
                .addAllNewsDto(newsService.findByTitle(title).stream()
                        .map(value -> NewsSimpleDto.newBuilder()
                                .setId(value.getId())
                                .setDate(value.getDate().toString())
                                .setText(value.getText())
                                .setUserId(value.getUser().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public NewsSimpleDtoList findByTitleContains(String title) {
        return NewsSimpleDtoList.newBuilder()
                .addAllNewsDto(newsService.findByTitleContains(title).stream()
                        .map(value -> NewsSimpleDto.newBuilder()
                                .setId(value.getId())
                                .setDate(value.getDate().toString())
                                .setText(value.getText())
                                .setUserId(value.getUser().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public NewsSimpleDtoList findByText(String text) {
        return NewsSimpleDtoList.newBuilder()
                .addAllNewsDto(newsService.findByText(text).stream()
                        .map(value -> NewsSimpleDto.newBuilder()
                                .setId(value.getId())
                                .setDate(value.getDate().toString())
                                .setText(value.getText())
                                .setUserId(value.getUser().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public NewsSimpleDtoList findByTextContains(String text) {
        return NewsSimpleDtoList.newBuilder()
                .addAllNewsDto(newsService.findByTextContains(text).stream()
                        .map(value -> NewsSimpleDto.newBuilder()
                                .setId(value.getId())
                                .setDate(value.getDate().toString())
                                .setText(value.getText())
                                .setUserId(value.getUser().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public void save(NewsSimpleDto newsDto, User user) {
        News news = new News();
        news.setDate(LocalDateTime.now());
        news.setTitle(newsDto.getTitle());
        news.setText(newsDto.getText());
        news.setUser(user);

        newsService.save(news);
    }

    @Override
    public void update(Long id, NewsSimpleDto newsDto, User user) {
        //Can be updated by owner or admin
        if ((newsDto.getUserId() == user.getId() && !user.getAuthorities().contains(Role.SUBSCRIBER))
                || user.getAuthorities().contains(Role.ADMIN)) {
            News news = newsService.findById(newsDto.getId())
                    .orElseThrow(() -> new NewsNotFoundException(newsDto.getId()));
            news.setTitle(newsDto.getTitle());
            news.setText(newsDto.getText());

            newsService.update(id, news);
        } else {
            throw new NotEnoughRightsException("User with id " + user.getId()
                    + " cannot update comment of user with id " + newsDto.getUserId());
        }
    }

    @Override
    public void deleteById(Long id, User user) {
        News news = newsService.findById(id)
                .orElseThrow(() -> new NewsNotFoundException(id));

        //Can be deleted by owner or admin
        if ((news.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(Role.SUBSCRIBER))
                || user.getAuthorities().contains(Role.ADMIN)) {
            newsService.deleteById(id);
        } else {
            throw new NotEnoughRightsException("User with id " + user.getId()
                    + " cannot delete news of user with id " + news.getUser().getId());
        }
    }
}
