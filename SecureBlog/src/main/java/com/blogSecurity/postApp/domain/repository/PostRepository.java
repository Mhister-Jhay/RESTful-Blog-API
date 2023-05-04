package com.blogSecurity.postApp.domain.repository;

import com.blogSecurity.constant.Status;
import com.blogSecurity.postApp.domain.model.Post;
import com.blogSecurity.postApp.domain.model.Tag;
import com.blogSecurity.authApp.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    boolean existsByTitle(String title);

    Page<Post> findAllByStatus(Status status, Pageable pageable);

    Page<Post> findAllByTagsContainingIgnoreCaseAndStatus(Tag mainTag, Status status, Pageable pageable);

    Page<Post> findAllByUserAndStatus(User user, Status status, Pageable pageable);
}
