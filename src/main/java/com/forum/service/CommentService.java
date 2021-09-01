package com.forum.service;

import com.forum.model.Category;
import com.forum.model.Comment;
import com.forum.model.Post;
import com.forum.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Page<Comment> getPaginatedSorted(Post post, Pageable pageable) {
        return commentRepository.findAllByPost(post, pageable);
    }
}
