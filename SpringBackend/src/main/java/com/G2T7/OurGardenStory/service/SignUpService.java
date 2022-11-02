package com.G2T7.OurGardenStory.service;

import com.G2T7.OurGardenStory.model.ReqResModel.UserSignUpRequest;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class SignUpService {
  @Autowired
  private AWSCognitoIdentityProvider cognitoClient;

  @Autowired
  private UserService userService;

  @Value(value = "${aws.cognito.userPoolId}")
  private String userPoolId;
  @Value(value = "${aws.cognito.clientId}")
  private String clientId;

  public void signUpFromUserSignUpRequest(UserSignUpRequest userSignUpRequest) {
    // TODO: Basic sign up validation. Can be improved. Can have a dedicated user
    // signup validation method.
    LocalDate birthday = LocalDate.parse(userSignUpRequest.getBirthDate(),
        DateTimeFormatter.ofPattern("MM-dd-yyyy"));
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
    userService.createUser(userSignUpRequest);
  }

  private AdminSetUserPasswordRequest userPasswordRequest(UserSignUpRequest userSignUpRequest) {
    AdminSetUserPasswordRequest adminSetUserPasswordRequest = new AdminSetUserPasswordRequest()
        .withUsername(userSignUpRequest.getUsername())
        .withUserPoolId(userPoolId)
        .withPassword(userSignUpRequest.getPassword()).withPermanent(true);

    return adminSetUserPasswordRequest;
  }

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
}
