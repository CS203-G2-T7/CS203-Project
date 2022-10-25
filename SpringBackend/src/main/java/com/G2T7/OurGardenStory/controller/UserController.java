package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.*;
import com.G2T7.OurGardenStory.service.UserService;
import com.amazonaws.Response;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*") // makes this api callable by other locally run servers, specifically,
// "localhost:3000", the frontend app.
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

        @GetMapping(path = "/user")
        public ResponseEntity<List<User>> findUser(@RequestParam(name = "username") Optional<String> username) {
                try {
                        if (username.isPresent()) {
                                return ResponseEntity.ok(userService.findUserByUsername(username.get()));
                        }
                        return ResponseEntity.ok(userService.findAllUsers());
                } catch (ResourceNotFoundException e) {
                        System.out.println(e.getMessage());
                        return ResponseEntity.notFound().build();
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return ResponseEntity.internalServerError().build();
                }
        }
}