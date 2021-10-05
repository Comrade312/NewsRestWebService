package com.example.demo.facade;

import com.example.demo.entity.Comment;
import com.example.demo.entity.News;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.comment.CommentNotFoundException;
import com.example.demo.exception.news.NewsNotFoundException;
import com.example.demo.exception.request.NotEnoughRightsException;
import com.example.demo.service.CommentService;
import com.example.demo.service.NewsService;
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
public class CommentFacade {
    @Autowired
    private CommentService commentService;

    @Autowired
    private NewsService newsService;

    /**
     * Method which returns all available {@link Comment} and convert it into {@link CommentSimpleDto}
     *
     * @return {@link CommentSimpleDtoList} of {@link CommentSimpleDto}
     */
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

    /**
     * Method which returns all available {@link Comment} with page format
     * and convert it into {@link CommentSimpleDto}
     *
     * @param page page number
     * @param size page size
     * @return {@link CommentSimpleDtoList} of {@link CommentSimpleDto}
     */
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

    /**
     * Method which returns {@link Comment} with specified id
     * and convert it into {@link CommentDto}
     *
     * @param id {@link Comment} objects id for search
     * @return {@link CommentDto} object wrap into {@link Optional}
     */
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

    /**
     * Method finds {@link Comment} objects by text
     * and convert it into {@link CommentSimpleDto}
     *
     * @param text {@link Comment} object to find object by text
     * @return {@link CommentSimpleDtoList} that have provided text
     */
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

    /**
     * Method finds {@link Comment} objects by text partially contains param
     * and convert it into {@link CommentSimpleDto}
     *
     * @param text {@link Comment} object to find object by text
     * @return {@link CommentSimpleDtoList} that have provided text
     */
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

    /**
     * Method which creates new {@link Comment}.
     * Converts {@link CommentSimpleDto} to {@link Comment} and then save it
     *
     * @param commentDto {@link CommentSimpleDto} dto object which needs to created
     * @param user       {@link User} that call method
     */
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

    /**
     * Method which updates {@link Comment}.
     * Converts {@link CommentSimpleDto} to {@link Comment} and then update it
     *
     * @param id         {@link Comment} from request url
     * @param commentDto {@link CommentSimpleDto} object dto to update
     * @param user       {@link User} that call method
     * @throws NotEnoughRightsException if user dont have right to update comment
     */
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

    /**
     * Method which deletes {@link Comment} object by id
     *
     * @param id   {@link Comment} object to delete
     * @param user {@link User} that call method
     * @throws NotEnoughRightsException if user dont have right to delete comment
     */
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