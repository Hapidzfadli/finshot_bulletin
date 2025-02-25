package com.finshot.codetest.app.service;

import com.finshot.codetest.domain.model.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(Long id);
    List<Post> getRecentPosts(int limit);
    void incrementViewCount(Long id);
    Post save(Post post);
    boolean verifyPassword(Long id, String password);
    void markAsDeleted(Long id);
}
