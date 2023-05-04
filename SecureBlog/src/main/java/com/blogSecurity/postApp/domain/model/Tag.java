package com.blogSecurity.postApp.domain.model;

import com.blogSecurity.postApp.domain.model.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String name;
    @ManyToMany(mappedBy = "tags", cascade = CascadeType.REMOVE)
    private Set<Post> posts;
}
