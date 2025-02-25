package com.finshot.codetest.app.service.impl;

import com.finshot.codetest.app.service.PostService;
import com.finshot.codetest.domain.model.Post;
import com.finshot.codetest.domain.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.getAllPosts();
    }

    @Override
    public Post getPostById(Long id) {
        Post post = postRepository.getPostById(id);
        if (post == null) {
            throw new RuntimeException("Post not found");
        }
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
        return post;
    }
}
