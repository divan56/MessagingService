package service.messaging.messagingservice.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import service.messaging.messagingservice.serivce.EmailService;

import javax.mail.MessagingException;

@Component
public class MailSendingJob extends QuartzJobBean {
    @Autowired
    private EmailService emailService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        SimpleMailMessage message = new SimpleMailMessage();

        String subject = jobDataMap.getString("subject");
        String text = jobDataMap.getString("text");

        try {
            emailService.buildMessageThenSend(subject, text);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
