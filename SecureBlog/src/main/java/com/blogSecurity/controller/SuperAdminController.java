package com.blogSecurity.controller;

import com.blogSecurity.postApp.application.model.TagRequest;
import com.blogSecurity.postApp.application.model.TagResponse;
import com.blogSecurity.postApp.domain.service.TagServiceImpl;
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
    private final TagServiceImpl tagServiceImpl;

    @PostMapping("/categories")
    public ResponseEntity<TagResponse> createNewCategory(@Valid @RequestBody TagRequest tagRequest){
        return new ResponseEntity<>(tagServiceImpl.createNewCategory(tagRequest), HttpStatus.CREATED);
    }
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        return new ResponseEntity<>(tagServiceImpl.deleteCategory(categoryId),HttpStatus.OK);
    }

}
