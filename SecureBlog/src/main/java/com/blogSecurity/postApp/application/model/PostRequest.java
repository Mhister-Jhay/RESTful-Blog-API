package com.blogSecurity.postApp.application.model;

import com.blogSecurity.postApp.domain.model.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostRequest {
    @NotBlank(message = "Title must not be blank")
    private String title;
    @NotBlank(message = "Description must not be blank")
    private String description;
    @NotBlank(message = "Content must not be blank")
    private String content;
    private Set<TagRequest> tags;
}
