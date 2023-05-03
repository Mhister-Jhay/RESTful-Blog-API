package com.blogSecurity.service;

import com.blogSecurity.dto.response.ImageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    ImageResponse addImageToPost(Long postId, MultipartFile image) throws IOException;

    ImageResponse approveImage(Long imageId);

    ImageResponse flagImage(Long imageId);

    // Admin
    String deleteImage(Long imageId);

    ResponseEntity<List<ImageResponse>> getPostImages(Long postId);
}
