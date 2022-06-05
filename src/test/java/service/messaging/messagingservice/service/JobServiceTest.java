package service.messaging.messagingservice.service;

import org.junit.jupiter.api.Test;
import service.messaging.messagingservice.dto.JobDescriptor;
import service.messaging.messagingservice.serivce.JobServiceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class JobServiceTest {

    @Test
    public void addNewJobWithEmptyFieldsShouldThrowException() {
        JobServiceImpl tester = new JobServiceImpl();
        assertThrows(NullPointerException.class,
                () -> tester.addNewJob(new JobDescriptor()));
    }

    @Test
    public void addNewJobWithWrongTime2ExecuteShouldThrowException() {
        JobServiceImpl tester = new JobServiceImpl();
        assertThrows(IllegalArgumentException.class,
                () -> tester.addNewJob(new JobDescriptor
                        (
                                "TEST",
                                "TEST",
                                LocalDateTime.parse("2020-04-12T14:43:14"),
                                ZoneId.of("America/Los_Angeles")
                        )));
    }
}
