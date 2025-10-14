package com.springboot.service;

import com.springboot.model.Category;
import com.springboot.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getPopularCategories() {
        return categoryRepository.findTop10Categories();
    }
}