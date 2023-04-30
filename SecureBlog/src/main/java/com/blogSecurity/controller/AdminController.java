package com.blogSecurity.controller;

import com.blogSecurity.constant.PageConstant;
import com.blogSecurity.dto.response.PageResponse;
import com.blogSecurity.dto.response.PostResponse;
import com.blogSecurity.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final PostServiceImpl postServiceImpl;

    @PostMapping("/posts/{postId}/tags/")
    public ResponseEntity<PostResponse> addTagToPost(@PathVariable Long postId,
                                                     @RequestParam("name") String name){
        return new ResponseEntity<>(postServiceImpl.addTagToPost(postId,name), HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/tags/delete/{postId}")
    public ResponseEntity<PostResponse> removeTagFromPost(@PathVariable Long postId,
                                                          @RequestParam("name") String name){
        return new ResponseEntity<>(postServiceImpl.removeTagFromPost(postId,name),HttpStatus.OK);
    }

    @PatchMapping("/posts/publish/{postId}")
    public ResponseEntity<PostResponse> publishPost(@PathVariable Long postId){
        return new ResponseEntity<>(postServiceImpl.publishPost(postId), HttpStatus.ACCEPTED);
    }

    @PatchMapping("/posts/flag/{postId}")
    public ResponseEntity<PostResponse> flagPost(@PathVariable Long postId){
        return new ResponseEntity<>(postServiceImpl.flagPost(postId),HttpStatus.OK);
    }

    @DeleteMapping("/posts/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId){
        return new ResponseEntity<>(postServiceImpl.deletePostAdmin(postId),HttpStatus.OK);
    }

    @GetMapping("/posts/status/{status}")
    public ResponseEntity<PageResponse> viewStatusPost(
            @RequestParam(value = "pageNo", defaultValue = PageConstant.pageNo) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PageConstant.pageSize) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PageConstant.sortBy) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PageConstant.sortDir) String sortDir,
            @PathVariable String status){
        return new ResponseEntity<>(postServiceImpl.viewPostsByStatus(
                pageNo,pageSize,sortBy,sortDir,status),HttpStatus.OK);
    }
}
