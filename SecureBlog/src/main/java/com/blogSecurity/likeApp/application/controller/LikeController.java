package com.blogSecurity.likeApp.application.controller;

import com.blogSecurity.likeApp.domain.service.LikeServiceImpl;
import com.blogSecurity.postApp.application.model.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeServiceImpl likeServiceImpl;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{postId}")
    public ResponseEntity<PostResponse> likePost(@PathVariable Long postId){
        return new ResponseEntity<>(likeServiceImpl.likePost(postId), HttpStatus.CREATED);
    }
}
