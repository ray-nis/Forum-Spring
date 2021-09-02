package com.forum.controller.v1;

import com.forum.dto.CommentDto;
import com.forum.dto.EmailDto;
import com.forum.dto.PostDto;
import com.forum.exception.ResourceNotFoundException;
import com.forum.model.Category;
import com.forum.model.Comment;
import com.forum.model.Post;
import com.forum.model.User;
import com.forum.service.CategoryService;
import com.forum.service.CommentService;
import com.forum.service.PostService;
import com.forum.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final CategoryService categoryService;
    private final CurrentUserUtil currentUserUtil;
    private final CommentService commentService;

    @GetMapping("/category/{category}/post/{id}/{slug}")
    public String getPost(@PathVariable("category") String categorySlug, @PathVariable("id") Long id, @PathVariable("slug") String postSlug, @RequestParam("page") Optional<Integer> page, Model model) throws ResourceNotFoundException {
        Category category = categoryService.getCategoryBySlug(categorySlug);
        Post post = postService.getPostByCategoryAndIdAndSlug(category, id, postSlug);

        postService.increaseTimesViewed(post);
        model.addAttribute("post", post);

        int currentPage = 1;
        if (page.isPresent() && page.get() > 0) {
            currentPage = page.get();
        }

        PageRequest pageRequest = PageRequest.of(currentPage - 1, 10, Sort.by("createdAt").ascending());
        Page<Comment> comments = commentService.getPaginatedSorted(post, pageRequest);

        if (comments.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, comments.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("comments", comments);
        model.addAttribute("commentDto", new CommentDto());

        return "post/post";
    }

    @PostMapping("/category/{category}/post/{id}/{slug}")
    public String postComment(@Valid @ModelAttribute("commentDto") CommentDto commentDto, BindingResult result, @PathVariable("category") String categorySlug, @PathVariable("id") Long id, @PathVariable("slug") String postSlug, @RequestParam("page") Optional<Integer> page, Model model) throws ResourceNotFoundException {
        Category category = categoryService.getCategoryBySlug(categorySlug);
        Post post = postService.getPostByCategoryAndIdAndSlug(category, id, postSlug);

        model.addAttribute("post", post);

        int currentPage = 1;
        if (page.isPresent() && page.get() > 0) {
            currentPage = page.get();
        }

        if (result.hasErrors()) {
            PageRequest pageRequest = PageRequest.of(currentPage - 1, 10, Sort.by("createdAt").ascending());
            Page<Comment> comments = commentService.getPaginatedSorted(post, pageRequest);

            if (comments.getTotalPages() > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, comments.getTotalPages())
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            model.addAttribute("comments", comments);
            model.addAttribute("commentDto", commentDto);
            return "post/post";
        }

        commentService.saveComment(commentDto, post, currentUserUtil.getUser());

        PageRequest pageRequest = PageRequest.of(currentPage - 1, 10, Sort.by("createdAt").ascending());
        Page<Comment> comments = commentService.getPaginatedSorted(post, pageRequest);

        if (comments.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, comments.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("comments", comments);

        return "redirect:/category/" + categorySlug + "/post/" + id + "/" + postSlug + "/?page=" + comments.getTotalPages();
    }

    @GetMapping("/category/{category}/post/{id}/{slug}/favorite")
    public ResponseEntity<Object> favoritePost(@PathVariable("category") String categorySlug, @PathVariable("id") Long id, @PathVariable("slug") String postSlug, Model model) throws ResourceNotFoundException {
        Category category = categoryService.getCategoryBySlug(categorySlug);
        Post post = postService.getPostByCategoryAndIdAndSlug(category, id, postSlug);

        User user = currentUserUtil.getUser();
        if (postService.hasFavoritedPost(user, post)) {
            postService.unfavoritePost(user, post);
        }
        else {
            postService.favoritePost(user, post);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{category}/post/{id}/{slug}/like")
    public ResponseEntity<Object> likePost(@PathVariable("category") String categorySlug, @PathVariable("id") Long id, @PathVariable("slug") String postSlug, Model model) throws ResourceNotFoundException {
        Category category = categoryService.getCategoryBySlug(categorySlug);
        Post post = postService.getPostByCategoryAndIdAndSlug(category, id, postSlug);

        User user = currentUserUtil.getUser();
        if (postService.hasLikedPost(user, post)) {
            postService.unlikePost(user, post);
        }
        else {
            postService.likePost(user, post);
        }
        return ResponseEntity.ok().build();
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
    public String newPost(@PathVariable("category") String categorySlug,Model model) throws ResourceNotFoundException {
        Category category = categoryService.getCategoryBySlug(categorySlug);

        model.addAttribute("post", new PostDto());
        return "post/newPost";
    }

    @PostMapping("/category/{category}/new")
    public ModelAndView postNewPost(@Valid @ModelAttribute("post") PostDto postDto, BindingResult result, @PathVariable("category") String categorySlug, Authentication authentication, Model model) throws ResourceNotFoundException {
        Category category = categoryService.getCategoryBySlug(categorySlug);

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("post/newPost", "post", postDto);
            return mav;
        }

        Post post = postService.savePost(postDto, category, (User)authentication.getPrincipal());

        return new ModelAndView("redirect:/category/" + category.getSlug() + "/post/" + post.getId() + "/" + post.getSlug(), "post", post);
    }
}
