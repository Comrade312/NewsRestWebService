package com.example.demo.facade;

import com.example.demo.entity.News;
import com.example.demo.entity.User;
import com.example.demo.exception.request.NotEnoughRightsException;

import java.util.Optional;

import static com.example.demo.dto.NewsProto.NewsDto;
import static com.example.demo.dto.NewsProto.NewsSimpleDto;
import static com.example.demo.dto.NewsProto.NewsSimpleDtoList;

/**
 * Interface for CRUD operations with {@link News} in facade layer
 */
public interface NewsCrudFacade {
    /**
     * Method which returns all available {@link News}
     * and convert it into {@link NewsSimpleDto}
     *
     * @return {@link NewsSimpleDtoList} of {@link NewsSimpleDto}
     */
    NewsSimpleDtoList findAll();

    /**
     * Method which returns all available {@link News} with page format
     * and convert it into {@link NewsSimpleDto}
     *
     * @param page page number
     * @param size page size
     * @return {@link NewsSimpleDtoList} of {@link NewsSimpleDto}
     */
    NewsSimpleDtoList findAll(Integer page, Integer size);

    /**
     * Method which returns {@link News} with specified id
     * and convert it into {@link NewsDto}
     *
     * @param id {@link News} objects id for search
     * @return {@link NewsDto} object wrap into {@link Optional}
     */
    Optional<NewsDto> findById(Long id);

    /**
     * Method which returns {@link News} with specified id and pageable comments
     * and convert it into {@link NewsDto}
     *
     * @param id   {@link News} objects id for search
     * @param page page number for comments
     * @param size page size for comments
     * @return {@link NewsDto} object wrap into {@link Optional}
     */
    Optional<NewsDto> findByIdWithPageComment(Long id, Integer page, Integer size);

    /**
     * Method finds {@link News} objects by title
     * and convert it into {@link NewsSimpleDto}
     *
     * @param title {@link News} object to find object by text
     * @return {@link NewsSimpleDtoList} that have provided text
     */
    NewsSimpleDtoList findByTitle(String title);

    /**
     * Method finds {@link News} objects by title partially contains param
     * and convert it into {@link NewsSimpleDto}
     *
     * @param title {@link News} object to find object by text
     * @return {@link NewsSimpleDtoList} that have provided text
     */
    NewsSimpleDtoList findByTitleContains(String title);

    /**
     * Method finds {@link News} objects by text
     * and convert it into {@link NewsSimpleDto}
     *
     * @param text {@link News} object to find object by text
     * @return {@link NewsSimpleDtoList} that have provided text
     */
    NewsSimpleDtoList findByText(String text);

    /**
     * Method finds {@link News} objects by text partially contains param
     * and convert it into {@link NewsSimpleDto}
     *
     * @param text {@link News} object to find object by text
     * @return {@link NewsSimpleDtoList} that have provided text
     */
    NewsSimpleDtoList findByTextContains(String text);

    /**
     * Method which creates new {@link News}.
     * Converts {@link NewsSimpleDto} to {@link News} and then save it
     *
     * @param newsDto {@link NewsSimpleDto} dto object which needs to created
     * @param user    {@link User} that call method
     */
    void save(NewsSimpleDto newsDto, User user);

    /**
     * Method which updates {@link News}.
     * Converts {@link NewsSimpleDto} to {@link News} and then update it
     *
     * @param id      {@link News} from request url
     * @param newsDto {@link NewsSimpleDto} object dto to update
     * @param user    {@link User} that call method
     * @throws NotEnoughRightsException if user dont have right to update news
     */
    void update(Long id, NewsSimpleDto newsDto, User user);

    /**
     * Method which deletes {@link News} object by id
     *
     * @param id   {@link News} object to delete
     * @param user {@link User} that call method
     * @throws NotEnoughRightsException if user dont have right to delete news
     */
    void deleteById(Long id, User user);
}
