package com.forum.controller.v1;

import com.forum.model.Category;
import com.forum.model.Post;
import com.forum.model.User;
import com.forum.service.CategoryService;
import com.forum.service.PostService;
import com.forum.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
@ExtendWith(MockitoExtension.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private Post setUpPost() {
        Post post = Post.builder()
                .id(1L)
                .title("title")
                .postContent("content")
                .slug("title")
                .poster(new User())
                .category(new Category())
                .build();

        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());

        return post;
    }

    @Test
    void shouldGetPost() throws Exception {
        Post post = setUpPost();
        when(postService.getPostByCategoryAndIdAndSlug(any(), any(), any())).thenReturn(Optional.of(post));
        when(categoryService.getCategoryBySlug(any())).thenReturn(Optional.of(new Category()));

        mockMvc.perform(get("/category/the-category/post/1/slug"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/post"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void shouldThrowNotFoundCategoryMissing() throws Exception {
        Post post = setUpPost();
        when(categoryService.getCategoryBySlug(any())).thenReturn(Optional.empty());
        when(postService.getPostByCategoryAndIdAndSlug(any(), any(), any())).thenReturn(Optional.of(post));

        mockMvc.perform(get("/category/the-category/post/1/slug"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    void shouldThrowNotFoundPostMissing() throws Exception {
        when(categoryService.getCategoryBySlug(any())).thenReturn(Optional.of(new Category()));
        when(postService.getPostById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/category/the-category/post/1/slug"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException));
    }

}