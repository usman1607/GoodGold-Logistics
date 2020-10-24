package com.goodgold.logistics.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@RestController
public class EmailController {

    @Autowired
    private JavaMailSender emailSender;

    @RequestMapping(value = "/sendEmail")
    public String sendEmail(Model model) throws AddressException, MessagingException, IOException {
//        sendmail();
//        sendSimpleMessage("johndoejava2@gmail.com", "Testing", "This is a testing email sent from GGL");
        model.addAttribute("sent", "Email sent successfully");
        return "Email sent successfully";
    }

    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("info@gglogistics.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

//    private void sendmail() throws AddressException, MessagingException, IOException {
//        Properties props = new Properties();
//        props.put("mail.host", "smtp.gmail.com");
//        props.put("mail.port", "587");
//        props.put("mail.debug", "true");
//        props.put("mail.transport.protocal", "smtps");
//        props.put("mail.smtp.STARTTLS.enable", "true");
//        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
////        props.put("mail.smtp.username", "johndoejava2@gmail.com");
////        props.put("mail.smtp.password", "ugni3txn");
//        props.put("mail.properties.mail.smtp.auth", "true");
////        props.put("mail.smtps.**ssl.enable", "false");
////        props.setProperty("mail.smtps.**ssl.required", "false");
//
//        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("johndoejava2@gmail.com", "ugni3txn");
//            }
//        });
//        Message msg = new MimeMessage(session);
//        msg.setFrom(new InternetAddress("johndoejava2@gmail.com", false));
//
//        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("johndoejava2@gmail.com"));
//        msg.setSubject("Tutorials point email");
//        msg.setContent("Tutorials point email", "text/html");
//        msg.setSentDate(new Date());
//
//        MimeBodyPart messageBodyPart = new MimeBodyPart();
//        messageBodyPart.setContent("Tutorials point email", "text/html");
//
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart);
//        MimeBodyPart attachPart = new MimeBodyPart();
//
//        attachPart.attachFile("/images/logo.png");
//        multipart.addBodyPart(attachPart);
//        msg.setContent(multipart);
//        Transport.send(msg);
//    }

}