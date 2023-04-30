package com.blogSecurity.service.impl;

import com.blogSecurity.constant.UserDetails;
import com.blogSecurity.dto.request.PostRequest;
import com.blogSecurity.dto.request.TagRequest;
import com.blogSecurity.dto.response.PageResponse;
import com.blogSecurity.dto.response.PostResponse;
import com.blogSecurity.enums.AccountStatus;
import com.blogSecurity.enums.PostStatus;
import com.blogSecurity.exception.*;
import com.blogSecurity.model.Posts;
import com.blogSecurity.model.Tag;
import com.blogSecurity.model.User;
import com.blogSecurity.repository.TagRepository;
import com.blogSecurity.repository.PostRepository;
import com.blogSecurity.repository.UserRepository;
import com.blogSecurity.service.PostService;
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
        return mapToResponse(postRepository.save(Posts.builder()
                .title(postRequest.getTitle())
                .description(postRequest.getDescription())
                .content(postRequest.getContent())
                .createdAt(saveLocalDate(LocalDateTime.now()))
                .status(PostStatus.PENDING)
                .tags(tags)
                .user(user)
                .build()));
    }
    // Admin
    @Override
    public PostResponse addTagToPost(Long postId, String name){
        Posts posts = findPostById(postId);
        Set<Tag> tags = posts.getTags();
        Tag tag = findTagByName(name);
        tags.add(tag);
        posts.setTags(tags);
        return mapToResponse(postRepository.save(posts));
    }
    // Admin
    @Override
    public PostResponse removeTagFromPost(Long postId, String name){
        Posts posts = findPostById(postId);
        Set<Tag> tags = posts.getTags();
        Tag tag = findTagByName(name);
        tags.remove(tag);
        posts.setTags(tags);
        return mapToResponse(postRepository.save(posts));
    }
    // Admin
    @Override
    public PostResponse publishPost(Long postId){
        Posts posts = findPostById(postId);
        posts.setPublishedAt(saveLocalDate(LocalDateTime.now()));
        posts.setStatus(PostStatus.APPROVED);
        return mapToResponse(postRepository.save(posts));
    }
    // User
    @Override
    public PostResponse editPost(Long postId, PostRequest postRequest){
        User user = UserDetails.getLoggedInUser();
        checkUserStatus(user);
        Posts posts = findPostById(postId);
        checkApprovedStatus(posts);
        checkPostOwner(posts,user);
        posts.setTitle(postRequest.getTitle());
        posts.setDescription(postRequest.getDescription());
        posts.setContent(postRequest.getContent());
        posts.setStatus(PostStatus.MODIFIED);
        return mapToResponse(postRepository.save(posts));
    }
    // User
    @Override
    public PostResponse getSinglePost(Long postId){
        Posts posts = findPostById(postId);
        checkApprovedStatus(posts);
        return mapToResponse(posts);
    }
    //User
    @Transactional
    @Override
    public String deletePostUser(Long postId){
        User user = UserDetails.getLoggedInUser();
        checkUserStatus(user);
        Posts posts = findPostById(postId);
        checkApprovedStatus(posts);
        checkPostOwner(posts,user);
        posts.setTags(null);
        postRepository.delete(posts);
        return "Post with id ("+postId+") deleted Successfully";
    }

    // Admin
    @Transactional
    @Override
    public String deletePostAdmin(Long postId){
        Posts posts = findPostById(postId);
        checkFlaggedStatus(posts);
        posts.setTags(null);
        postRepository.delete(posts);
        return "Post with id ("+postId+") deleted Successfully";
    }
    // Admin
    @Override
    public PostResponse flagPost(Long postId){
        Posts posts = findPostById(postId);
        posts.setStatus(PostStatus.FLAGGED);
        return mapToResponse(postRepository.save(posts));
    }

    // Admin
    @Override
    public PageResponse viewPostsByStatus(int pageNo, int pageSize, String sortBy, String sortDir, String status){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Page<Posts> postsPage = postRepository.findAllByStatus(PostStatus.valueOf(status.toUpperCase()),
                PageRequest.of(pageNo,pageSize,sort));
        List<PostResponse> postResponses = postsPage.getContent().stream().map(this::mapToResponse).toList();
        return getPage(postResponses,postsPage);
    }

    // User
    @Override
    public PageResponse viewPostsByTag(int pageNo, int pageSize, String sortBy, String sortDir, String tag){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Tag mainTag = findTagByName(tag);
        Page<Posts> postsPage = postRepository.findAllByTagsContainingIgnoreCaseAndStatus(mainTag,PostStatus.APPROVED,
                PageRequest.of(pageNo,pageSize,sort));
        List<PostResponse> postResponses = postsPage.getContent().stream().map(this::mapToResponse).toList();
        return getPage(postResponses,postsPage);
    }

    // User
    @Override
    public PageResponse viewPostsByUser(int pageNo, int pageSize, String sortBy, String sortDir, Long userId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        User user = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User with id ("+userId+") does not exists"));
        Page<Posts> postsPage = postRepository.findAllByUserAndStatus(user,PostStatus.APPROVED,
                PageRequest.of(pageNo,pageSize,sort));
        List<PostResponse> postResponses = postsPage.getContent().stream().map(this::mapToResponse).toList();
        return getPage(postResponses,postsPage);
    }

    // User
    @Override
    public PageResponse viewAllPost(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Page<Posts> postsPage = postRepository.findAllByStatus(PostStatus.APPROVED,
                PageRequest.of(pageNo, pageSize, sort));
        List<PostResponse> postResponses = postsPage.getContent().stream().map(this::mapToResponse).toList();
        return getPage(postResponses, postsPage);
    }
    private String saveLocalDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm:ss a");
        return date.format(formatter);
    }

    private PostResponse mapToResponse(Posts posts){
        return modelMapper.map(posts,PostResponse.class);
    }
    private Posts findPostById(Long postId){
        Optional<Posts> optionalPosts = postRepository.findById(postId);
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
    private void checkApprovedStatus(Posts posts){
        if(!posts.getStatus().equals(PostStatus.APPROVED)){
            throw new ResourceApprovalException("Post awaiting approval");
        }
    }
    private void checkFlaggedStatus(Posts posts){
        if(!posts.getStatus().equals(PostStatus.FLAGGED)){
            throw new ResourceApprovalException("Post has not been flagged, Please Flag before deleting");
        }
    }
    private void checkPostOwner(Posts posts, User user){
        if(!posts.getUser().equals(user)){
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
