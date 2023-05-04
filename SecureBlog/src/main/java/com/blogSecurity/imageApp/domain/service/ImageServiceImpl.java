package com.blogSecurity.imageApp.domain.service;

import com.blogSecurity.constant.UserDetails;
import com.blogSecurity.imageApp.application.model.ImageResponse;
import com.blogSecurity.authApp.application.model.AccountStatus;
import com.blogSecurity.constant.Status;
import com.blogSecurity.exception.*;
import com.blogSecurity.imageApp.domain.model.Image;
import com.blogSecurity.postApp.domain.model.Post;
import com.blogSecurity.authApp.domain.model.User;
import com.blogSecurity.imageApp.domain.repository.ImageRepository;
import com.blogSecurity.postApp.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    // User
    @Override
    public ImageResponse addImageToPost(Long postId, MultipartFile image) throws IOException {
        String name = StringUtils.cleanPath(image.getOriginalFilename());
        String type = image.getContentType();
        checkFileType(type);
        Post post = findPostById(postId);
        User user = UserDetails.getLoggedInUser();
        checkUserStatus(user);
        checkPostOwner(post,user);
        checkIfImageExist(name, post);
        ImageResponse imageResponse = mapToResponse(imageRepository.save(Image.builder()
                .post(post)
                .status(Status.PENDING)
                .name(name)
                .type(type)
                .image(image.getBytes())
                .build()));
        imageResponse.setPostId(post.getId());
        imageResponse.setUrl(ServletUriComponentsBuilder.fromCurrentContextPath().path("/").path(String.valueOf(imageResponse.getId())).toUriString());
        return imageResponse;
    }

    // Admin
    @Override
    public ImageResponse approveImage(Long imageId){
        Image image = findById(imageId);
        image.setStatus(Status.APPROVED);
        ImageResponse imageResponse = mapToResponse(image);
        imageResponse.setPostId(image.getPost().getId());
        return imageResponse;
    }

    // Admin
    @Override
    public ImageResponse flagImage(Long imageId){
        Image image = findById(imageId);
        image.setStatus(Status.FLAGGED);
        ImageResponse imageResponse = mapToResponse(image);
        imageResponse.setPostId(image.getPost().getId());
        return imageResponse;
    }

    // Admin
    @Override
    public String deleteImage(Long imageId){
        Image image = findById(imageId);
        checkFlagStatus(image);
        imageRepository.delete(image);
        return "Image with id ("+imageId+") Deleted Successfully";
    }

    @Override
    public ResponseEntity<List<ImageResponse>> getPostImages(Long postId){
        Post post = findPostById(postId);
        Set<Image> images = post.getImage();
        List<ImageResponse> imageResponses = images.stream().map(this::mapToResponse)
                .toList();
        for(ImageResponse image : imageResponses){
            image.setPostId(post.getId());
        }
        return new ResponseEntity<>(imageResponses, HttpStatus.OK);
    }
    private Image findById(Long imageId){
        Optional<Image> optionalImage = imageRepository.findById(imageId);
        if(optionalImage.isEmpty()){
            throw new ResourceNotFoundException("Image with id ("+imageId+") does not exist");
        }
        return optionalImage.get();
    }

    private void checkFlagStatus(Image image){
        if(!image.getStatus().equals(Status.FLAGGED)){
            throw new RestrictedAccessException("Unable to delete,Image has not yet been flagged");
        }
    }
    private Post findPostById(Long postId){
        Optional<Post> optionalPosts = postRepository.findById(postId);
        if(optionalPosts.isEmpty()){
            throw new ResourceNotFoundException("Post with id ("+postId+") does not exist");
        }
        return optionalPosts.get();
    }
    private void checkPostOwner(Post post, User user){
        if(!post.getUser().equals(user)){
            throw new UnauthorizedException("Post Belongs to Another User");
        }
    }
    private ImageResponse mapToResponse(Image image){
        return modelMapper.map(image,ImageResponse.class);
    }
    private void checkIfImageExist(String name, Post post){
        if(imageRepository.existsByNameAndPost(name, post)){
            throw new ResourceAlreadyExistException("Image with name ("+name+") already attached to post");
        }
    }
    private void checkFileType(String type){
        if(type != null && !type.startsWith("image/")){
            throw new ResourceApprovalException("File is not an image");
        }
    }
    private void checkUserStatus(User user){
        if(user.getStatus().equals(AccountStatus.BANNED) || user.getStatus().equals(AccountStatus.SUSPENDED)){
            throw new RestrictedAccessException("Account has been restricted from this function");
        }
    }
}
