package com.blogSecurity.postApp.domain.service;

import com.blogSecurity.postApp.application.model.TagRequest;
import com.blogSecurity.postApp.application.model.TagResponse;

import java.util.List;

public interface TagService {
    TagResponse createNewCategory(TagRequest tagRequest);

    String deleteCategory(Long categoryId);

    List<TagResponse> viewCategories();
}
