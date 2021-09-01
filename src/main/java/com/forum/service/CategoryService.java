package com.forum.service;

import com.forum.exception.ResourceNotFoundException;
import com.forum.model.Category;
import com.forum.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Iterable<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getCategoryBySlug(String slug) throws ResourceNotFoundException {
        return categoryRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    }
}
