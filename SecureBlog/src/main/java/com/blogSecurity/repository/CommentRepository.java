package com.blogSecurity.repository;

import com.blogSecurity.enums.Status;
import com.blogSecurity.model.Comment;
import com.blogSecurity.model.Posts;
import com.blogSecurity.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    boolean existsByBodyAndUser(String body, User user);

    Page<Comment> findAllByPostAndStatus(Posts posts, Status approved, Pageable pageable);
}
