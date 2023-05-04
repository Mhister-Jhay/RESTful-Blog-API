package com.blogSecurity.commentApp.domain.service;

import com.blogSecurity.commentApp.application.model.CommentRequest;
import com.blogSecurity.commentApp.application.model.CommentResponse;
import com.blogSecurity.constant.PageResponse;

public interface CommentService {
    CommentResponse createNewComment(Long postId, CommentRequest commentRequest);

    // Admin
    CommentResponse flagComment(Long commentId);

    // Admin
    String deleteCommentAdmin(Long commentId);

    // Admin
    CommentResponse publishComment(Long commentId);

    // User
    CommentResponse editComment(Long commentId, CommentRequest commentRequest);

    String deleteCommentUser(Long commentId);

    // User
    PageResponse getPostComments(int pageNo, int pageSize, String sortBy, String sortDir, Long postId);

    PageResponse getPostCommentsByStatus(int pageNo, int pageSize, String sortBy, String sortDir, Long postId, String status);
}
