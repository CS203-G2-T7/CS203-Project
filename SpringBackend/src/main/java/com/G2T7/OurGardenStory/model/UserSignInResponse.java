package com.G2T7.OurGardenStory.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInResponse {
    private String accessToken;
    @Getter
    @Setter
    private static String idToken;
    private String refreshToken;
    private String tokenType;
    private Integer expiresIn;
    private String address;
}