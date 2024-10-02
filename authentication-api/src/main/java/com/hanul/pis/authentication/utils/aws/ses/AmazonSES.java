package com.hanul.pis.authentication.utils.aws.ses;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

public class AmazonSES {
    private static final String FROM = "Hanul Pisicilor <georgiana.tache@hotmail.com>";

    public SendEmailResult sendEmail(String destination, String subject, String htmlBody, String textBody) {
        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build();
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(destination))
                .withMessage(new Message().withBody(new Body()
                                .withHtml(new Content().withData(htmlBody))
                                .withText(new Content().withData(textBody)))
                        .withSubject(new Content().withData(subject)))
                .withSource(FROM);
        return client.sendEmail(request);
    }
}
