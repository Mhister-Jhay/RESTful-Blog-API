package com.blogSecurity.service.impl;

import com.blogSecurity.dto.request.CategoryRequest;
import com.blogSecurity.dto.response.CategoryResponse;
import com.blogSecurity.exception.ResourceAlreadyExistException;
import com.blogSecurity.exception.ResourceNotFoundException;
import com.blogSecurity.model.Category;
import com.blogSecurity.repository.CategoryRepository;
import com.blogSecurity.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryResponse createNewCategory(CategoryRequest categoryRequest){
        if(categoryRepository.existsByName(categoryRequest.getName().toUpperCase())){
            throw new ResourceAlreadyExistException("Category with name ("+categoryRequest.getName()+") already exist");
        }
        return mapToCategoryResponse(categoryRepository.save(Category.builder()
                .name(categoryRequest.getName())
                .build()));
    }
    @Override
    public String deleteCategory(Long categoryId){
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if(optionalCategory.isEmpty()){
            throw new ResourceNotFoundException("Category with id ("+categoryId+") does not exist");
        }
        categoryRepository.delete(optionalCategory.get());
        return "Category Deleted Successfully";
    }

    @Override
    public List<CategoryResponse> viewCategories(){
        return categoryRepository.findAll().stream().map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }
    private CategoryResponse mapToCategoryResponse(Category category){
        return modelMapper.map(category,CategoryResponse.class);
    }
}
