package service.messaging.messagingservice.serivce;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.messaging.messagingservice.dto.JobDescriptor;
import service.messaging.messagingservice.job.MailSendingJob;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.quartz.JobKey.jobKey;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private Scheduler scheduler;

    /*****
     This method returns info about added job as dto object JodDescriptorToResponse
     *****/
    @Override
    public JobDescriptor addNewJob(@NotNull JobDescriptor jobDescriptor) throws SchedulerException {
        ZonedDateTime dateTime = ZonedDateTime.of
                (jobDescriptor.getDateTime(), jobDescriptor.getTimeZone());

        if (dateTime.isBefore(ZonedDateTime.now())) {
            throw new IllegalArgumentException("Date to release cannot be less than date of creation");
        }

        JobDetail jobDetail = buildJobDetail(jobDescriptor);
        Trigger trigger = buildTrigger(jobDetail, dateTime);
        scheduler.scheduleJob(jobDetail, trigger);

        jobDescriptor.setJobId(jobDetail.getKey().getName());
        jobDescriptor.setJobGroup(jobDetail.getKey().getGroup());

        return jobDescriptor;
    }

    @Override
    public JobDescriptor getJob(@NotNull String group, @NotNull String id) throws SchedulerException {
        JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(id, group));
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        return new JobDescriptor
                       (jobDataMap.getString("subject"),
                        jobDataMap.getString("text"),
                        LocalDateTime.parse(jobDataMap.getString("time")),
                        ZoneId.of(jobDataMap.getString("timeZone")),
                        jobDetail.getKey().getName(),
                        jobDetail.getKey().getGroup());

    }

    @Override
    public List<JobDescriptor> getAllJobs() {
        List<JobDescriptor> jobList = new ArrayList<>();
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                    JobDetail jobDetail = scheduler.getJobDetail
                            (jobKey(jobKey.getName(), jobKey.getGroup()));

                    JobDataMap jobDataMap = jobDetail.getJobDataMap();
                    jobList.add(new JobDescriptor
                                   (jobDataMap.getString("subject"),
                                    jobDataMap.getString("text"),
                                    LocalDateTime.parse(jobDataMap.getString("time")),
                                    ZoneId.of(jobDataMap.getString("timeZone")),
                                    jobDetail.getKey().getName(),
                                    jobDetail.getKey().getGroup()));
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobList;
    }

    @Override
    public JobDetail buildJobDetail(JobDescriptor jobDescriptor) {

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("text", jobDescriptor.getText());
        jobDataMap.put("subject", jobDescriptor.getSubject());
        jobDataMap.put("time", jobDescriptor.getDateTime().toString());
        jobDataMap.put("timeZone", jobDescriptor.getTimeZone().toString());


        return JobBuilder.newJob(MailSendingJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .build();
    }

    @Override
    public Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                .withDescription("Mail sending trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
