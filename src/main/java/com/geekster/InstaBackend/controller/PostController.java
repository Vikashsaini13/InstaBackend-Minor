package com.geekster.InstaBackend.controller;

import com.geekster.InstaBackend.model.Post;
import com.geekster.InstaBackend.model.User;
import com.geekster.InstaBackend.repository.IUserRepo;
import com.geekster.InstaBackend.service.AuthenticationService;
import com.geekster.InstaBackend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    IUserRepo userRepo;

    //get all post

    @GetMapping("posts")
    public List<Post> getAllPosts(){
        return  postService.getAllPosts();
    }


//    content on instagram

    @PostMapping("post")
    public String createInstaPost(@RequestBody Post post, @RequestParam String email, @RequestParam String token)
    {
        if(authenticationService.authenticate(email,token)) {
            User postOwner = userRepo.findFirstByUserEmail(email);
            post.setPostOwner(postOwner);
            return postService.createInstaPost(post);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }
    }


}
