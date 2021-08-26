package com.forum.repository;

import com.forum.model.Category;
import com.forum.model.Post;
import com.forum.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Optional<List<Post>> findByPosterId(Long id);
    Optional<List<Post>> findByPosterUserName(String userName);
    Optional<List<Post>> findByPosterEmail(String email);
    Optional<List<Post>> findByPoster(User user);
    Optional<List<Post>> findByCategory(Category category);
    Optional<List<Post>> findByCategoryName(String name);
    Optional<List<Post>> findByCategoryId(Long id);
    Optional<Post> findByIdAndSlug(Long id, String slug);

    Optional<Post> findBySlug(String slug);

    Optional<Post> findByCategoryAndId(Category category, Long id);

    Optional<Post> findByCategoryAndIdAndSlug(Category category, Long id, String postSlug);

    Page<Post> findAllByCategory(Category category, Pageable pageable);

    @Query(value = "SELECT * , TIMES_VIEWED / ((TIMESTAMPDIFF(HOUR, NOW(), CREATED_AT) + 2) * 1.8) AS HOT FROM POST ORDER BY HOT DESC LIMIT 15", nativeQuery = true)
    List<Post> findTop15Hottest();
}
