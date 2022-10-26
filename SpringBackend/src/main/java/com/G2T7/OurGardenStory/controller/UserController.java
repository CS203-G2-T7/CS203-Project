package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.*;
import com.G2T7.OurGardenStory.service.UserService;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.cognitoidp.model.InvalidParameterException;
import com.amazonaws.services.cognitoidp.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        try {
            userService.signUpFromUserSignUpRequest(userSignUpRequest);
            return ResponseEntity.ok().body(userSignUpRequest.getGivenName() + " signed up successfully.");
        } catch (InvalidParameterException | InvalidPasswordException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException | DateTimeParseException | UsernameExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e instanceof AmazonServiceException) {
                AmazonServiceException ase = (AmazonServiceException) e;
                return ResponseEntity.status(ase.getStatusCode()).body(ase.getMessage());
            }
            return ResponseEntity.internalServerError().build();
        }
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