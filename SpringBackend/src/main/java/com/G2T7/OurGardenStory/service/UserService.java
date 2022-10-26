package com.G2T7.OurGardenStory.service;

import com.G2T7.OurGardenStory.exception.CustomException;
import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.model.UserSignInRequest;
import com.G2T7.OurGardenStory.model.UserSignInResponse;
import com.G2T7.OurGardenStory.model.UserSignUpRequest;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AWSCognitoIdentityProvider cognitoClient;

    @Value(value = "${aws.cognito.userPoolId}")
    private String userPoolId;
    @Value(value = "${aws.cognito.clientId}")
    private String clientId;

    private AdminCreateUserRequest createUserRequest(UserSignUpRequest userSignUpRequest) {
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
                .withUserPoolId(userPoolId).withUsername(userSignUpRequest.getUsername())
                .withTemporaryPassword(userSignUpRequest.getPassword())
                .withUserAttributes(emailAttr, emailVerifiedAttr, addressAttr, givenNameAttr, familyNameAttr,
                        birthDateAttr, phoneNumberAttr, phoneNumberVerifiedAttr)
                .withMessageAction(MessageActionType.SUPPRESS)
                .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL);

        return userRequest;
    }

    private AdminSetUserPasswordRequest userPasswordRequest(UserSignUpRequest userSignUpRequest) {
        AdminSetUserPasswordRequest adminSetUserPasswordRequest = new AdminSetUserPasswordRequest()
                .withUsername(userSignUpRequest.getUsername())
                .withUserPoolId(userPoolId)
                .withPassword(userSignUpRequest.getPassword()).withPermanent(true);

        return adminSetUserPasswordRequest;
    }

    public void signUpFromUserSignUpRequest(UserSignUpRequest userSignUpRequest) {
        // TODO: Basic sign up validation. Can be improved. Can have a dedicated user
        // signup validation method.
        LocalDate birthday = LocalDate.parse(userSignUpRequest.getBirthDate(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (birthday.isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("You must be at least 18 years old to sign up.");
        }
        if (userSignUpRequest.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }

        // Cognito SignUp
        AdminCreateUserRequest userRequest = createUserRequest(userSignUpRequest);
        cognitoClient.adminCreateUser(userRequest);
        AdminSetUserPasswordRequest adminSetUserPasswordRequest = userPasswordRequest(userSignUpRequest);
        cognitoClient.adminSetUserPassword(adminSetUserPasswordRequest);

        // Cognito Signup no error, then DynamoDB SignUp
        User newUser = new User("User", userSignUpRequest.getUsername(), userSignUpRequest.getGivenName(),
                userSignUpRequest.getFamilyName(), userSignUpRequest.getUsername(),
                userSignUpRequest.getBirthDate(),
                userSignUpRequest.getEmail(), userSignUpRequest.getAddress(), userSignUpRequest.getPhoneNumber(),
                LocalDate.now().toString(), new ArrayList<String>());
        dynamoDBMapper.save(newUser);
    }

    public UserSignInResponse signInFromUserSignInRequest(UserSignInRequest userSignInRequest) {

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
                        throw new CustomException("User must change password " + result.getChallengeName());
                    } else {
                        final Map<String, String> challengeResponses = new HashMap<>();
                        challengeResponses.put("USERNAME", userSignInRequest.getUsername());
                        challengeResponses.put("PASSWORD", userSignInRequest.getPassword());
                        // add new password
                        challengeResponses.put("NEW_PASSWORD", userSignInRequest.getNewPassword());

                        final AdminRespondToAuthChallengeRequest request = new AdminRespondToAuthChallengeRequest()
                                .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                                .withChallengeResponses(challengeResponses)
                                .withClientId(clientId).withUserPoolId(userPoolId)
                                .withSession(result.getSession());

                        AdminRespondToAuthChallengeResult resultChallenge = cognitoClient
                                .adminRespondToAuthChallenge(request);
                        authenticationResult = resultChallenge.getAuthenticationResult();

                        userSignInResponse.setAccessToken(authenticationResult.getAccessToken());
                        userSignInResponse.setIdToken(authenticationResult.getIdToken());
                        userSignInResponse.setRefreshToken(authenticationResult.getRefreshToken());
                        userSignInResponse.setExpiresIn(authenticationResult.getExpiresIn());
                        userSignInResponse.setTokenType(authenticationResult.getTokenType());
                    }
                } else {
                    throw new CustomException("User has other challenge " + result.getChallengeName());
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

        // cognitoClient.shutdown(); This results in user only signing in once.
        return userSignInResponse;
    }

    private User findUserByPkSk(final String pk, final String sk) {
        return dynamoDBMapper.load(User.class, pk, sk);
    }

    public List<User> findAllUsers() {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":USR", new AttributeValue().withS("User"));
        DynamoDBQueryExpression<User> qe = new DynamoDBQueryExpression<User>()
                .withKeyConditionExpression("PK = :USR").withExpressionAttributeValues(eav);

        PaginatedQueryList<User> foundUserList = dynamoDBMapper.query(User.class, qe);
        if (foundUserList.size() == 0) {
            throw new ResourceNotFoundException("There are no users.");
        }
        return foundUserList;
    }

    public User updateUserPlantId(final User user, final String plantID) {
        user.setPK("User");
        User findUser = findUserByPkSk(user.getPK(), user.getSK());
        if (findUser == null) {
            throw new ResourceNotFoundException("User not found.");
        }
        user.getPlant().add(plantID);
        dynamoDBMapper.save(user);
        return user;
    }

    public List<User> findUserByUsername(final String username) { // queries must always return a paginiated list

        // Build query expression to Query by username
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":USR", new AttributeValue().withS("User"));
        eav.put(":USRNAME", new AttributeValue().withS(username));

        DynamoDBQueryExpression<User> qe = new DynamoDBQueryExpression<User>()
                .withKeyConditionExpression("PK = :USR and SK = :USRNAME ")
                .withExpressionAttributeValues(eav);

        PaginatedQueryList<User> foundUserList = dynamoDBMapper.query(User.class, qe);
        // Check if not found. Should only return a single value.
        if (foundUserList.size() == 0) {
            throw new ResourceNotFoundException("User not found"); // might not be right exception
        }
        return foundUserList;
    }
}
