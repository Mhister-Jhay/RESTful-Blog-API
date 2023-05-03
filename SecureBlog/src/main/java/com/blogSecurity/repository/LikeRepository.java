package com.blogSecurity.repository;

import com.blogSecurity.model.Likes;
import com.blogSecurity.model.Posts;
import com.blogSecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes,Long> {
    boolean existsByPostAndUser(Posts posts, User user);

}
