package com.smart_conatct_managet.help;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    public String sendEmail(String to) {

        final String from = "rakshi7259172962@gmail.com";
        final String subject = "Changing Password using OTP";
        String content = "Your one time password for changing password is : ";
        final String password = "ehcntbqrutmphrrr";

        String OTP = Integer.toString(new Random().nextInt(999999) + 100000);

        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, password);
            }

        });

        session.setDebug(true);

        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            mimeMessage.setText(content.concat(OTP));
            mimeMessage.setFrom(from);
            mimeMessage.setSubject(subject);
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            Transport.send(mimeMessage);

            return OTP;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
