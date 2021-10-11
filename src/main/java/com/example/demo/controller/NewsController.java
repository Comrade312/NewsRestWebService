package com.example.demo.controller;


import com.example.demo.entity.News;
import com.example.demo.entity.User;
import com.example.demo.facade.NewsCrudFacade;
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

import static com.example.demo.dto.NewsProto.NewsDto;
import static com.example.demo.dto.NewsProto.NewsSimpleDto;
import static com.example.demo.dto.NewsProto.NewsSimpleDtoList;

/**
 * Rest controller for operations with {@link News}
 */
@RestController
@RequestMapping(value = "/api/news")
public class NewsController {
    private final NewsCrudFacade newsFacade;

    @Autowired
    public NewsController(NewsCrudFacade newsFacade) {
        this.newsFacade = newsFacade;
    }

    /**
     * Method which shows all available {@link News}
     *
     * @return {@link NewsSimpleDtoList} of {@link NewsSimpleDto}.
     */
    @GetMapping
    public ResponseEntity<NewsSimpleDtoList> findAllNews() {
        return new ResponseEntity<>(newsFacade.findAll(), HttpStatus.OK);
    }

    /**
     * Method which shows all available {@link News} with page format
     *
     * @param page page number
     * @param size page size
     * @return {@link NewsSimpleDtoList} of {@link NewsSimpleDto}
     */
    @GetMapping(params = {"page", "size"})
    public ResponseEntity<NewsSimpleDtoList> findAllNews(@RequestParam Integer page,
                                                         @RequestParam Integer size) {
        return new ResponseEntity<>(newsFacade.findAll(page, size), HttpStatus.OK);
    }

    /**
     * Method which shows {@link News} with specified id
     *
     * @param id {@link News} objects id for search
     * @return {@link NewsDto} object or Http Code NOT_FOUND(404)
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<NewsDto> findNewsById(@PathVariable Long id) {
        return newsFacade.findById(id)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    /**
     * Method which shows {@link News} with specified id and pageable comments
     *
     * @param id   {@link News} objects id for search
     * @param page page number for comments
     * @param size page size for comments
     * @return {@link NewsDto} object or Http Code NOT_FOUND(404)
     */
    @GetMapping(value = "/{id}", params = {"page", "size"})
    public ResponseEntity<NewsDto> findNewsByIdWithPageComment(@PathVariable Long id,
                                                               @RequestParam Integer page,
                                                               @RequestParam Integer size) {
        return newsFacade.findByIdWithPageComment(id, page, size)
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Method which finds {@link News} objects by text
     *
     * @param text {@link News} object to find object by text
     * @return {@link NewsSimpleDtoList} that have provided text
     */
    @GetMapping(params = {"text"})
    public ResponseEntity<NewsSimpleDtoList> findNewsByText(@RequestParam String text) {
        return new ResponseEntity<>(newsFacade.findByText(text), HttpStatus.OK);
    }

    /**
     * Method which finds {@link News} objects by text partially contains param
     *
     * @param textLike {@link News} object to find object by text
     * @return {@link NewsSimpleDtoList} that have provided text
     */
    @GetMapping(params = {"textLike"})
    public ResponseEntity<NewsSimpleDtoList> findNewsByTextLike(@RequestParam String textLike) {
        return new ResponseEntity<>(newsFacade.findByTextContains(textLike), HttpStatus.OK);
    }

    /**
     * Method which finds {@link News} objects by text
     *
     * @param title {@link News} object to find object by text
     * @return {@link NewsSimpleDtoList} that have provided text
     */
    @GetMapping(params = {"title"})
    public ResponseEntity<NewsSimpleDtoList> findNewsByTitle(@RequestParam String title) {
        return new ResponseEntity<>(newsFacade.findByTitle(title), HttpStatus.OK);
    }

    /**
     * Method which finds {@link News} objects by text partially contains param
     *
     * @param titleLike {@link News} object to find object by title
     * @return {@link NewsSimpleDtoList} that have provided text
     */
    @GetMapping(params = {"titleLike"})
    public ResponseEntity<NewsSimpleDtoList> findNewsByTitleLike(@RequestParam String titleLike) {
        return new ResponseEntity<>(newsFacade.findByTitleContains(titleLike), HttpStatus.OK);
    }

    /**
     * Method which creates new {@link News}
     *
     * @param news {@link NewsSimpleDto} dto object which needs to created
     * @param user {@link User} that call method
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    @PostMapping
    public ResponseEntity<Void> createNews(@RequestBody NewsSimpleDto news,
                                           @AuthenticationPrincipal User user) {
        newsFacade.save(news, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Method which updates {@link News}
     *
     * @param id   {@link News} from request url
     * @param news {@link NewsSimpleDto} object dto to update
     * @param user {@link User} that call method
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateNews(@PathVariable Long id,
                                           @RequestBody NewsSimpleDto news,
                                           @AuthenticationPrincipal User user) {
        newsFacade.update(id, news, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Method which deletes {@link News} object by id
     *
     * @param id   {@link News} object to delete
     * @param user {@link User} that call method
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id,
                                           @AuthenticationPrincipal User user) {
        newsFacade.deleteById(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
