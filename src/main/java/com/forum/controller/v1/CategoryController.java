package com.forum.controller.v1;

import com.forum.model.Category;
import com.forum.model.Post;
import com.forum.service.CategoryService;
import com.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final PostService postService;
    private final CategoryService categoryService;

    @GetMapping("/category/{slug}")
    public String getCategory(@PathVariable("slug") String slug, @RequestParam("page") Optional<Integer> page, Model model) {
        Optional<Category> category = categoryService.getCategoryBySlug(slug);
        if (category.isPresent()) {
            int currentPage = 1;
            if (page.isPresent() && page.get() > 0) {
                currentPage = page.get();
            }

            PageRequest pageRequest = PageRequest.of(currentPage - 1, 10, Sort.by("pinned").descending().and(Sort.by("createdAt").descending()));
            Page<Post> posts = postService.getPaginatedSorted(category.get(), pageRequest);

            if (posts.getTotalPages() > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, posts.getTotalPages())
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }

            model.addAttribute("category", category.get());
            model.addAttribute("posts", posts);
            return "category/category";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
