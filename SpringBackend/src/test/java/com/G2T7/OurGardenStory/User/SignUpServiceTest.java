package com.G2T7.OurGardenStory.User;

import com.G2T7.OurGardenStory.model.ReqResModel.UserSignUpRequest;
import com.G2T7.OurGardenStory.service.SignUpService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SignUpServiceTest {
    @InjectMocks
    private SignUpService signUpService;

    @Test
    void createSignUpRequest_underageUser_throwIllegalArgumentException() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        userSignUpRequest.setUsername("John");
        userSignUpRequest.setPassword("Password1");
        userSignUpRequest.setUsername("JohnTest");
        userSignUpRequest.setEmail("John@email.com");
        userSignUpRequest.setFamilyName("Tan");
        userSignUpRequest.setGivenName("John");
        userSignUpRequest.setPhoneNumber("+6591234567");
        userSignUpRequest.setBirthDate("01-01-2010");
        assertThrows(IllegalArgumentException.class, () -> signUpService.signUpFromUserSignUpRequest(userSignUpRequest));
    }

    @Test
    void createSignUpRequest_passwordLengthTooShort_throwIllegalArgumentException() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        userSignUpRequest.setUsername("John");
        userSignUpRequest.setPassword("Pa");
        userSignUpRequest.setUsername("JohnTest");
        userSignUpRequest.setEmail("John@email.com");
        userSignUpRequest.setFamilyName("Tan");
        userSignUpRequest.setGivenName("John");
        userSignUpRequest.setPhoneNumber("+6591234567");
        userSignUpRequest.setBirthDate("01-01-2000");
        assertThrows(IllegalArgumentException.class, () -> signUpService.signUpFromUserSignUpRequest(userSignUpRequest));
    }
}
