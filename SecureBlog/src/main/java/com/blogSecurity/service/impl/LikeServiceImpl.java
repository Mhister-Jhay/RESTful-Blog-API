package com.blogSecurity.service.impl;

import com.blogSecurity.constant.UserDetails;
import com.blogSecurity.dto.response.PostResponse;
import com.blogSecurity.enums.AccountStatus;
import com.blogSecurity.enums.Status;
import com.blogSecurity.exception.ResourceApprovalException;
import com.blogSecurity.exception.ResourceNotFoundException;
import com.blogSecurity.exception.RestrictedAccessException;
import com.blogSecurity.model.Likes;
import com.blogSecurity.model.Posts;
import com.blogSecurity.model.User;
import com.blogSecurity.repository.LikeRepository;
import com.blogSecurity.repository.PostRepository;
import com.blogSecurity.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    @Override
    public PostResponse likePost(Long postId){
        User user = UserDetails.getLoggedInUser();
        checkUserStatus(user);
        Posts posts = findPostById(postId);
        checkApprovedStatus(posts);
        if(checkLikeExist(posts,user)){
            posts.setLikeCount(posts.getLikeCount()-1);
        }else{
            likeRepository.save(Likes.builder().post(posts).user(user).build());
            posts.setLikeCount(posts.getLikeCount()+1);
        }
        return modelMapper.map(posts,PostResponse.class);
    }
    private Posts findPostById(Long postId){
        Optional<Posts> optionalPosts = postRepository.findById(postId);
        if(optionalPosts.isEmpty()){
            throw new ResourceNotFoundException("Post with id ("+postId+") does not exist");
        }
        return optionalPosts.get();
    }
    private void checkApprovedStatus(Posts posts){
        if(!posts.getStatus().equals(Status.APPROVED)){
            throw new ResourceApprovalException("Post awaiting approval");
        }
    }
    private void checkUserStatus(User user){
        if(user.getStatus().equals(AccountStatus.BANNED) || user.getStatus().equals(AccountStatus.SUSPENDED)){
            throw new RestrictedAccessException("Account has been restricted from this function");
        }
    }
    private boolean checkLikeExist(Posts posts, User user){
        return likeRepository.existsByPostAndUser(posts, user);
    }
}
