package com.blogSecurity.imageApp.domain.model;

import com.blogSecurity.constant.Status;
import com.blogSecurity.postApp.domain.model.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "post_images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;
    @Lob
    @Column(nullable = false,length = 1000)
    private byte[] image;

    @Column(nullable = false)
    private String imageUrl;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id",referencedColumnName = "id")
    private Post post;
    @Enumerated(EnumType.STRING)
    private Status status;
}
