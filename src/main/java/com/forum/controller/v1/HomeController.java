package com.forum.controller.v1;

import com.forum.model.Category;
import com.forum.model.Post;
import com.forum.service.CategoryService;
import com.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CategoryService categoryService;
    private final PostService postService;

    @GetMapping("/")
    public String index(Model model) {
        List<Category> categories = (List<Category>) categoryService.getAll();
        model.addAttribute("categories", categories);
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "home/about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "home/contact";
    }

    @GetMapping("/search")
    public String getSearch() {
        return "home/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam String searchWord, RedirectAttributes redirectAttributes) {
        List<Post> posts = postService.getPostsContaining(searchWord);
        redirectAttributes.addFlashAttribute("posts", posts);
        return "redirect:/search";
    }
}
