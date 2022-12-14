package com.G2T7.OurGardenStory.service;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import com.sendgrid.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailService {

    @Value(value = "${sendgrid.api.key}")
    private String SENDGRID_API_KEY;

    @Value(value = "${SENDGRID_SUCCESS_TEMPLATE}")
    private String SENDGRID_SUCCESS_TEMPLATE;

    @Value(value = "${SENDGRID_FAIL_TEMPLATE}")
    private String SENDGRID_FAIL_TEMPLATE;

    /**
    * Sends either a success or a fail email to the user of a ballot, depending on their ballot status
    *
    * @param emailTo the email address of the User
    * @param username a String
    * @param status the ballot status of the User
    * @param winId_GardenName the GSI of the ballot
    * @return the response String
    */
    public String sendTextEmail(String emailTo, String username, String status, String winId_GardenName) throws IOException {
        // the sender email should be the same as we used to Create a Single Sender Verification
        Email from = new Email("jinhan.loh.2021@scis.smu.edu.sg");
        String subject = "OurGardenStory Ballot";
        Email to = new Email(emailTo);

        String winId = winId_GardenName.substring(3, 4);
        String gardenName = winId_GardenName.substring(5);

        Personalization personalization = new Personalization();
        personalization.addTo(to);
        personalization.addDynamicTemplateData("username", username);
        personalization.addDynamicTemplateData("winId", winId);
        personalization.addDynamicTemplateData("gardenName", gardenName);
        String templateId = "";

        if (status.equals("SUCCESS")) {
            templateId = SENDGRID_SUCCESS_TEMPLATE;
        } else if (status.equals("FAIL")) {
            templateId = SENDGRID_FAIL_TEMPLATE;
        }

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addPersonalization(personalization);
        mail.setTemplateId(templateId);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            return response.getBody();
        } catch (IOException e) {
            throw e;
        }
    }
}
