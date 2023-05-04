package com.blogSecurity.commentApp.domain.repository;

import com.blogSecurity.constant.Status;
import com.blogSecurity.commentApp.domain.model.Comment;
import com.blogSecurity.postApp.domain.model.Post;
import com.blogSecurity.authApp.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    boolean existsByBodyAndUser(String body, User user);

    Page<Comment> findAllByPostAndStatus(Post post, Status approved, Pageable pageable);
}
