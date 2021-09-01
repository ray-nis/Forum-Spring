package com.forum.controller.v1;

import com.forum.exception.ResourceNotFoundException;
import com.forum.model.Category;
import com.forum.model.Post;
import com.forum.model.User;
import com.forum.service.CategoryService;
import com.forum.service.PostService;
import com.forum.service.UserDetailsServiceImpl;
import com.forum.util.CurrentUserUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
    @MockBean
    private AuthenticationFailureHandler loginFailureHandler;
    @MockBean
    private CurrentUserUtil currentUserUtil;

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
        when(postService.getPostByCategoryAndIdAndSlug(any(), any(), any())).thenReturn(post);
        when(categoryService.getCategoryBySlug(any())).thenReturn(new Category());

        mockMvc.perform(get("/category/the-category/post/1/slug"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/post"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void shouldThrowNotFoundCategoryMissing() throws Exception {
        Post post = setUpPost();
        when(categoryService.getCategoryBySlug(any())).thenThrow(new ResourceNotFoundException());
        when(postService.getPostByCategoryAndIdAndSlug(any(), any(), any())).thenReturn(post);

        mockMvc.perform(get("/category/the-category/post/1/slug"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    void shouldThrowNotFoundPostMissing() throws Exception {
        Category category = new Category();
        when(categoryService.getCategoryBySlug(any())).thenReturn(category);
        when(postService.getPostByCategoryAndIdAndSlug(category, 1L, "slug")).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(get("/category/the-category/post/1/slug"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    @WithMockUser
    void shouldGetNewPost() throws Exception {
        when(categoryService.getCategoryBySlug("any")).thenReturn(new Category());

        mockMvc.perform(get("/category/any/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/newPost"))
                .andExpect(model().attributeExists("post"));
    }
}