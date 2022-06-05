package service.messaging.messagingservice.serivce;

import service.messaging.messagingservice.entity.EmailAddress;

import javax.mail.MessagingException;
import java.util.List;

public interface EmailService {

    public void buildMessageThenSend(String subject, String text) throws MessagingException;
    List<EmailAddress> getAllEmails();
}
