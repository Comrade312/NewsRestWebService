package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.facade.CommentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.dto.CommentProto.CommentDto;
import static com.example.demo.dto.CommentProto.CommentSimpleDto;
import static com.example.demo.dto.CommentProto.CommentSimpleDtoList;

/**
 * Rest controller for operations with {@link Comment}
 */
@RestController
@RequestMapping(value = "/api/comment")
public class CommentController {
    @Autowired
    private CommentFacade commentFacade;

    /**
     * Method which shows all available {@link Comment}
     *
     * @return {@link CommentSimpleDtoList} of {@link CommentSimpleDto}
     */
    @GetMapping
    public ResponseEntity<CommentSimpleDtoList> findAllComment() {
        return new ResponseEntity<>(commentFacade.findAll(), HttpStatus.OK);
    }

    /**
     * Method which shows all available {@link Comment} with page format
     *
     * @param page page number
     * @param size page size
     * @return {@link CommentSimpleDtoList} of {@link CommentSimpleDto}
     */
    @GetMapping(params = {"page", "size"})
    public ResponseEntity<CommentSimpleDtoList> findAllComment(@RequestParam Integer page,
                                                               @RequestParam Integer size) {
        return new ResponseEntity<>(commentFacade.findAll(page, size), HttpStatus.OK);
    }

    /**
     * Method which shows {@link Comment} with specified id
     *
     * @param id {@link Comment} objects id for search
     * @return {@link CommentDto} object or Http Code NOT_FOUND(404)
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentDto> findCommentById(@PathVariable Long id) {
        return commentFacade.findById(id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    /**
     * Method which finds {@link Comment} objects by text
     *
     * @param text {@link Comment} object to find object by text
     * @return {@link CommentSimpleDtoList} that have provided text
     */
    @GetMapping(params = {"text"})
    public ResponseEntity<CommentSimpleDtoList> findCommentByText(@RequestParam String text) {
        return new ResponseEntity<>(commentFacade.findByText(text), HttpStatus.OK);
    }

    /**
     * Method which finds {@link Comment} objects by text partially contains param
     *
     * @param textLike {@link Comment} object to find object by text
     * @return {@link CommentSimpleDtoList} that have provided text
     */
    @GetMapping(params = {"textLike"})
    public ResponseEntity<CommentSimpleDtoList> findCommentByTextLike(@RequestParam String textLike) {
        return new ResponseEntity<>(commentFacade.findByTextContains(textLike), HttpStatus.OK);
    }

    /**
     * Method which creates new {@link Comment}
     *
     * @param comment {@link CommentSimpleDto} dto object which needs to created
     * @param user    {@link User} that call method
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST', 'SUBSCRIBER')")
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentSimpleDto comment,
                                              @AuthenticationPrincipal User user) {
        commentFacade.save(comment, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Method which updates {@link Comment}
     *
     * @param id      {@link Comment} from request url
     * @param comment {@link CommentSimpleDto} object dto to update
     * @param user    {@link User} that call method
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST', 'SUBSCRIBER')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable Long id,
                                              @RequestBody CommentSimpleDto comment,
                                              @AuthenticationPrincipal User user) {
        commentFacade.update(id, comment, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Method which deletes {@link Comment} object by id
     *
     * @param id   {@link Comment} object to delete
     * @param user {@link User} that call method
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST', 'SUBSCRIBER')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id,
                                              @AuthenticationPrincipal User user) {
        commentFacade.deleteById(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
