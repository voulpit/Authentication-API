package com.hanul.pis.authentication.utils.aws.ses;

import com.amazonaws.services.simpleemail.model.SendEmailResult;

public class PasswordReset implements AmazonSES {
    private static final String SUBJECT = "Cererea dvs de schimbare a parolei";

    private static final String FULL_PATH = "$url/reset/$token";

    private static final String HTML_BODY = "<h3>Salut, $name!</h3>" +
            "<p>Pentru a va schimba parola, va rugam sa apasati " +
            "<a href='$fullPath'>aici</a>.</p>" +
            "<p>Daca nu ati initiat schimbarea, va rugam sa ignorati acest mesaj.</p>";

    private static final String TEXT_BODY = "Salut, $name! " +
            "Pentru a va schimba parola, va rugam sa apasati pe link-ul alaturat: $fullPath. " +
            "Daca nu ati initiat schimbarea, va rugam sa ignorati acest mesaj.";

    public boolean resetPassword(String url, String firstName, String email, String token) {
        String fullPath = FULL_PATH.replace("$url", url).replace("$token", token);
        String htmlBody = HTML_BODY.replace("$fullPath", fullPath).replace("$name", firstName);
        String textBody = TEXT_BODY.replace("$fullPath", fullPath).replace("$name", firstName);
        SendEmailResult result = sendEmail(email, SUBJECT, htmlBody, textBody);
       return result != null && result.getMessageId() != null && !result.getMessageId().isEmpty();
    }
}
