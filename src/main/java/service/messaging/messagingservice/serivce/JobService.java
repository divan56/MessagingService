package service.messaging.messagingservice.serivce;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import service.messaging.messagingservice.dto.JobDescriptor;

import java.time.ZonedDateTime;
import java.util.List;

public interface JobService {
    JobDetail buildJobDetail(JobDescriptor jobDescriptor);

    Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startAt);

    JobDescriptor addNewJob(JobDescriptor jobDescriptor) throws SchedulerException;

    JobDescriptor getJob(String group, String id) throws SchedulerException;

    List<JobDescriptor> getAllJobs();
}
