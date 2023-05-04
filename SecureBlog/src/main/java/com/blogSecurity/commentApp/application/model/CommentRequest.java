package com.blogSecurity.commentApp.application.model;

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
public class CommentRequest {
    @NotBlank(message = "Comment must not be blank")
    @Size(min = 10, message = "Comment must contain at least 10 characters")
    private String body;
}
