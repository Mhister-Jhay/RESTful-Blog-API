package com.blogSecurity.dto.response;

import com.blogSecurity.enums.PostStatus;
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
    private Set<CategoryResponse> categories;
    private PostStatus status;
    private UserResponse user;
}
