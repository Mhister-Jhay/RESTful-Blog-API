package com.blogSecurity.dto.response;

import com.blogSecurity.enums.Status;
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
