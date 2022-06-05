package service.messaging.messagingservice.serivce;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.messaging.messagingservice.entity.EmailAddress;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @Override
    public List<EmailAddress> getAllEmails() {
        Session session = entityManager.unwrap(Session.class);
        Query<EmailAddress> query = session.createQuery("SELECT email FROM EmailAddress");
        return query.getResultList();
    }

    @Override
    public void buildMessageThenSend(String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        List<EmailAddress> allMailAddresses = getAllEmails();
        String[] allMailAddressesArr = new String[allMailAddresses.size()];
        allMailAddressesArr = allMailAddresses.toArray(allMailAddressesArr);

        helper.setFrom("${spring.mail.username}");
        helper.setSubject(subject);
        helper.setText(text);

        for (String s : allMailAddressesArr) {

            helper.setTo(allMailAddressesArr);
            mailSender.send(message);

        /*
                ExecutorService executorService = Executors.newFixedThreadPool(10);
                    for (String s : allMailAddressesArr) {
                executorService.submit(() -> {
                    try {
                        helper.setTo(s);
                        mailSender.send(message);
                        System.out.println("Current thread id " + Thread.currentThread().getId());
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            executorService.shutdown();
         */
        }
    }
}
