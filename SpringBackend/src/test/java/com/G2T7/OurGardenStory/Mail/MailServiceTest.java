package com.G2T7.OurGardenStory.Mail;

import com.G2T7.OurGardenStory.service.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MailServiceTest {
    @InjectMocks
    MailService mailService;

    @Test
    public void sendSuccessEmail_newSuccessEmail_returnSavedEmail() throws IOException {
        String savedMail = mailService.sendTextEmail("John@email.com", "John",
                "SUCCESS", "Win1_Garden1");

        assertNotNull(savedMail);
    }

    @Test
    public void sendFailEmail_newFailEmail_returnSavedEmail() throws IOException {
        String savedMail = mailService.sendTextEmail("John@email.com", "John",
                "FAIL", "Win1_Garden1");

        assertNotNull(savedMail);
    }

    @Test
    void sendEmail_invalidEmail_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> mailService.sendTextEmail(null, null, null, null));
    }

}
