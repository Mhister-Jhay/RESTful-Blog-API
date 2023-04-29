package com.blogSecurity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostRequest {
    @NotBlank(message = "Title must not be blank")
    @Size(min = 3, message = "Title must contain at least 3 characters")
    private String title;
    @NotBlank(message = "Description must not be blank")
    @Size(min = 10, message = "Description must contain at least 10 characters")
    private String description;
    @NotBlank(message = "Content must not be blank")
    @Size(min = 10, message = "Content must contain at least 10 characters")
    private String content;
}
