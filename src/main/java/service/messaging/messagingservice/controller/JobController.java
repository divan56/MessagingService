package service.messaging.messagingservice.controller;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.messaging.messagingservice.dto.JobDescriptor;
import service.messaging.messagingservice.serivce.JobService;

import java.util.List;


@RestController
@RequestMapping("/api")
public class JobController {
    @Autowired
    private JobService jobService;

    @PostMapping("/job")
    public ResponseEntity<JobDescriptor> addNewJob
            (@RequestBody JobDescriptor jobDescriptor) throws SchedulerException {
        //addNewJob returns info about added job
         JobDescriptor response = jobService.addNewJob(jobDescriptor);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/job")
    public List<JobDescriptor> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/job/group/{group}/id/{id}")
    public ResponseEntity<JobDescriptor> getJob
            (@PathVariable String group, @PathVariable String id) throws SchedulerException {
        JobDescriptor response = jobService.getJob(group, id);
        return ResponseEntity.ok(response);
    }
}
