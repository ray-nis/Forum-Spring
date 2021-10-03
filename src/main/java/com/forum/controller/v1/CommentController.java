package com.forum.controller.v1;

import com.forum.dto.CommentDto;
import com.forum.dto.ReportDto;
import com.forum.exception.AccessDeniedException;
import com.forum.exception.BadTokenException;
import com.forum.exception.ResourceNotFoundException;
import com.forum.model.Category;
import com.forum.model.Comment;
import com.forum.model.Post;
import com.forum.service.CategoryService;
import com.forum.service.CommentService;
import com.forum.service.PostService;
import com.forum.service.ReportService;
import com.forum.util.CurrentUserUtil;
import com.forum.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final PostService postService;
    private final CategoryService categoryService;
    private final CurrentUserUtil currentUserUtil;
    private final CommentService commentService;

    @PostMapping("/category/{category}/post/{id}/{slug}")
    public String postComment(@Valid @ModelAttribute("commentDto") CommentDto commentDto, BindingResult result, @PathVariable("category") String categorySlug, @PathVariable("id") Long id, @PathVariable("slug") String postSlug, @RequestParam("page") Optional<Integer> page, Model model) throws ResourceNotFoundException {
        Category category = categoryService.getCategoryBySlug(categorySlug);
        Post post = postService.getPostByCategoryAndIdAndSlug(category, id, postSlug);

        if (post.isLocked()) {
            throw new AccessDeniedException();
        }

        model.addAttribute("post", post);

        int currentPage = PaginationUtil.getPage(page);

        if (result.hasErrors()) {
            Page<Comment> comments = commentService.getPaginatedSorted(post, currentPage);

            List<Integer> pageNumbers = PaginationUtil.getPageNumbers(comments.getTotalPages());
            model.addAttribute("pageNumbers", pageNumbers);

            model.addAttribute("comments", comments);
            model.addAttribute("commentDto", commentDto);
            model.addAttribute("reportDto", new ReportDto());
            return "post/post";
        }

        commentService.saveComment(commentDto, post, currentUserUtil.getUser());

        Page<Comment> comments = commentService.getPaginatedSorted(post, currentPage);

        List<Integer> pageNumbers = PaginationUtil.getPageNumbers(comments.getTotalPages());
        model.addAttribute("pageNumbers", pageNumbers);

        model.addAttribute("comments", comments);

        return "redirect:/category/" + categorySlug + "/post/" + id + "/" + postSlug + "/?page=" + comments.getTotalPages();
    }
}
