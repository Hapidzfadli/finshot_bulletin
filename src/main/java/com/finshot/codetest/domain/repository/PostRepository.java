package com.finshot.codetest.domain.repository;

import com.finshot.codetest.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository {
    List<Post> getAllPosts();
    Post getPostById(Long id);
    void save(Post post);
}
