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
    void shouldGetPostById() throws Exception {
        Post post = setUpPost();
        when(postService.getPostById(1L)).thenReturn(Optional.of(post));

        mockMvc.perform(get("/post/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/post"))
                .andExpect(model().attributeExists("post"));

        verify(postService, times(1)).getPostById(1L);
    }

    @Test
    void shouldGetPostByIdAndSlug() throws Exception {
        Post post = setUpPost();
        when(postService.getPostByIdAndSlug(1L, "title")).thenReturn(Optional.of(post));

        mockMvc.perform(get("/post/1/title"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/post"))
                .andExpect(model().attributeExists("post"));

        verify(postService, times(1)).getPostByIdAndSlug(1L, "title");
    }

    @Test
    void shouldGetPostBySlug() throws Exception {
        Post post = setUpPost();
        when(postService.getPostBySlug("title")).thenReturn(Optional.of(post));

        mockMvc.perform(get("/post/id/title"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/post"))
                .andExpect(model().attributeExists("post"));

        verify(postService, times(1)).getPostBySlug("title");
    }

    @Test
    void shouldThrowNotFoundWithId() throws Exception {
        when(postService.getPostById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/post/1"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    void shouldThrowNotFoundWithSlug() throws Exception {
        when(postService.getPostBySlug("title")).thenReturn(Optional.empty());

        mockMvc.perform(get("/post/id/title"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    void shouldThrowNotFoundWithIdAndSlug() throws Exception {
        when(postService.getPostByIdAndSlug(1L, "title")).thenReturn(Optional.empty());

        mockMvc.perform(get("/post/1/title"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException));
    }

}