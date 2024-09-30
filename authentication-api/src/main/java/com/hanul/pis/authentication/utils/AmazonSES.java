package com.hanul.pis.authentication.utils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.hanul.pis.authentication.model.dto.shared.UserDto;

public class AmazonSES {
    private static final String FROM = "georgiana.tache@hotmail.com";
    private static final String SUBJECT = "Inregistrare HANUL PISICILOR";

    private static final String FULL_PATH = "$url/authentication/users/email-verification?token=$token"; // TODO replaced with UI ref

    private static final String HTML_BODY = "<h2>Bine ati venit pe HANUL PISICILOR!</h2>" +
            "<p>Pentru a confirma inregistrarea cu adresa de email, va rugam sa apasati " +
            "<a href='$fullPath'>aici</a>.</p>";

    private static final String TEXT_BODY = "Bine ati venit pe HANUL PISICILOR! " +
            "Pentru a confirma inregistrarea cu adresa de email, va rugam sa apasati pe link-ul de mai jos: " +
            "http://$url/email-confirmation?token=$token";

    public void verifyEmail(String url, UserDto userDto) {
        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();
        String fullPath = FULL_PATH.replace("$url", url).replace("$token", userDto.getEmailVerificationToken());
        String htmlBody = HTML_BODY.replace("$fullPath", fullPath);
        String textBody = TEXT_BODY.replace("$$fullPath", fullPath);

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(userDto.getEmail()))
                .withMessage(new Message().withBody(new Body()
                                .withHtml(new Content().withData(htmlBody))
                                .withText(new Content().withData(textBody)))
                        .withSubject(new Content().withData(SUBJECT)))
                .withSource(FROM);
        client.sendEmail(request);
    }

    /*
     * Sending emails will work after setting env variables: AWS_ACCESS_KEY, AWS_SECRET_ACCESS_KEY
     */
}
