package com.forum.controller.v1;

import com.forum.model.Post;
import com.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/post/{id}/{slug}")
    public String getPost(@PathVariable("id") Long id, @PathVariable("slug") String slug, Model model) {
        Optional<Post> post = postService.getPostByIdAndSlug(id, slug);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "post/post";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}