package com.blogSecurity.commentApp.domain.service;

import com.blogSecurity.constant.UserDetails;
import com.blogSecurity.commentApp.application.model.CommentRequest;
import com.blogSecurity.commentApp.application.model.CommentResponse;
import com.blogSecurity.constant.PageResponse;
import com.blogSecurity.authApp.application.model.AccountStatus;
import com.blogSecurity.constant.Status;
import com.blogSecurity.exception.*;
import com.blogSecurity.commentApp.domain.model.Comment;
import com.blogSecurity.postApp.domain.model.Post;
import com.blogSecurity.authApp.domain.model.User;
import com.blogSecurity.commentApp.domain.repository.CommentRepository;
import com.blogSecurity.postApp.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    // User
    @Override
    public CommentResponse createNewComment(Long postId, CommentRequest commentRequest){
        Post post = findPostById(postId);
        checkPostApprovedStatus(post);
        User user = UserDetails.getLoggedInUser();
        checkUserStatus(user);
        if(commentRepository.existsByBodyAndUser(commentRequest.getBody(),user)){
            throw new ResourceAlreadyExistException("Comment Body must be unique to a User");
        }
        return mapToResponse(commentRepository.save(Comment.builder()
                        .body(commentRequest.getBody())
                        .user(user)
                        .post(post)
                        .status(Status.PENDING)
                        .build()));
    }

    // Admin
    @Override
    public CommentResponse flagComment(Long commentId){
        Comment comment = findCommentById(commentId);
        comment.setStatus(Status.FLAGGED);
        return mapToResponse(commentRepository.save(comment));
    }
    // Admin
    @Override
    public String deleteCommentAdmin(Long commentId){
        Comment comment = findCommentById(commentId);
        checkFlaggedStatus(comment);
        commentRepository.delete(comment);
        return "Comment with id ("+commentId+") deleted successfully";
    }

    // Admin
    @Transactional
    @Override
    public CommentResponse publishComment(Long commentId){
        Comment comment = findCommentById(commentId);
        comment.setStatus(Status.APPROVED);
        Post post = comment.getPost();
        post.setCommentCount(post.getCommentCount() + 1);
        return mapToResponse(commentRepository.save(comment));
    }

    // User
    @Transactional
    @Override
    public CommentResponse editComment(Long commentId, CommentRequest commentRequest){
        Comment comment = findCommentById(commentId);
        checkApprovedStatus(comment);
        User user = UserDetails.getLoggedInUser();
        checkUserStatus(user);
        checkCommentOwner(comment,user);
        comment.setBody(commentRequest.getBody());
        comment.setStatus(Status.MODIFIED);
        Post post = comment.getPost();
        post.setCommentCount(post.getCommentCount()-1);
        return mapToResponse(commentRepository.save(comment));
    }

    // User
    @Override
    public String deleteCommentUser(Long commentId){
        Comment comment = findCommentById(commentId);
        User user = UserDetails.getLoggedInUser();
        checkUserStatus(user);
        checkCommentOwner(comment,user);
        Post post = comment.getPost();
        post.setCommentCount(post.getCommentCount()-1);
        commentRepository.delete(comment);
        return "Comment with id ("+commentId+") deleted successfully";
    }

    // User
    @Override
    public PageResponse getPostComments(int pageNo, int pageSize, String sortBy, String sortDir, Long postId){
        User user = UserDetails.getLoggedInUser();
        checkUserStatus(user);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Post post = findPostById(postId);
        Page<Comment> commentPage = commentRepository.findAllByPostAndStatus(
                post, Status.APPROVED, PageRequest.of(pageNo,pageSize,sort));
        List<CommentResponse> commentResponses = commentPage.getContent()
                .stream().map(this::mapToResponse).toList();
        return getPage(commentResponses,commentPage);
    }

    // Admin
    @Override
    public PageResponse getPostCommentsByStatus(int pageNo, int pageSize, String sortBy, String sortDir, Long postId,String status){
        User user = UserDetails.getLoggedInUser();
        checkUserStatus(user);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Post post = findPostById(postId);
        Page<Comment> commentPage = commentRepository.findAllByPostAndStatus(
                post, Status.valueOf(status.toUpperCase()), PageRequest.of(pageNo,pageSize,sort));
        List<CommentResponse> commentResponses = commentPage.getContent()
                .stream().map(this::mapToResponse).toList();
        return getPage(commentResponses,commentPage);
    }

    private Post findPostById(Long postId){
        Optional<Post> optionalPosts = postRepository.findById(postId);
        if(optionalPosts.isEmpty()){
            throw new ResourceNotFoundException("Post with id ("+postId+") does not exist");
        }
        return optionalPosts.get();
    }
    private Comment findCommentById(Long commentId){
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isEmpty()){
            throw new ResourceNotFoundException("Comment with id ("+commentId+") does not exist");
        }
        return optionalComment.get();
    }
    private void checkUserStatus(User user){
        if(user.getStatus().equals(AccountStatus.BANNED) || user.getStatus().equals(AccountStatus.SUSPENDED)){
            throw new RestrictedAccessException("Account has been restricted from this function");
        }
    }
    private void checkCommentOwner(Comment comment, User user){
        if(!comment.getUser().equals(user)){
            throw new UnauthorizedException("Comment was made by Another User");
        }
    }
    private void checkFlaggedStatus(Comment comment){
        if(!comment.getStatus().equals(Status.FLAGGED)){
            throw new UnauthorizedException("Comment is not flagged, Unable to delete comment");
        }
    }
    private void checkApprovedStatus(Comment comment){
        if(!comment.getStatus().equals(Status.APPROVED)){
            throw new UnauthorizedException("Comment is awaiting approval");
        }
    }
    private void checkPostApprovedStatus(Post post){
        if(!post.getStatus().equals(Status.APPROVED)){
            throw new ResourceApprovalException("Post awaiting approval");
        }
    }
    private CommentResponse mapToResponse(Comment comment){
        return modelMapper.map(comment,CommentResponse.class);
    }
    private PageResponse getPage(List<?> list, Page<?> page){
        return PageResponse.builder()
                .content(list)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .isLast(page.isLast())
                .build();
    }
}
