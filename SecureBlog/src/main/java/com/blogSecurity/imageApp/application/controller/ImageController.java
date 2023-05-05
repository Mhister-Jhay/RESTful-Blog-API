package com.blogSecurity.imageApp.application.controller;

import com.blogSecurity.imageApp.application.model.ImageResponse;
import com.blogSecurity.imageApp.domain.service.ImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageServiceImpl imageServiceImpl;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(value = "/upload/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponse> addImageToPost(@PathVariable Long postId,
                                                        @RequestParam("image") MultipartFile image) throws IOException {
        return new ResponseEntity<>(imageServiceImpl.addImageToPost(postId,image), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/view-all/{postId}")
    public ResponseEntity<List<ImageResponse>> getPostImages(@PathVariable Long postId){
        return imageServiceImpl.getPostImages(postId);
    }
    @GetMapping("/view/{postId}/{imageId}")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long postId, @PathVariable Long imageId){
        return imageServiceImpl.viewSingleImage(postId,imageId);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PatchMapping("/approve-image/{imageId}")
    public ResponseEntity<ImageResponse> approveImage(@PathVariable Long imageId){
        return new ResponseEntity<>(imageServiceImpl.approveImage(imageId), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PatchMapping("/flag-image/{imageId}")
    public ResponseEntity<ImageResponse> flagImage(@PathVariable Long imageId){
        return new ResponseEntity<>(imageServiceImpl.flagImage(imageId), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @DeleteMapping("/delete-image/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId){
        return new ResponseEntity<>(imageServiceImpl.deleteImage(imageId),HttpStatus.OK);
    }

}
