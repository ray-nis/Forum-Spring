package com.forum.controller.v1;

import com.forum.model.Category;
import com.forum.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CategoryService categoryService;

    @GetMapping("/")
    public String index(Model model) {
        List<Category> categories = (List<Category>) categoryService.getAll();
        model.addAttribute("categories", categories);
        return "index";
    }
}
