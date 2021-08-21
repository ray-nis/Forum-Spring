package com.forum.controller.v1;

import com.forum.model.Category;
import com.forum.model.Post;
import com.forum.service.CategoryService;
import com.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final PostService postService;
    private final CategoryService categoryService;

    @GetMapping("/category/{slug}")
    public String getCategory(@PathVariable("slug") String slug, Model model) {
        Optional<Category> category = categoryService.getCategoryBySlug(slug);
        if (category.isPresent()) {
            Optional<List<Post>> posts = postService.getAllByCategory(category.get());
            model.addAttribute("category", category.get());
            model.addAttribute("posts", posts.get());
            return "category/category";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
