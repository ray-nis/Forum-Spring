package com.forum.service;

import com.forum.dto.CommentDto;
import com.forum.model.Category;
import com.forum.model.Comment;
import com.forum.model.Post;
import com.forum.model.User;
import com.forum.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Page<Comment> getPaginatedSorted(Post post, Pageable pageable) {
        return commentRepository.findAllByPost(post, pageable);
    }

    @Transactional
    public void saveComment(CommentDto commentDto, Post post, User poster) {
        Comment comment = Comment
                .builder()
                .commenter(poster)
                .post(post)
                .commentContent(commentDto.getCommentContent())
                .build();

        commentRepository.save(comment);
    }
}