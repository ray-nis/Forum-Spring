package com.forum.service;

import com.forum.model.Post;
import com.forum.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Optional<Post> getPostByIdAndSlug(Long id, String slug) {
        return postRepository.findByIdAndSlug(id, slug);
    }
}