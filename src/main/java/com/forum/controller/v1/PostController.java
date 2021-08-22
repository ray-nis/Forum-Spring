package com.forum.controller.v1;

import com.forum.dto.PostDto;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CategoryService categoryService;

    @GetMapping("/category/{category}/post/{id}/{slug}")
    public String getPost(@PathVariable("category") String categorySlug, @PathVariable("id") Long id, @PathVariable("slug") String postSlug, Model model) {
        Optional<Category> category = categoryService.getCategoryBySlug(categorySlug);
        if (category.isPresent()) {
            Optional<Post> post = postService.getPostByCategoryAndIdAndSlug(category.get(), id, postSlug);
            if (post.isPresent()) {
                model.addAttribute("post", post.get());
                return "post/post";
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/category/{category}/new")
    public String newPost(@PathVariable("category") String categorySlug,Model model) {
        Optional<Category> category = categoryService.getCategoryBySlug(categorySlug);
        if (category.isPresent()) {
            model.addAttribute("post", new PostDto());
            return "post/newPost";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
