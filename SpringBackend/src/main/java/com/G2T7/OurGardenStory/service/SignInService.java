package com.G2T7.OurGardenStory.service;

import com.G2T7.OurGardenStory.model.ReqResModel.UserSignInRequest;
import com.G2T7.OurGardenStory.model.ReqResModel.UserSignInResponse;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SignInService {
  @Autowired
  private AWSCognitoIdentityProvider cognitoClient;

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

    // cognitoClient.shutdown(); //This results in user only signing in once.
    return new UserSignInResponse(authenticationResult.getAccessToken(),
        authenticationResult.getIdToken(), authenticationResult.getRefreshToken(),
        authenticationResult.getTokenType(), authenticationResult.getExpiresIn(), "");
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

    return new UserSignInResponse(authenticationResult.getAccessToken(),
        authenticationResult.getIdToken(), authenticationResult.getRefreshToken(),
        authenticationResult.getTokenType(), authenticationResult.getExpiresIn(), "");
    // what is address in signInResponse?
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

}
