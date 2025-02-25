package com.finshot.codetest.api;

import com.finshot.codetest.app.service.PostService;
import com.finshot.codetest.domain.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PostController {
    @Autowired
    private PostService postService;


    @GetMapping("/")
    public String index(Model model) {
        List<Post> recentPosts = postService.getRecentPosts(5);
        model.addAttribute("recentPosts", recentPosts);
        return "index";
    }

    @GetMapping("/posts")
    public String listPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "posts/post-list";
    }

    @GetMapping("/posts/{id}")
    public String getPostById(Model model, Long id) {
        Post post = postService.getPostById(id);

        if(post == null || post.isDeleted()){
            return "redirect:/posts";
        }

        postService.incrementViewCount(id);

        model.addAttribute("post", post);
        return "posts/post-detail";
    }

    @GetMapping("/posts/create")
    public String showCreateForm() {
        return "posts/post-create";
    }

    @PostMapping("/posts/create")
    public String createPost(@ModelAttribute Post post, RedirectAttributes redirectAttributes) {
        try {
            Post savedPost = postService.save(post);
            redirectAttributes.addFlashAttribute("successMessage", "Post created successfully");
            return "redirect:/posts/" + savedPost.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/posts/create";
        }
    }

    @PostMapping("/posts/{id}/edit")
    public String editPost(@PathVariable("id") Long id, @RequestParam String password,
                           Model model, RedirectAttributes redirectAttributes) {
        if(postService.verifyPassword(id, password)) {
            Post post = postService.getPostById(id);
            model.addAttribute("post", post);
            return "posts/post-edit";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Incorrect password. Unable to edit post.");
            return "redirect:/posts/" + id;
        }
    }

    @PostMapping("/posts/{id}/update")
    public String updatePost(@PathVariable("id") Long id, @ModelAttribute Post post,
                             @RequestParam String password, RedirectAttributes redirectAttributes) {

        try {
            if (postService.verifyPassword(id, password)) {
                postService.save(post);
                redirectAttributes.addFlashAttribute("successMessage", "Post updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Incorrect password. Unable to update post.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating post: " + e.getMessage());
        }

        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable("id") Long id, @RequestParam String password,
                             RedirectAttributes redirectAttributes) {

        try {
            if (postService.verifyPassword(id, password)) {
                postService.markAsDeleted(id);
                redirectAttributes.addFlashAttribute("successMessage", "Post deleted successfully!");
                return "redirect:/posts";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Incorrect password. Unable to delete post.");
                return "redirect:/posts/" + id;
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting post: " + e.getMessage());
            return "redirect:/posts/" + id;
        }
    }
}
