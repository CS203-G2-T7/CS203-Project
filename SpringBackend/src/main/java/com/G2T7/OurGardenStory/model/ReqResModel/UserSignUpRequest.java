package com.G2T7.OurGardenStory.model.ReqResModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequest {
    private String username;
    private String email;
    private String password;
    private String address;
    private String birthDate;
    private String phoneNumber;
    private String givenName;
    private String familyName;

}
