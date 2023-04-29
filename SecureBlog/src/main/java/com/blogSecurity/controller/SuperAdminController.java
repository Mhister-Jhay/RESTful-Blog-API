package com.blogSecurity.controller;

import com.blogSecurity.dto.request.CategoryRequest;
import com.blogSecurity.dto.response.CategoryResponse;
import com.blogSecurity.service.impl.CategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/super")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SUPER_ADMIN')")
public class SuperAdminController {
    private final CategoryServiceImpl categoryServiceImpl;

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createNewCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        return new ResponseEntity<>(categoryServiceImpl.createNewCategory(categoryRequest), HttpStatus.CREATED);
    }
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        return new ResponseEntity<>(categoryServiceImpl.deleteCategory(categoryId),HttpStatus.OK);
    }

}
