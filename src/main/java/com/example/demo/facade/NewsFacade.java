package com.example.demo.facade;

import com.example.demo.entity.Comment;
import com.example.demo.entity.News;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.news.NewsNotFoundException;
import com.example.demo.exception.request.NotEnoughRightsException;
import com.example.demo.service.CommentService;
import com.example.demo.service.NewsService;
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
public class NewsFacade {
    @Autowired
    private NewsService newsService;

    @Autowired
    private CommentService commentService;

    /**
     * Method which returns all available {@link News}
     * and convert it into {@link NewsSimpleDto}
     *
     * @return {@link NewsSimpleDtoList} of {@link NewsSimpleDto}
     */
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

    /**
     * Method which returns all available {@link News} with page format
     * and convert it into {@link NewsSimpleDto}
     *
     * @param page page number
     * @param size page size
     * @return {@link NewsSimpleDtoList} of {@link NewsSimpleDto}
     */
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

    /**
     * Method which returns {@link News} with specified id
     * and convert it into {@link NewsDto}
     *
     * @param id {@link News} objects id for search
     * @return {@link NewsDto} object wrap into {@link Optional}
     */
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

    /**
     * Method which returns {@link News} with specified id and pageable comments
     * and convert it into {@link NewsDto}
     *
     * @param id   {@link News} objects id for search
     * @param page page number for comments
     * @param size page size for comments
     * @return {@link NewsDto} object wrap into {@link Optional}
     */
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

    /**
     * Method finds {@link News} objects by title
     * and convert it into {@link NewsSimpleDto}
     *
     * @param title {@link News} object to find object by text
     * @return {@link NewsSimpleDtoList} that have provided text
     */
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

    /**
     * Method finds {@link News} objects by title partially contains param
     * and convert it into {@link NewsSimpleDto}
     *
     * @param title {@link News} object to find object by text
     * @return {@link NewsSimpleDtoList} that have provided text
     */
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

    /**
     * Method finds {@link News} objects by text
     * and convert it into {@link NewsSimpleDto}
     *
     * @param text {@link News} object to find object by text
     * @return {@link NewsSimpleDtoList} that have provided text
     */
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

    /**
     * Method finds {@link News} objects by text partially contains param
     * and convert it into {@link NewsSimpleDto}
     *
     * @param text {@link News} object to find object by text
     * @return {@link NewsSimpleDtoList} that have provided text
     */
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

    /**
     * Method which creates new {@link News}.
     * Converts {@link NewsSimpleDto} to {@link News} and then save it
     *
     * @param newsDto {@link NewsSimpleDto} dto object which needs to created
     * @param user    {@link User} that call method
     */
    public void save(NewsSimpleDto newsDto, User user) {
        News news = new News();
        news.setDate(LocalDateTime.now());
        news.setTitle(newsDto.getTitle());
        news.setText(newsDto.getText());
        news.setUser(user);

        newsService.save(news);
    }

    /**
     * Method which updates {@link News}.
     * Converts {@link NewsSimpleDto} to {@link News} and then update it
     *
     * @param id      {@link News} from request url
     * @param newsDto {@link NewsSimpleDto} object dto to update
     * @param user    {@link User} that call method
     * @throws NotEnoughRightsException if user dont have right to update news
     */
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

    /**
     * Method which deletes {@link News} object by id
     *
     * @param id   {@link News} object to delete
     * @param user {@link User} that call method
     * @throws NotEnoughRightsException if user dont have right to delete news
     */
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
