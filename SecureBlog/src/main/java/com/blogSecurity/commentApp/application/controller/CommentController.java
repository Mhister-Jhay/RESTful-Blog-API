package com.blogSecurity.commentApp.application.controller;

import com.blogSecurity.commentApp.application.model.CommentRequest;
import com.blogSecurity.commentApp.application.model.CommentResponse;
import com.blogSecurity.commentApp.domain.service.CommentServiceImpl;
import com.blogSecurity.constant.PageConstant;
import com.blogSecurity.constant.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentServiceImpl commentServiceImpl;
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/add-comment/{postId}")
    public ResponseEntity<CommentResponse>  createNewComment(@PathVariable Long postId,
                                                             @Valid @RequestBody CommentRequest commentRequest){
        return new ResponseEntity<>(commentServiceImpl.createNewComment(postId,commentRequest),HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PatchMapping("/edit-comment/{commentId}")
    public ResponseEntity<CommentResponse> editComment(@PathVariable Long commentId,
                                                       @Valid @RequestBody CommentRequest commentRequest){
        return new ResponseEntity<>(commentServiceImpl.editComment(commentId,commentRequest),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/delete-comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId){
        return new ResponseEntity<>(commentServiceImpl.deleteCommentUser(commentId),HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping("/view-all-comment/{postId}")
    public ResponseEntity<PageResponse> getPostComments(
            @RequestParam(value = "pageNo", defaultValue = PageConstant.pageNo) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PageConstant.pageSize) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PageConstant.sortBy) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PageConstant.sortDir) String sortDir,
            @PathVariable Long postId){
        return new ResponseEntity<>(commentServiceImpl.getPostComments(
                pageNo,pageSize,sortBy,sortDir,postId),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PatchMapping("/flag-comment/{commentId}")
    public ResponseEntity<CommentResponse> flagComment(@PathVariable Long commentId){
        return new ResponseEntity<>(commentServiceImpl.flagComment(commentId),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PatchMapping("/publish-comment/{commentId}")
    public ResponseEntity<CommentResponse> publishComment(@PathVariable Long commentId){
        return new ResponseEntity<>(commentServiceImpl.publishComment(commentId),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @DeleteMapping("/delete-comment-admin/{commentId}")
    public ResponseEntity<String> deleteCommentAdmin(@PathVariable Long commentId){
        return new ResponseEntity<>(commentServiceImpl.deleteCommentAdmin(commentId),HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @GetMapping("/view-status-comments")
    public ResponseEntity<PageResponse> getPostCommentsByStatus(
            @RequestParam(value = "pageNo", defaultValue = PageConstant.pageNo) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = PageConstant.pageSize) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PageConstant.sortBy) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PageConstant.sortDir) String sortDir,
            @PathVariable Long postId, @RequestParam("status") String status){
        return new ResponseEntity<>(commentServiceImpl.getPostCommentsByStatus(
                pageNo,pageSize,sortBy,sortDir,postId,status),HttpStatus.OK);
    }
}
