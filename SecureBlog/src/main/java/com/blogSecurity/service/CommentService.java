package com.blogSecurity.service;

import com.blogSecurity.dto.request.CommentRequest;
import com.blogSecurity.dto.response.CommentResponse;
import com.blogSecurity.dto.response.PageResponse;

public interface CommentService {
    CommentResponse createNewComment(Long postId, CommentRequest commentRequest);

    // Admin
    CommentResponse flagComment(Long commedtId);

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
