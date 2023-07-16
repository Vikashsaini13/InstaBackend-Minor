package com.geekster.InstaBackend.service;


import com.geekster.InstaBackend.model.*;
import com.geekster.InstaBackend.model.dto.SignInInput;
import com.geekster.InstaBackend.model.dto.SignUpOutput;
import com.geekster.InstaBackend.repository.IUserRepo;
import com.geekster.InstaBackend.service.emailUtility.EmailHandler;
import com.geekster.InstaBackend.service.hashingUtility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    IUserRepo userRepo;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    PostService postService;


    public SignUpOutput signUpUser(User user) {

        boolean signUpStatus = true;
        String signUpStatusMessage = null;

        String newEmail = user.getUserEmail();

        if (newEmail == null) {
            signUpStatusMessage = "Invalid email";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus, signUpStatusMessage);
        }

        //check if this user email already exists ??
        User existingUser = userRepo.findFirstByUserEmail(newEmail);

        if (existingUser != null) {
            signUpStatusMessage = "Email already registered!!!";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus, signUpStatusMessage);
        }

        //hash the password: encrypt the password
        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(user.getUserPassword());

            //saveAppointment the user with the new encrypted password

            user.setUserPassword(encryptedPassword);
            userRepo.save(user);

            return new SignUpOutput(signUpStatus, "User registered successfully!!!");
        } catch (Exception e) {
            signUpStatusMessage = "Internal error occurred during sign up";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus, signUpStatusMessage);
        }
    }


    public String signInUser(SignInInput signInInput) {


        String signInStatusMessage = null;

        String signInEmail = signInInput.getEmail();

        if (signInEmail == null) {
            signInStatusMessage = "Invalid email";
            return signInStatusMessage;


        }

        //check if this user email already exists ??
        User existingUser = userRepo.findFirstByUserEmail(signInEmail);

        if (existingUser == null) {
            signInStatusMessage = "Email not registered!!!";
            return signInStatusMessage;

        }

        //match passwords :

        //hash the password: encrypt the password
        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(signInInput.getPassword());
            if (existingUser.getUserPassword().equals(encryptedPassword)) {
                //session should be created since password matched and user id is valid
                AuthenticationToken authToken = new AuthenticationToken(existingUser);
                authenticationService.saveAuthToken(authToken);

                EmailHandler.sendEmail("vs2215727@gmail.com", "email testing", authToken.getTokenValue());
                return "Token sent to your email";
            } else {
                signInStatusMessage = "Invalid credentials!!!";
                return signInStatusMessage;
            }
        } catch (Exception e) {
            signInStatusMessage = "Internal error occurred during sign in";
            return signInStatusMessage;
        }

    }
//    public String createInstaPost(Post post, String email) {
//
//        User postOwner = userRepo.findFirstByUserEmail(email);
//        post.setPostOwner(postOwner);
//        return postService.createInstaPost(post);
//    }
    public String removeInstaPost(Integer postId, String email) {

        User user = userRepo.findFirstByUserEmail(email);
        return postService.removeInstaPost(postId, user);
    }

    public String updateUserDetail(User user) {

        User existingUser= userRepo.findById(user.getUserId()).get();

        existingUser.setUserName(user.getUserName());
        existingUser.setUserBio(user.getUserBio());
        existingUser.setUserHandle(user.getUserHandle());
        existingUser.setUserEmail(user.getUserEmail());

        userRepo.save(existingUser);

        return "user details update successfully!!!";
    }
}











