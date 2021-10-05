package com.example.demo.repo;

import com.example.demo.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data Access Object class.
 * Provides CRUD and other operations with {@link Comment} objects.
 */
@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    /**
     * Find {@link Comment} objects by pages,
     * that tied to {@link com.example.demo.entity.News} object with provided id
     *
     * @param newsId   {@link com.example.demo.entity.News} object to find by news
     * @param pageable {@link Pageable} object
     * @return page of {@link Comment} objects, that tied to {@link com.example.demo.entity.News} object.
     */
    Page<Comment> findByNewsId(Long newsId, Pageable pageable);

    /**
     * Find {@link Comment} object by it's title in data storage
     *
     * @param text {@link Comment} object's text for search
     * @return found list with {@link Comment}.
     */
    List<Comment> findByText(String text);

    /**
     * Find {@link Comment} object by it's text partially contains param
     *
     * @param text {@link Comment} object's text for search
     * @return found list with {@link Comment}.
     */
    List<Comment> findByTextContains(String text);
}
