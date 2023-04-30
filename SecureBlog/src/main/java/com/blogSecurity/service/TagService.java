package com.blogSecurity.service;

import com.blogSecurity.dto.request.TagRequest;
import com.blogSecurity.dto.response.TagResponse;

import java.util.List;

public interface TagService {
    TagResponse createNewCategory(TagRequest tagRequest);

    String deleteCategory(Long categoryId);

    List<TagResponse> viewCategories();
}
