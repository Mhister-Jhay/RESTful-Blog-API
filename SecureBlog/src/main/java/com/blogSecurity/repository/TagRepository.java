package com.blogSecurity.repository;

import com.blogSecurity.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String name);

    Optional<Tag> findByName(String name);

    Tag findTagByName(String name);

}
