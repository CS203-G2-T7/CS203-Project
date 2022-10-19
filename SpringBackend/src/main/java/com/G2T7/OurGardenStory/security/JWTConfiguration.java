package com.G2T7.OurGardenStory.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.*;

@Getter
@Setter
@Component
public class JWTConfiguration {
    private String userPoolId;

    @Value(value = "${aws.cognito.jwk}")
    private String jwkUrl;

    private String region = "ap-southeast-1";
    private String userNameField = "cognito:username";
    private int connectionTimeout = 2000;
    private int readTimeout = 2000;

    @Value(value = "${aws.cognito.identityPoolId}")
    private String cognitoIdentityPoolUrl;
}
