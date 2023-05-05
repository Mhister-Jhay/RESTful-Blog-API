package com.blogSecurity.postApp.application.controller;

import com.blogSecurity.constant.PageConstant;
import com.blogSecurity.constant.PageResponse;
import com.blogSecurity.postApp.application.model.PostRequest;
import com.blogSecurity.postApp.application.model.PostResponse;
import com.blogSecurity.postApp.application.model.TagRequest;
import com.blogSecurity.postApp.application.model.TagResponse;
import com.blogSecurity.postApp.domain.service.PostServiceImpl;
import com.blogSecurity.postApp.domain.service.TagServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/post")
@RequiredArgsConstructor
public class PostController {
    private final PostServiceImpl postServiceImpl;
    private final TagServiceImpl tagServiceImpl;
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/create-post")
    public ResponseEntity<PostResponse> createNewPost(@Valid @RequestBody PostRequest postRequest){
        return new ResponseEntity<>(postServiceImpl.createNewPost(postRequest), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/edit-post/{postId}")
    public ResponseEntity<PostResponse> editPost(@Valid @RequestBody PostRequest postRequest, @PathVariable Long postId){
        return new ResponseEntity<>(postServiceImpl.editPost(postId,postRequest),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/one-post/{postId}")
    public ResponseEntity<PostResponse> getSinglePost(@PathVariable Long postId){
        return new ResponseEntity<>(postServiceImpl.getSinglePost(postId), HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId){
        return new ResponseEntity<>(postServiceImpl.deletePostUser(postId), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/view-by-tag/{tag}")
    public ResponseEntity<PageResponse> viewPostsByTag(
            @RequestParam(value = "pageNo", defaultValue = PageConstant.pageNo) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PageConstant.pageSize) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PageConstant.sortBy) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PageConstant.sortDir) String sortDir,
            @PathVariable String tag){
        return new ResponseEntity<>(postServiceImpl.viewPostsByTag(
                pageNo,pageSize,sortBy,sortDir,tag),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/view-by-user/{userId}")
    public ResponseEntity<PageResponse> viewPostByUSer(
            @RequestParam(value = "pageNo", defaultValue = PageConstant.pageNo) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PageConstant.pageSize) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PageConstant.sortBy) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PageConstant.sortDir) String sortDir,
            @PathVariable Long userId){
        return new ResponseEntity<>(postServiceImpl.viewPostsByUser(
                pageNo,pageSize,sortBy,sortDir,userId),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/view-all")
    public ResponseEntity<PageResponse> viewAllPost(
            @RequestParam(value = "pageNo", defaultValue = PageConstant.pageNo) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PageConstant.pageSize) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PageConstant.sortBy) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PageConstant.sortDir) String sortDir){
        return new ResponseEntity<>(postServiceImpl.viewAllPost(
                pageNo,pageSize,sortBy,sortDir), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/view-categories")
    public ResponseEntity<List<TagResponse>> viewCategories(){
        return new ResponseEntity<>(tagServiceImpl.viewCategories(), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping("/add-tag/{postId}/")
    public ResponseEntity<PostResponse> addTagToPost(@PathVariable Long postId,
                                                     @RequestParam("name") String name){
        return new ResponseEntity<>(postServiceImpl.addTagToPost(postId,name), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @DeleteMapping("/delete-tag/{postId}")
    public ResponseEntity<PostResponse> removeTagFromPost(@PathVariable Long postId,
                                                          @RequestParam("name") String name){
        return new ResponseEntity<>(postServiceImpl.removeTagFromPost(postId,name),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PatchMapping("/publish-post/{postId}")
    public ResponseEntity<PostResponse> publishPost(@PathVariable Long postId){
        return new ResponseEntity<>(postServiceImpl.publishPost(postId), HttpStatus.ACCEPTED);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PatchMapping("/flag-post/{postId}")
    public ResponseEntity<PostResponse> flagPost(@PathVariable Long postId){
        return new ResponseEntity<>(postServiceImpl.flagPost(postId),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @DeleteMapping("/delete-post-admin/{postId}")
    public ResponseEntity<String> deletePostAdmin(@PathVariable Long postId){
        return new ResponseEntity<>(postServiceImpl.deletePostAdmin(postId),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @GetMapping("/view-status-posts/{status}")
    public ResponseEntity<PageResponse> viewStatusPost(
            @RequestParam(value = "pageNo", defaultValue = PageConstant.pageNo) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PageConstant.pageSize) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PageConstant.sortBy) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PageConstant.sortDir) String sortDir,
            @PathVariable String status){
        return new ResponseEntity<>(postServiceImpl.viewPostsByStatus(
                pageNo,pageSize,sortBy,sortDir,status),HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping("/categories")
    public ResponseEntity<TagResponse> createNewCategory(@Valid @RequestBody TagRequest tagRequest){
        return new ResponseEntity<>(tagServiceImpl.createNewCategory(tagRequest), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        return new ResponseEntity<>(tagServiceImpl.deleteCategory(categoryId),HttpStatus.OK);
    }
}
