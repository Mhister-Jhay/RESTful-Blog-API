package com.blogSecurity.likeApp.domain.service;

import com.blogSecurity.constant.UserDetails;
import com.blogSecurity.postApp.application.model.PostResponse;
import com.blogSecurity.authApp.application.model.AccountStatus;
import com.blogSecurity.constant.Status;
import com.blogSecurity.exception.ResourceApprovalException;
import com.blogSecurity.exception.ResourceNotFoundException;
import com.blogSecurity.exception.RestrictedAccessException;
import com.blogSecurity.likeApp.domain.model.Likes;
import com.blogSecurity.postApp.domain.model.Post;
import com.blogSecurity.authApp.domain.model.User;
import com.blogSecurity.likeApp.domain.repository.LikeRepository;
import com.blogSecurity.postApp.domain.repository.PostRepository;
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
        Post post = findPostById(postId);
        checkApprovedStatus(post);
        if(checkLikeExist(post,user)){
            post.setLikeCount(post.getLikeCount()-1);
        }else{
            likeRepository.save(Likes.builder().post(post).user(user).build());
            post.setLikeCount(post.getLikeCount()+1);
        }
        return modelMapper.map(post,PostResponse.class);
    }
    private Post findPostById(Long postId){
        Optional<Post> optionalPosts = postRepository.findById(postId);
        if(optionalPosts.isEmpty()){
            throw new ResourceNotFoundException("Post with id ("+postId+") does not exist");
        }
        return optionalPosts.get();
    }
    private void checkApprovedStatus(Post post){
        if(!post.getStatus().equals(Status.APPROVED)){
            throw new ResourceApprovalException("Post awaiting approval");
        }
    }
    private void checkUserStatus(User user){
        if(user.getStatus().equals(AccountStatus.BANNED) || user.getStatus().equals(AccountStatus.SUSPENDED)){
            throw new RestrictedAccessException("Account has been restricted from this function");
        }
    }
    private boolean checkLikeExist(Post post, User user){
        return likeRepository.existsByPostAndUser(post, user);
    }
}
