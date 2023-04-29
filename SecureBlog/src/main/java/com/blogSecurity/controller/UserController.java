package com.blogSecurity.controller;

import com.blogSecurity.dto.response.CategoryResponse;
import com.blogSecurity.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final CategoryServiceImpl categoryServiceImpl;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> viewCategories(){
        return new ResponseEntity<>(categoryServiceImpl.viewCategories(), HttpStatus.OK);
    }
}
