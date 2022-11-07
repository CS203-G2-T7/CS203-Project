package com.G2T7.OurGardenStory.service;

import com.G2T7.OurGardenStory.model.ReqResModel.*;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SignInService {

  private final AWSCognitoIdentityProvider cognitoClient;

  @Autowired
  public SignInService(AWSCognitoIdentityProvider cognitoClient) {
    this.cognitoClient = cognitoClient;
  }

  @Value(value = "${aws.cognito.userPoolId}")
  private String userPoolId;
  @Value(value = "${aws.cognito.clientId}")
  private String clientId;

  public UserSignInResponse signInFromUserSignInRequest(UserSignInRequest userSignInRequest) {
    if (userSignInRequest.getPassword() == null || userSignInRequest.getPassword().isEmpty()
        || userSignInRequest.getUsername() == null || userSignInRequest.getUsername().isEmpty()) {
      throw new IllegalArgumentException("Username or password is invalid.");
    }
    AdminInitiateAuthRequest authRequest = createAuthRequest(userSignInRequest);
    AdminInitiateAuthResult result = cognitoClient.adminInitiateAuth(authRequest);

    // authenticateChallenge
    if (result.getChallengeName() != null && !result.getChallengeName().isEmpty()
        && result.getChallengeName().contentEquals("NEW_PASSWORD_REQUIRED")) {
      System.out.println("User has challenge");
      return authenticateChallenge(result, userSignInRequest);
    }

    System.out.println("User has no challenge");
    AuthenticationResultType authenticationResult = result.getAuthenticationResult();

    return createUserSignInResponse(authenticationResult);
  }

  private AdminInitiateAuthRequest createAuthRequest(final UserSignInRequest signInRequest) {
    final Map<String, String> authParams = new HashMap<>() {
      {
        put("USERNAME", signInRequest.getUsername());
        put("PASSWORD", signInRequest.getPassword());
      }
    };
    return new AdminInitiateAuthRequest()
        .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).withClientId(clientId)
        .withUserPoolId(userPoolId).withAuthParameters(authParams);
  }

  /*
   * If not going to have any challenges, we can remove this block
   * Or else how to implement forget password? What does browser need to send?
   * Just add an extra "new password" in body?
   */
  private UserSignInResponse authenticateChallenge(AdminInitiateAuthResult authResult,
      UserSignInRequest signInRequest) {
    AdminRespondToAuthChallengeRequest challengeRequest = createChallengeRequest(authResult, signInRequest);
    AdminRespondToAuthChallengeResult challengeResult = cognitoClient
        .adminRespondToAuthChallenge(challengeRequest);
    AuthenticationResultType authenticationResult = challengeResult.getAuthenticationResult();
    
    return createUserSignInResponse(authenticationResult);
  }

  private AdminRespondToAuthChallengeRequest createChallengeRequest(AdminInitiateAuthResult authResult,
      UserSignInRequest signInRequest) {
    final Map<String, String> challengeResponses = new HashMap<>() {
      {
        put("USERNAME", signInRequest.getUsername());
        put("PASSWORD", signInRequest.getPassword());
        put("NEW_PASSWORD", signInRequest.getNewPassword()); // add new password
      }
    };

    return new AdminRespondToAuthChallengeRequest()
        .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
        .withChallengeResponses(challengeResponses)
        .withClientId(clientId).withUserPoolId(userPoolId)
        .withSession(authResult.getSession());
  }

  private UserSignInResponse createUserSignInResponse(AuthenticationResultType authenticationResult) {
    UserSignInResponse userSignInResponse = new UserSignInResponse();
    userSignInResponse.setAccessToken(authenticationResult.getAccessToken());
    userSignInResponse.setIdToken(authenticationResult.getIdToken());
    userSignInResponse.setRefreshToken(authenticationResult.getRefreshToken());
    userSignInResponse.setTokenType(authenticationResult.getTokenType());
    userSignInResponse.setExpiresIn(authenticationResult.getExpiresIn());
    userSignInResponse.setAddress("");

    return userSignInResponse;
  }

}
