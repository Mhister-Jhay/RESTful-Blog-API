package com.blogSecurity.controller;

import com.blogSecurity.constant.PageConstant;
import com.blogSecurity.dto.request.CommentRequest;
import com.blogSecurity.dto.request.PostRequest;
import com.blogSecurity.dto.response.CommentResponse;
import com.blogSecurity.dto.response.PageResponse;
import com.blogSecurity.dto.response.PostResponse;
import com.blogSecurity.dto.response.TagResponse;
import com.blogSecurity.service.impl.CommentServiceImpl;
import com.blogSecurity.service.impl.PostServiceImpl;
import com.blogSecurity.service.impl.TagServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final TagServiceImpl tagServiceImpl;
    private final PostServiceImpl postServiceImpl;
    private final CommentServiceImpl commentServiceImpl;
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/categories")
    public ResponseEntity<List<TagResponse>> viewCategories(){
        return new ResponseEntity<>(tagServiceImpl.viewCategories(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/posts")
    public ResponseEntity<PostResponse> createNewPost(@Valid @RequestBody PostRequest postRequest){
        return new ResponseEntity<>(postServiceImpl.createNewPost(postRequest),HttpStatus.CREATED);
    }
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/posts/edit/{postId}")
    public ResponseEntity<PostResponse> editPost(@Valid @RequestBody PostRequest postRequest, @PathVariable Long postId){
        return new ResponseEntity<>(postServiceImpl.editPost(postId,postRequest),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> getSinglePost(@PathVariable Long postId){
        return new ResponseEntity<>(postServiceImpl.getSinglePost(postId), HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/posts/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId){
        return new ResponseEntity<>(postServiceImpl.deletePostUser(postId), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/posts/tags/{tag}")
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
    @GetMapping("/posts/user/{userId}")
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
    @GetMapping("/posts")
    public ResponseEntity<PageResponse> viewAllPost(
            @RequestParam(value = "pageNo", defaultValue = PageConstant.pageNo) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PageConstant.pageSize) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PageConstant.sortBy) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PageConstant.sortDir) String sortDir){
        return new ResponseEntity<>(postServiceImpl.viewAllPost(
                pageNo,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse>  createNewComment(@PathVariable Long postId,
                                                             @Valid @RequestBody CommentRequest commentRequest){
        return new ResponseEntity<>(commentServiceImpl.createNewComment(postId,commentRequest),HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PatchMapping("/posts/comments/{commentId}")
    public ResponseEntity<CommentResponse> editComment(@PathVariable Long commentId,
                                                       @Valid @RequestBody CommentRequest commentRequest){
        return new ResponseEntity<>(commentServiceImpl.editComment(commentId,commentRequest),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/posts/comments/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId){
        return new ResponseEntity<>(commentServiceImpl.deleteCommentUser(commentId),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<PageResponse> getPostComments(
            @RequestParam(value = "pageNo", defaultValue = PageConstant.pageNo) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PageConstant.pageSize) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PageConstant.sortBy) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PageConstant.sortDir) String sortDir,
            @PathVariable Long postId){
        return new ResponseEntity<>(commentServiceImpl.getPostComments(
                pageNo,pageSize,sortBy,sortDir,postId),HttpStatus.OK);
    }
}
