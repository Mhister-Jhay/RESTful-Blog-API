package com.blogSecurity.dto.response;

import com.blogSecurity.enums.Status;
import com.blogSecurity.model.Posts;
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
    private byte[] image;
    private Long postId;
    private Status status;
}
