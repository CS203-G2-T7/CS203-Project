package com.G2T7.OurGardenStory.User;

import com.G2T7.OurGardenStory.model.ReqResModel.UserSignInRequest;
import com.G2T7.OurGardenStory.model.ReqResModel.UserSignInResponse;
import com.G2T7.OurGardenStory.service.SignInService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SignInServiceTest {

    @InjectMocks
    private SignInService signInService;

    @Test
    void createAuthRequest_newSignInUser_returnSignInUser() {
        UserSignInRequest userSignInRequest = new UserSignInRequest();
        userSignInRequest.setUsername("John");
        userSignInRequest.setPassword("Password123$");
        UserSignInResponse userSignInResponse = signInService.signInFromUserSignInRequest(userSignInRequest);

        assertNotNull(userSignInResponse);
    }

    @Test
    void createSignInRequest_noUsernameOrPassword_throwIllegalArgumentException() {
        UserSignInRequest userSignInRequest = new UserSignInRequest();
        assertThrows(IllegalArgumentException.class, () -> signInService.signInFromUserSignInRequest(userSignInRequest));
    }
}
