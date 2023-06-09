package com.blogSecurity.postApp.application.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TagRequest {
    @NotBlank(message = "Category must not be blank")
    private String name;
}
