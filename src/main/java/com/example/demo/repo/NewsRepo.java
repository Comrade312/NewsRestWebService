package com.example.demo.repo;

import com.example.demo.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data Access Object class.
 * Provides CRUD and other operations with {@link News} objects.
 */
@Repository
public interface NewsRepo extends JpaRepository<News, Long> {
    /**
     * Find {@link News} object by it's title in data storage
     *
     * @param title {@link News} object's title for search
     * @return found list with {@link News}.
     */
    List<News> findByTitle(String title);

    /**
     * Find {@link News} object by it's title partially contains param
     *
     * @param title {@link News} object's title for search
     * @return found list with {@link News}.
     */
    List<News> findByTitleContains(String title);

    /**
     * Find {@link News} object by it's title in data storage
     *
     * @param text {@link News} object's text for search
     * @return found list with {@link News}.
     */
    List<News> findByText(String text);

    /**
     * Find {@link News} object by it's text partially contains param
     *
     * @param text {@link News} object's text for search
     * @return found list with {@link News}.
     */
    List<News> findByTextContains(String text);
}
