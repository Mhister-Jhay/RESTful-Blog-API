package com.blogSecurity.model;

import com.blogSecurity.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fullName;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(
                name = "role_id",
                referencedColumnName = "id"
    )
    private Roles role;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @OneToMany(mappedBy = "user",orphanRemoval = true,cascade = CascadeType.REMOVE)
    private Set<Posts> posts;
    @OneToMany(mappedBy = "user",orphanRemoval = true,cascade = CascadeType.REMOVE)
    private Set<Comment> comments;
}