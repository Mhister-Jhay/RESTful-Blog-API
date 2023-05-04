package com.blogSecurity.likeApp.domain.repository;

import com.blogSecurity.likeApp.domain.model.Likes;
import com.blogSecurity.postApp.domain.model.Post;
import com.blogSecurity.authApp.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes,Long> {
    boolean existsByPostAndUser(Post post, User user);

}
