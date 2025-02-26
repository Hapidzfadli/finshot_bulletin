package com.finshot.codetest.domain.repository.impl;

import com.finshot.codetest.domain.mapper.PostMapper;
import com.finshot.codetest.domain.model.Post;
import com.finshot.codetest.domain.repository.PostRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostRepositoryImpl implements PostRepository {

    @Resource
    private PostMapper postMapper;

    @Override
    public List<Post> getAllPosts() {
        return postMapper.getAllPosts();
    }

    @Override
    public Post getPostById(Long id) {
        return postMapper.getPostById(id);
    }

    @Override
    public void save(Post post) {
        postMapper.save(post);
    }

    @Override
    public void incrementViewCount(Long id) {
        postMapper.incrementViewCount(id);
    }
}
