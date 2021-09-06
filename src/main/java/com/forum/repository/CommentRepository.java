package com.forum.repository;

import com.forum.model.Category;
import com.forum.model.Comment;
import com.forum.model.Post;
import com.forum.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPost(Post post, Pageable pageable);

    Page<Comment> findAllByCommenter(User user, Pageable pageable);

    Long countByCommenter(User user);
}
