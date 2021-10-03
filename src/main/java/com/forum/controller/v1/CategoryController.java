package com.forum.controller.v1;

import com.forum.exception.ResourceNotFoundException;
import com.forum.model.Category;
import com.forum.model.Post;
import com.forum.service.CategoryService;
import com.forum.service.PostService;
import com.forum.util.PaginationUtil;
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
public class CategoryController {

    private final PostService postService;
    private final CategoryService categoryService;

    @GetMapping("/category/{slug}")
    public String getCategory(@PathVariable("slug") String slug, @RequestParam("page") Optional<Integer> page, Model model) throws ResourceNotFoundException {
        Category category = categoryService.getCategoryBySlug(slug);
        model.addAttribute("category", category);

        int currentPage = PaginationUtil.getPage(page);

        Page<Post> posts = postService.getPaginatedSorted(category, currentPage);
        model.addAttribute("posts", posts);

        List<Integer> pageNumbers = PaginationUtil.getPageNumbers(posts.getTotalPages());
        model.addAttribute("pageNumbers", pageNumbers);

        return "category/category";
    }
}
