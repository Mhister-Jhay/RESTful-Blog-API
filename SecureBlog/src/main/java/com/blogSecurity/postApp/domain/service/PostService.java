package com.blogSecurity.postApp.domain.service;

import com.blogSecurity.postApp.application.model.PostRequest;
import com.blogSecurity.constant.PageResponse;
import com.blogSecurity.postApp.application.model.PostResponse;
import org.springframework.transaction.annotation.Transactional;

public interface PostService {
    PostResponse createNewPost(PostRequest postRequest);

    PostResponse addTagToPost(Long postId, String name);

    PostResponse removeTagFromPost(Long postId, String name);

    PostResponse publishPost(Long postId);

    PostResponse editPost(Long postId, PostRequest postRequest);

    PostResponse getSinglePost(Long postId);

    @Transactional
    String deletePostUser(Long postId);

    // Admin
    String deletePostAdmin(Long postId);

    // Admin
    PostResponse flagPost(Long postId);

    // Admin
    PageResponse viewPostsByStatus(int pageNo, int pageSize, String sortBy, String sortDir, String status);

    // User
    PageResponse viewPostsByTag(int pageNo, int pageSize, String sortBy, String sortDir, String tag);

    // User
    PageResponse viewPostsByUser(int pageNo, int pageSize, String sortBy, String sortDir, Long userId);

    // User
    PageResponse viewAllPost(int pageNo, int pageSize, String sortBy, String sortDir);
}
