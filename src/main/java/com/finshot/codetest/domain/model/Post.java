package com.finshot.codetest.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Lob
    private String content;

    @Column(name = "author", nullable = false, length = 10)
    private String author;

    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
