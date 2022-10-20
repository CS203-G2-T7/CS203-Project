package com.G2T7.OurGardenStory.controller;

import java.util.HashMap;
import java.util.Map;

import com.G2T7.OurGardenStory.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.cognitoidp.model.DeliveryMediumType;
import com.amazonaws.services.cognitoidp.model.InvalidParameterException;
import com.amazonaws.services.cognitoidp.model.MessageActionType;
import com.G2T7.OurGardenStory.exception.CustomException;

@CrossOrigin("*") // makes this api callable by other locally run servers, specifically,
                  // "localhost:3000", the frontend app.
@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    @Autowired
    private AWSCognitoIdentityProvider cognitoClient;

    @Value(value = "${aws.cognito.userPoolId}")
    private String userPoolId;
    @Value(value = "${aws.cognito.clientId}")
    private String clientId;

    @PostMapping(path = "/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        try {

            AttributeType emailAttr = new AttributeType().withName("email")
                    .withValue(userSignUpRequest.getEmail());
            AttributeType emailVerifiedAttr = new AttributeType().withName("email_verified")
                    .withValue("true");
            AttributeType addressAttr = new AttributeType().withName("address")
                    .withValue(userSignUpRequest.getAddress());
            AttributeType givenNameAttr = new AttributeType().withName("given_name")
                    .withValue(userSignUpRequest.getGivenName());
            AttributeType familyNameAttr = new AttributeType().withName("family_name")
                    .withValue(userSignUpRequest.getFamilyName());
            AttributeType birthDateAttr = new AttributeType().withName("birthdate")
                    .withValue(userSignUpRequest.getBirthDate());
            AttributeType phoneNumberAttr = new AttributeType().withName("phone_number")
                    .withValue(userSignUpRequest.getPhoneNumber());
            AttributeType phoneNumberVerifiedAttr = new AttributeType().withName("phone_number_verified")
                    .withValue("true");
            AdminCreateUserRequest userRequest = new AdminCreateUserRequest()
                    .withUserPoolId(userPoolId).withUsername(userSignUpRequest.getUsername()) //username
                    .withTemporaryPassword(userSignUpRequest.getPassword()) //password
                    .withUserAttributes(emailAttr, emailVerifiedAttr, addressAttr, givenNameAttr,
                            familyNameAttr,
                            birthDateAttr, phoneNumberAttr, phoneNumberVerifiedAttr)
                    .withMessageAction(MessageActionType.SUPPRESS)
                    .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL);

            AdminCreateUserResult createUserResult = cognitoClient.adminCreateUser(userRequest);

            System.out.println("User " + createUserResult.getUser().getUsername()
                    + " is created. Status: " + createUserResult.getUser().getUserStatus());

            System.out.println("User address is " + userSignUpRequest.getAddress());
            System.out.println("User Given name is " + userSignUpRequest.getGivenName());
            System.out.println("User Family name is " + userSignUpRequest.getFamilyName());
            System.out.println("User Date of Birth is " + userSignUpRequest.getBirthDate());
            System.out.println("User phone number is " + userSignUpRequest.getPhoneNumber());
            AdminSetUserPasswordRequest adminSetUserPasswordRequest = new AdminSetUserPasswordRequest()
                    .withUsername(userSignUpRequest.getUsername())
                    .withUserPoolId(userPoolId)
                    .withPassword(userSignUpRequest.getPassword()).withPermanent(true);

            cognitoClient.adminSetUserPassword(adminSetUserPasswordRequest);

        } catch (AWSCognitoIdentityProviderException e) {
            System.out.println(e.getErrorMessage());
            return new ResponseEntity<>(e.getErrorMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("Error: ");
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping(path = "/sign-in")
    public @ResponseBody UserSignInResponse signIn(
            @RequestBody UserSignInRequest userSignInRequest) {

        UserSignInResponse userSignInResponse = new UserSignInResponse();

        final Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", userSignInRequest.getUsername());
        authParams.put("PASSWORD", userSignInRequest.getPassword());

        final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();
        authRequest.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).withClientId(clientId)
                .withUserPoolId(userPoolId).withAuthParameters(authParams);

        try {
            AdminInitiateAuthResult result = cognitoClient.adminInitiateAuth(authRequest);

            AuthenticationResultType authenticationResult = null;

            if (result.getChallengeName() != null && !result.getChallengeName().isEmpty()) {

                System.out.println("Challenge Name is " + result.getChallengeName());

                if (result.getChallengeName().contentEquals("NEW_PASSWORD_REQUIRED")) {
                    if (userSignInRequest.getPassword() == null) {
                        throw new CustomException(
                                "User must change password "
                                        + result.getChallengeName());

                    } else {

                        final Map<String, String> challengeResponses = new HashMap<>();
                        challengeResponses.put("USERNAME", userSignInRequest.getUsername());
                        challengeResponses.put("PASSWORD", userSignInRequest.getPassword());
                        // add new password
                        challengeResponses.put("NEW_PASSWORD",
                                userSignInRequest.getNewPassword());

                        final AdminRespondToAuthChallengeRequest request = new AdminRespondToAuthChallengeRequest()
                                .withChallengeName(
                                        ChallengeNameType.NEW_PASSWORD_REQUIRED)
                                .withChallengeResponses(challengeResponses)
                                .withClientId(clientId).withUserPoolId(userPoolId)
                                .withSession(result.getSession());

                        AdminRespondToAuthChallengeResult resultChallenge = cognitoClient
                                .adminRespondToAuthChallenge(request);
                        authenticationResult = resultChallenge.getAuthenticationResult();

                        userSignInResponse
                                .setAccessToken(authenticationResult.getAccessToken());
                        userSignInResponse.setIdToken(authenticationResult.getIdToken());
                        userSignInResponse.setRefreshToken(
                                authenticationResult.getRefreshToken());
                        userSignInResponse.setExpiresIn(authenticationResult.getExpiresIn());
                        userSignInResponse.setTokenType(authenticationResult.getTokenType());
                    }

                } else {
                    throw new CustomException(
                            "User has other challenge " + result.getChallengeName());
                }
            } else {

                System.out.println("User has no challenge");
                authenticationResult = result.getAuthenticationResult();

                userSignInResponse.setAccessToken(authenticationResult.getAccessToken());
                userSignInResponse.setIdToken(authenticationResult.getIdToken());
                userSignInResponse.setRefreshToken(authenticationResult.getRefreshToken());
                userSignInResponse.setExpiresIn(authenticationResult.getExpiresIn());
                userSignInResponse.setTokenType(authenticationResult.getTokenType());
            }

        } catch (InvalidParameterException e) {
            throw new CustomException(e.getErrorMessage());
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        cognitoClient.shutdown();
        return userSignInResponse;

    }

    @GetMapping(path = "/detail")
    public @ResponseBody ResponseEntity<?> getUserDetail() {

        return ResponseEntity.ok("User is authenticated!");
    }
}
