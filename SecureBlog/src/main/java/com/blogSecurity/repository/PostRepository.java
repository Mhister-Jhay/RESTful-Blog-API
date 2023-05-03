package com.blogSecurity.repository;

import com.blogSecurity.enums.Status;
import com.blogSecurity.model.Posts;
import com.blogSecurity.model.Tag;
import com.blogSecurity.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Posts,Long> {
    boolean existsByTitle(String title);

    Page<Posts> findAllByStatus(Status status, Pageable pageable);

    Page<Posts> findAllByTagsContainingIgnoreCaseAndStatus(Tag mainTag, Status status, Pageable pageable);

    Page<Posts> findAllByUserAndStatus(User user, Status status, Pageable pageable);
}
