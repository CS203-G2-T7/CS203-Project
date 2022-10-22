package com.G2T7.OurGardenStory.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequest {
    private String firstName;
    private String lastName;
    private String DOB;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private String username;
    

}
