package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.ReqResModel.*;
import com.G2T7.OurGardenStory.service.*;

import com.amazonaws.AmazonServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.format.DateTimeParseException;

import java.util.Optional;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Api(value = "User Controller", description = "Operations pertaining to User model")
@CrossOrigin(origins = {"https://ourgardenstory.me/", "https://backend.ourgardenstory.me/api/users/sign-in", "https://ourgardenstory.me/login", "*"})
@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    private final UserService userService;
    private final SignInService signInService;
    private final SignUpService signUpService;

    @Autowired
    public UserController(UserService userService, SignInService signInService, SignUpService signUpService) {
        this.userService = userService;
        this.signInService = signInService;
        this.signUpService = signUpService;
    }

    /**
    * This is a sign-up service for new users who do not have an account yet
    * If user is not above 18, or password is less than 8 characters long, throw an exception
    *
    * @param userSignUpRequest which is a UserSignUpRequest object
    * @return a message "'givenName' signed up successfully" if sign up was successful
    */
    @ApiOperation(value = "Signs up a new User")
    @PostMapping(path = "/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        try {
            signUpService.signUpFromUserSignUpRequest(userSignUpRequest);
            return ResponseEntity.ok().body(userSignUpRequest.getGivenName() + " signed up successfully.");
        } catch (IllegalArgumentException | DateTimeParseException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AmazonServiceException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * This is a sign-in service 
    * If an invalid username or password is given, throw IllegalArgumentException
    *
    * @param userSignInRequest which is a UserSignInRequest object
    * @return a UserSignInResponse object if user is successfully signed in
    */
    @ApiOperation(value = "Sign in a registered User")
    @PostMapping(path = "/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserSignInRequest userSignInRequest) {
        try {
            return ResponseEntity.ok(signInService.signInFromUserSignInRequest(userSignInRequest));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (AmazonServiceException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Returns a User object corresponding to the given username
    * If an invalid username is given, throw ResourceNotFoundException
    *
    * @param username an optional String
    * @return a User object if there is an existing user signed up with the given username
    */
    @ApiOperation(value = "Get a User given their Username")
    @GetMapping(path = "/user")
    public ResponseEntity<?> findUser(@RequestParam(name = "username") Optional<String> username) {
        try {
            if (username.isPresent()) {
                return ResponseEntity.ok(userService.findUserByUsername(username.get()));
            }
            return ResponseEntity.ok(userService.findAllUsers());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // user plant endpoints. CRUD for user/plant. myProfile endpoint.
}