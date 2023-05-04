package com.blogSecurity.imageApp.application.model;

import com.blogSecurity.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ImageResponse {
    private Long id;
    private String name;
    private String type;
    private String url;
    private Long postId;
    private Status status;
}
