package com.blogSecurity.repository;

import com.blogSecurity.model.Image;
import com.blogSecurity.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>{
    boolean existsByNameAndPost(String name, Posts posts);

}
