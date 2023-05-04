package com.blogSecurity.commentApp.application.model;

import com.blogSecurity.authApp.application.model.UserResponse;
import com.blogSecurity.constant.Status;
import com.blogSecurity.postApp.application.model.PostResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentResponse {
    private Long id;
    private String body;
    private Status status;
    private UserResponse user;
    private PostResponse post;
}
