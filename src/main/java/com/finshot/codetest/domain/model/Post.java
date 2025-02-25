package com.finshot.codetest.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @NotNull
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Column(name = "author", nullable = false, length = 50)
    private String author;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    private int views;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public void incrementViewCount() {
        this.views = this.views + 1;
    }
}
