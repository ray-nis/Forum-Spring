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
    Optional<List<Post>> findByCategory(Category category);
    Optional<Post> findByCategoryAndIdAndSlug(Category category, Long id, String postSlug);

    Page<Post> findAllByCategory(Category category, Pageable pageable);

    @Query(value = "SELECT * , \n" +
            "((SELECT COUNT(*) FROM comment WHERE post_id = post.id) + ((SELECT COUNT(*) FROM user_likes WHERE user_likes.post_id = post.id) * 0.5) + (TIMES_VIEWED * 0.3)) / \n" +
            "((TIMESTAMPDIFF(HOUR, CREATED_AT, NOW())) + 0.1) * 1.8 AS HOT\n" +
            "FROM POST \n" +
            "ORDER BY HOT DESC \n" +
            "LIMIT 15", nativeQuery = true)
    List<Post> findTop15Hottest();

    Long countByCategory(Category category);

    Long countByPoster(User user);

    Page<Post> findAllByPoster(User user, Pageable pageable);

    List<Post> findAllByUsersFavorited(User user);

    List<Post> findByTitleContaining(String searchWord);

    List<Post> findByPostContentContaining(String searchWord);
}
