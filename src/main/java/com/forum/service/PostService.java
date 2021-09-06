package com.forum.service;

import com.forum.dto.PostDto;
import com.forum.exception.ResourceNotFoundException;
import com.forum.model.Category;
import com.forum.model.Post;
import com.forum.model.User;
import com.forum.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post getPostByCategoryAndIdAndSlug(Category category, Long id, String postSlug) throws ResourceNotFoundException {
        return postRepository.findByCategoryAndIdAndSlug(category, id, postSlug).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public Post savePost(PostDto postDto, Category category, User principal) {
        Post post = Post.builder()
                .title(postDto.getTitle())
                .postContent(postDto.getPostContent())
                .poster(principal)
                .category(category)
                .timesViewed(0)
                .pinned(false)
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

    public Page<Post> getRecent(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public List<Post> getHotPosts() {
        return postRepository.findTop15Hottest();
    }

    @Transactional
    public void favoritePost(User user, Post post) {
        post.getUsersFavorited().add(user);
    }

    public boolean hasFavoritedPost(User user, Post post) {
        return post.getUsersFavorited().contains(user);
    }

    @Transactional
    public void unfavoritePost(User user, Post post) {
        post.getUsersFavorited().remove(user);
    }

    @Transactional
    public void likePost(User user, Post post) {
        post.getUsersLiked().add(user);
    }

    public boolean hasLikedPost(User user, Post post) {
        return post.getUsersLiked().contains(user);
    }

    @Transactional
    public void unlikePost(User user, Post post) {
        post.getUsersLiked().remove(user);
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    public Page<Post> getFiveRecentPostsFromUser(User user) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        return postRepository.findAllByPoster(user, pageable);
    }

    public Long getNumberOfPostsFromUser(User user) {
        return postRepository.countByPoster(user);
    }
}
