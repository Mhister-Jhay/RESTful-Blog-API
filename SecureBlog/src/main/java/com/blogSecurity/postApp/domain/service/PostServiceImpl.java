package com.blogSecurity.postApp.domain.service;

import com.blogSecurity.constant.UserDetails;
import com.blogSecurity.postApp.application.model.PostRequest;
import com.blogSecurity.postApp.application.model.TagRequest;
import com.blogSecurity.constant.PageResponse;
import com.blogSecurity.postApp.application.model.PostResponse;
import com.blogSecurity.authApp.application.model.AccountStatus;
import com.blogSecurity.constant.Status;
import com.blogSecurity.exception.*;
import com.blogSecurity.postApp.domain.model.Post;
import com.blogSecurity.postApp.domain.model.Tag;
import com.blogSecurity.authApp.domain.model.User;
import com.blogSecurity.postApp.domain.repository.TagRepository;
import com.blogSecurity.postApp.domain.repository.PostRepository;
import com.blogSecurity.authApp.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // User
    @Override
    public PostResponse createNewPost(PostRequest postRequest){
        if(postRepository.existsByTitle(postRequest.getTitle())){
            throw new ResourceAlreadyExistException("Title Already Exist, Please Choose A Different Title");
        }
        User user = UserDetails.getLoggedInUser();
        checkUserStatus(user);
        Set<Tag> tags = new HashSet<>();
        Set<TagRequest> tagRequests = postRequest.getTags();
        for(TagRequest tag : tagRequests){
            Tag mainTag = findTagByName(tag.getName());
            tags.add(mainTag);
        }
        return mapToResponse(postRepository.save(Post.builder()
                .title(postRequest.getTitle())
                .description(postRequest.getDescription())
                .content(postRequest.getContent())
                .createdAt(saveLocalDate(LocalDateTime.now()))
                .status(Status.PENDING)
                .tags(tags)
                .commentCount(0L)
                .user(user)
                .build()));
    }
    // Admin
    @Override
    public PostResponse addTagToPost(Long postId, String name){
        Post post = findPostById(postId);
        Set<Tag> tags = post.getTags();
        Tag tag = findTagByName(name);
        tags.add(tag);
        post.setTags(tags);
        return mapToResponse(postRepository.save(post));
    }
    // Admin
    @Override
    public PostResponse removeTagFromPost(Long postId, String name){
        Post post = findPostById(postId);
        Set<Tag> tags = post.getTags();
        Tag tag = findTagByName(name);
        tags.remove(tag);
        post.setTags(tags);
        return mapToResponse(postRepository.save(post));
    }
    // Admin
    @Override
    public PostResponse publishPost(Long postId){
        Post post = findPostById(postId);
        post.setPublishedAt(saveLocalDate(LocalDateTime.now()));
        post.setStatus(Status.APPROVED);
        return mapToResponse(postRepository.save(post));
    }
    // User
    @Override
    public PostResponse editPost(Long postId, PostRequest postRequest){
        User user = UserDetails.getLoggedInUser();
        checkUserStatus(user);
        Post post = findPostById(postId);
        checkApprovedStatus(post);
        checkPostOwner(post,user);
        post.setTitle(postRequest.getTitle());
        post.setDescription(postRequest.getDescription());
        post.setContent(postRequest.getContent());
        post.setStatus(Status.MODIFIED);
        return mapToResponse(postRepository.save(post));
    }
    // User
    @Override
    public PostResponse getSinglePost(Long postId){
        User user = UserDetails.getLoggedInUser();
        checkBannedUser(user);
        Post post = findPostById(postId);
        checkApprovedStatus(post);
        return mapToResponse(post);
    }
    //User
    @Transactional
    @Override
    public String deletePostUser(Long postId){
        User user = UserDetails.getLoggedInUser();
        checkUserStatus(user);
        Post post = findPostById(postId);
        checkApprovedStatus(post);
        checkPostOwner(post,user);
        post.setTags(null);
        post.setComments(null);
        postRepository.delete(post);
        return "Post with id ("+postId+") deleted Successfully";
    }

    // Admin
    @Transactional
    @Override
    public String deletePostAdmin(Long postId){
        Post post = findPostById(postId);
        checkFlaggedStatus(post);
        post.setTags(null);
        post.setComments(null);
        postRepository.delete(post);
        return "Post with id ("+postId+") deleted Successfully";
    }
    // Admin
    @Override
    public PostResponse flagPost(Long postId){
        Post post = findPostById(postId);
        post.setStatus(Status.FLAGGED);
        return mapToResponse(postRepository.save(post));
    }

    // Admin
    @Override
    public PageResponse viewPostsByStatus(int pageNo, int pageSize, String sortBy, String sortDir, String status){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Page<Post> postsPage = postRepository.findAllByStatus(Status.valueOf(status.toUpperCase()),
                PageRequest.of(pageNo,pageSize,sort));
        List<PostResponse> postResponses = postsPage.getContent().stream().map(this::mapToResponse).toList();
        return getPage(postResponses,postsPage);
    }

    // User
    @Override
    public PageResponse viewPostsByTag(int pageNo, int pageSize, String sortBy, String sortDir, String tag){
        User user = UserDetails.getLoggedInUser();
        checkBannedUser(user);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Tag mainTag = findTagByName(tag);
        Page<Post> postsPage = postRepository.findAllByTagsContainingIgnoreCaseAndStatus(mainTag, Status.APPROVED,
                PageRequest.of(pageNo,pageSize,sort));
        List<PostResponse> postResponses = postsPage.getContent().stream().map(this::mapToResponse).toList();
        return getPage(postResponses,postsPage);
    }

    // User
    @Override
    public PageResponse viewPostsByUser(int pageNo, int pageSize, String sortBy, String sortDir, Long userId) {
        User userLoggedIn = UserDetails.getLoggedInUser();
        checkBannedUser(userLoggedIn);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        User user = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User with id ("+userId+") does not exists"));
        Page<Post> postsPage = postRepository.findAllByUserAndStatus(user, Status.APPROVED,
                PageRequest.of(pageNo,pageSize,sort));
        List<PostResponse> postResponses = postsPage.getContent().stream().map(this::mapToResponse).toList();
        return getPage(postResponses,postsPage);
    }

    // User
    @Override
    public PageResponse viewAllPost(int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = UserDetails.getLoggedInUser();
        checkBannedUser(user);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Page<Post> postsPage = postRepository.findAllByStatus(Status.APPROVED,
                PageRequest.of(pageNo, pageSize, sort));
        List<PostResponse> postResponses = postsPage.getContent().stream().map(this::mapToResponse).toList();
        return getPage(postResponses, postsPage);
    }

    private String saveLocalDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm:ss a");
        return date.format(formatter);
    }

    private PostResponse mapToResponse(Post post){
        return modelMapper.map(post,PostResponse.class);
    }
    private Post findPostById(Long postId){
        Optional<Post> optionalPosts = postRepository.findById(postId);
        if(optionalPosts.isEmpty()){
            throw new ResourceNotFoundException("Post with id ("+postId+") does not exist");
        }
        return optionalPosts.get();
    }
    private void checkUserStatus(User user){
        if(user.getStatus().equals(AccountStatus.BANNED) || user.getStatus().equals(AccountStatus.SUSPENDED)){
            throw new RestrictedAccessException("Account has been restricted from this function");
        }
    }
    private void checkBannedUser(User user){
        if(user.getStatus().equals(AccountStatus.BANNED)){
            throw new RestrictedAccessException("Account has been restricted from this function");
        }
    }
    private void checkApprovedStatus(Post post){
        if(!post.getStatus().equals(Status.APPROVED)){
            throw new ResourceApprovalException("Post awaiting approval");
        }
    }
    private void checkFlaggedStatus(Post post){
        if(!post.getStatus().equals(Status.FLAGGED)){
            throw new ResourceApprovalException("Post has not been flagged, Please Flag before deleting");
        }
    }
    private void checkPostOwner(Post post, User user){
        if(!post.getUser().equals(user)){
            throw new UnauthorizedException("Post Belongs to Another User");
        }
    }
    private Tag findTagByName(String name){
        Optional<Tag> optionalTag = tagRepository.findByName(name.toUpperCase());
        if(optionalTag.isEmpty()){
            throw new ResourceNotFoundException("Tag with name ("+name+") does not exist");
        }
        return optionalTag.get();
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
