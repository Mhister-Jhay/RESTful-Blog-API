package com.blogSecurity.likeApp.domain.service;

import com.blogSecurity.postApp.application.model.PostResponse;

public interface LikeService {
    PostResponse likePost(Long postId);
}
