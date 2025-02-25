package com.finshot.codetest.app.service;

import com.finshot.codetest.domain.model.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(Long id);
}
