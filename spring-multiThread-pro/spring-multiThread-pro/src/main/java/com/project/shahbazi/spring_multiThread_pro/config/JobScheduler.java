package com.project.shahbazi.spring_multiThread_pro.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobScheduler {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job customerJob;

    @PostConstruct
    public void runJobAtStartup() {
        try {
            log.info("starting customer job");
            jobLauncher.run(customerJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
