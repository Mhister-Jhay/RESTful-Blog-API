package com.blogSecurity.service;

import com.blogSecurity.dto.response.PostResponse;

public interface LikeService {
    PostResponse likePost(Long postId);
}
