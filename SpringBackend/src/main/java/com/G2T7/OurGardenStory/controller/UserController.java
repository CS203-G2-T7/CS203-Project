package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.*;
import com.G2T7.OurGardenStory.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*") // makes this api callable by other locally run servers, specifically,
@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        return userService.signUpFromUserSignUpRequest(userSignUpRequest);
    }

    @PostMapping(path = "/sign-in")
    public @ResponseBody UserSignInResponse signIn(@RequestBody UserSignInRequest userSignInRequest) {
        return userService.signInFromUserSignInRequest(userSignInRequest);
    }
}