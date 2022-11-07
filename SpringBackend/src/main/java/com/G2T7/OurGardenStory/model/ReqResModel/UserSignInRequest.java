package com.G2T7.OurGardenStory.model.ReqResModel;

import lombok.*;

@Getter
@Setter
public class UserSignInRequest {
    private String username;
    private String email;
    private String password;
    private String newPassword;

}
