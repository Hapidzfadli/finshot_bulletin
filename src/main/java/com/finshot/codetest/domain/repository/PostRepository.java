package com.finshot.codetest.domain.repository;

import com.finshot.codetest.domain.model.Post;

import java.util.List;

public interface PostRepository {
    List<Post> getAllPosts();
    Post getPostById(Long id);
    void save(Post post);
    void incrementViewCount(Long id);
}
