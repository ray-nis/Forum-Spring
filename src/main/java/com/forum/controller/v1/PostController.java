package com.forum.controller.v1;

import com.forum.dto.PostDto;
import com.forum.model.Category;
import com.forum.model.Post;
import com.forum.model.User;
import com.forum.service.CategoryService;
import com.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.util.List;
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
                postService.increaseTimesViewed(post.get());
                model.addAttribute("post", post.get());
                return "post/post";
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/post/recent")
    public String getRecentPosts(Model model) {
        PageRequest pageable = PageRequest.of(0, 15, Sort.by("createdAt").descending());
        Page<Post> posts = postService.getRecent(pageable);
        model.addAttribute("posts", posts);
        return "post/recentPosts";
    }

    @GetMapping("/post/hot")
    public String getHotPosts(Model model) {
        List<Post> posts = postService.getHotPosts();
        model.addAttribute("posts", posts);
        return "post/hotPosts";
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

    @PostMapping("/category/{category}/new")
    public ModelAndView postNewPost(@Valid @ModelAttribute("post") PostDto postDto, BindingResult result, @PathVariable("category") String categorySlug, Authentication authentication, Model model) {
        Optional<Category> category = categoryService.getCategoryBySlug(categorySlug);
        if (category.isPresent()) {
            if (result.hasErrors()) {
                ModelAndView mav = new ModelAndView("post/newPost", "post", postDto);
                return mav;
            }

            Post post = postService.savePost(postDto, category.get(), (User)authentication.getPrincipal());

            return new ModelAndView("redirect:/category/" + category.get().getSlug() + "/post/" + post.getId() + "/" + post.getSlug(), "post", post);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
