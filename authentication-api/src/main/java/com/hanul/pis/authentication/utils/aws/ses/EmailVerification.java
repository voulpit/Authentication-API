package com.hanul.pis.authentication.utils.aws.ses;

import com.hanul.pis.authentication.model.dto.shared.UserDto;

public class EmailVerification implements AmazonSES {
    private static final String SUBJECT = "Inregistrare HANUL PISICILOR";

    private static final String FULL_PATH = "$url/register/$token";

    private static final String HTML_BODY = "<h2>Bine ati venit pe HANUL PISICILOR!</h2>" +
            "<p>Pentru a confirma inregistrarea cu adresa de email, va rugam sa apasati " +
            "<a href='$fullPath'>aici</a>.</p>";

    private static final String TEXT_BODY = "Bine ati venit pe HANUL PISICILOR! " +
            "Pentru a confirma inregistrarea cu adresa de email, va rugam sa apasati pe link-ul alaturat: $fullPath.";

    public void verifyEmail(String url, UserDto userDto) {
        String fullPath = FULL_PATH.replace("$url", url).replace("$token", userDto.getEmailVerificationToken());
        String htmlBody = HTML_BODY.replace("$fullPath", fullPath);
        String textBody = TEXT_BODY.replace("$fullPath", fullPath);
        sendEmail(userDto.getEmail(), SUBJECT, htmlBody, textBody);
    }

    /*
     * Sending emails will work after setting env variables: AWS_ACCESS_KEY, AWS_SECRET_ACCESS_KEY,
     * and moving out from sandbox mode -> production access
     */
}
