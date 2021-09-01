package com.forum.controller.v1;

import com.forum.exception.ResourceNotFoundException;
import com.forum.model.Category;
import com.forum.model.Post;
import com.forum.service.CategoryService;
import com.forum.service.PostService;
import com.forum.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
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

    @Test
    void shouldGetCategoryBySlug() throws Exception {
        Category category = Category.builder()
                .id(1L)
                .name("name")
                .description("desc")
                .slug("name")
                .posts(new ArrayList<>())
                .build();
        when(categoryService.getCategoryBySlug(any())).thenReturn(category);
        when(postService.getPaginatedSorted(any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/category/name"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/category"))
                .andExpect(model().attributeExists("category"))
                .andExpect(model().attributeExists("posts"));
    }

    @Test
    void shouldThrowNotFound() throws Exception {
        when(categoryService.getCategoryBySlug(any())).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(get("/category/name"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResourceNotFoundException));
    }
}