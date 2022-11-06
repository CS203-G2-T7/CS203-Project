package com.G2T7.OurGardenStory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import com.amazonaws.auth.*;
import com.amazonaws.services.cognitoidp.*;

@Configuration
public class CognitoConfig {
    @Value(value = "${aws.access-key}")
    private String accessKey;
    @Value(value = "${aws.access-secret}")
    private String secretKey;

    @Bean
    public AWSCognitoIdentityProvider cognitoClient() {

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

        return AWSCognitoIdentityProviderClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("ap-southeast-1")
            .build();
    }
}
