package com.blogSecurity.imageApp.domain.repository;

import com.blogSecurity.imageApp.domain.model.Image;
import com.blogSecurity.postApp.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>{
    boolean existsByNameAndPost(String name, Post post);

}
