package com.finshot.codetest.app.service.impl;

import com.finshot.codetest.app.service.PostService;
import com.finshot.codetest.domain.model.Post;
import com.finshot.codetest.domain.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        return postRepository.getPostById(id);
    }

    @Override
    public List<Post> getRecentPosts(int limit) {
        return postRepository.getAllPosts().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public void incrementViewCount(Long id) {
        Post post = postRepository.getPostById(id);

        if (post != null && !post.isDeleted()) {
            post.incrementViewCount();
            postRepository.save(post);
        }
    }

    @Override
    public Post save(Post post) {

        if(post.getId() == null) {
            post.setCreatedAt(LocalDateTime.now());
            post.setViews(0);
            post.setDeleted(false);
        } else {
            Post existingPost = postRepository.getPostById(post.getId());

            if(existingPost == null || existingPost.isDeleted()){
                throw new IllegalArgumentException("Post not found");
            }

            post.setUpdatedAt(LocalDateTime.now());
        }


        postRepository.save(post);
        return post;
    }

    @Override
    public boolean verifyPassword(Long id, String password) {
        Post post = postRepository.getPostById(id);

        if (post == null) {
            return false;
        }

        return post.getPassword().equals(password);
    }

    @Override
    public void markAsDeleted(Long id) {
        Post post = postRepository.getPostById(id);

        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }

        post.setDeleted(true);
        postRepository.save(post);
    }
}
