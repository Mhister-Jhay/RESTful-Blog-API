package com.blogSecurity.postApp.application.model;

import com.blogSecurity.authApp.application.model.UserResponse;
import com.blogSecurity.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostResponse {
    private Long id;
    private String title;
    private String description;
    private String content;
    private String createdAt;
    private String publishedAt;
    private Status status;
    private Long commentCount;
    private Long likeCount;
    private Set<TagResponse> tags;

    private UserResponse user;
}
