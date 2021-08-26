package com.forum.service;

import com.forum.dto.PostDto;
import com.forum.model.Category;
import com.forum.model.Post;
import com.forum.model.User;
import com.forum.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Optional<List<Post>> getAllByCategory(Category category) {
        return postRepository.findByCategory(category);
    }

    public Optional<Post> getPostByCategoryAndIdAndSlug(Category category, Long id, String postSlug) {
        return postRepository.findByCategoryAndIdAndSlug(category, id, postSlug);
    }

    @Transactional
    public Post savePost(PostDto postDto, Category category, User principal) {
        Post post = Post.builder()
                .title(postDto.getTitle())
                .postContent(postDto.getPostContent())
                .poster(principal)
                .category(category)
                .timesViewed(0)
                .build();

        return postRepository.save(post);
    }

    @Transactional
    public void increaseTimesViewed(Post post) {
        post.setTimesViewed(post.getTimesViewed() + 1);
    }

    public Page<Post> getPaginatedSorted(Category category, Pageable pageable) {
        return postRepository.findAllByCategory(category, pageable);
    }

    public Optional<Iterable<Post>> getAll() {
        return Optional.of(postRepository.findAll());
    }

    public Page<Post> getRecent(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public List<Post> getHotPosts() {
        return postRepository.findTop15Hottest();
    }
}
