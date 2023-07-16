package com.geekster.InstaBackend.controller;


import com.geekster.InstaBackend.model.*;
import com.geekster.InstaBackend.model.dto.SignInInput;
import com.geekster.InstaBackend.model.dto.SignUpOutput;
import com.geekster.InstaBackend.service.AuthenticationService;
import com.geekster.InstaBackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;


    //sign up, sign in  a particular instagram user
    @PostMapping("user/signup")
    public SignUpOutput signUpInstaUser(@RequestBody User user)
    {

        return userService.signUpUser(user);
    }

    @PostMapping("user/signIn")
    public String sigInInstaUser(@RequestBody @Valid SignInInput signInInput)
    {
        return userService.signInUser(signInInput);
    }



//    //content on instagram
//
//    @PostMapping("post")
//    public String createInstaPost(@RequestBody Post post, @RequestParam String email, @RequestParam String token)
//    {
//        if(authenticationService.authenticate(email,token)) {
//            return userService.createInstaPost(post,email);
//        }
//        else {
//            return "Not an Authenticated user activity!!!";
//        }
//    }

    @DeleteMapping("post")
    public String removeInstaPost(@RequestParam Integer postId, @RequestParam String email, @RequestParam String token)
    {
        if(authenticationService.authenticate(email,token)) {
            return userService.removeInstaPost(postId,email);
        }
        else {
            return "Not an Authenticated user activity!!!";
        }
    }


    //update user details

    @PutMapping("user/detail")
    public String updateUserDetail(User user){
        return userService.updateUserDetail(user);
    }

}


