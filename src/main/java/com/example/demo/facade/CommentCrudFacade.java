package com.example.demo.facade;


import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.exception.request.NotEnoughRightsException;

import java.util.Optional;

import static com.example.demo.dto.CommentProto.CommentDto;
import static com.example.demo.dto.CommentProto.CommentSimpleDto;
import static com.example.demo.dto.CommentProto.CommentSimpleDtoList;

/**
 * Interface for CRUD operations with {@link Comment} in facade layer
 */
public interface CommentCrudFacade {
    /**
     * Method which returns all available {@link Comment} and convert it into {@link CommentSimpleDto}
     *
     * @return {@link CommentSimpleDtoList} of {@link CommentSimpleDto}
     */
    CommentSimpleDtoList findAll();

    /**
     * Method which returns all available {@link Comment} with page format
     * and convert it into {@link CommentSimpleDto}
     *
     * @param page page number
     * @param size page size
     * @return {@link CommentSimpleDtoList} of {@link CommentSimpleDto}
     */
    CommentSimpleDtoList findAll(Integer page, Integer size);

    /**
     * Method which returns {@link Comment} with specified id
     * and convert it into {@link CommentDto}
     *
     * @param id {@link Comment} objects id for search
     * @return {@link CommentDto} object wrap into {@link Optional}
     */
    Optional<CommentDto> findById(Long id);

    /**
     * Method finds {@link Comment} objects by text
     * and convert it into {@link CommentSimpleDto}
     *
     * @param text {@link Comment} object to find object by text
     * @return {@link CommentSimpleDtoList} that have provided text
     */
    CommentSimpleDtoList findByText(String text);

    /**
     * Method finds {@link Comment} objects by text partially contains param
     * and convert it into {@link CommentSimpleDto}
     *
     * @param text {@link Comment} object to find object by text
     * @return {@link CommentSimpleDtoList} that have provided text
     */
    CommentSimpleDtoList findByTextContains(String text);

    /**
     * Method which creates new {@link Comment}.
     * Converts {@link CommentSimpleDto} to {@link Comment} and then save it
     *
     * @param commentDto {@link CommentSimpleDto} dto object which needs to created
     * @param user       {@link User} that call method
     */
    void save(CommentSimpleDto commentDto, User user);

    /**
     * Method which updates {@link Comment}.
     * Converts {@link CommentSimpleDto} to {@link Comment} and then update it
     *
     * @param id         {@link Comment} from request url
     * @param commentDto {@link CommentSimpleDto} object dto to update
     * @param user       {@link User} that call method
     * @throws NotEnoughRightsException if user dont have right to update comment
     */
    void update(Long id, CommentSimpleDto commentDto, User user);

    /**
     * Method which deletes {@link Comment} object by id
     *
     * @param id   {@link Comment} object to delete
     * @param user {@link User} that call method
     * @throws NotEnoughRightsException if user dont have right to delete comment
     */
    void deleteById(Long id, User user);
}
