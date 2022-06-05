package service.messaging.messagingservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class JobDescriptor {
    @NotBlank
    private String subject;

    @NotBlank
    private String text;

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private ZoneId timeZone;

    private String jobId;

    private String jobGroup;

    public String getSubject() {
        return subject;
    }

    public JobDescriptor() {
    }

    public JobDescriptor(String subject, String text, LocalDateTime dateTime, ZoneId timeZone) {
        this.subject = subject;
        this.text = text;
        this.dateTime = dateTime;
        this.timeZone = timeZone;
    }

    public JobDescriptor(String subject, String text, LocalDateTime dateTime, ZoneId timeZone, String jobId, String jobGroup) {
        this.subject = subject;
        this.text = text;
        this.dateTime = dateTime;
        this.timeZone = timeZone;
        this.jobId = jobId;
        this.jobGroup = jobGroup;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }
}
