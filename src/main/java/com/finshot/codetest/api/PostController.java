package com.finshot.codetest.api;

import com.finshot.codetest.app.service.PostService;
import com.finshot.codetest.domain.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public String listPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "posts/post-list";
    }

    @GetMapping("/posts/{id}")
    public String getPostById(Model model, Long id) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "posts/post-detail";
    }

}
