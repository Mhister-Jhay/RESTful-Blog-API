package com.blogSecurity.service;

import com.blogSecurity.dto.request.CategoryRequest;
import com.blogSecurity.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createNewCategory(CategoryRequest categoryRequest);

    String deleteCategory(Long categoryId);

    List<CategoryResponse> viewCategories();
}
