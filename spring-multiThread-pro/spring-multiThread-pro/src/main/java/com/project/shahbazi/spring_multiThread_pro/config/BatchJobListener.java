package com.project.shahbazi.spring_multiThread_pro.config;

import com.project.shahbazi.spring_multiThread_pro.service.CustomerReportService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class BatchJobListener implements JobExecutionListener {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private final Job accountJob;

    @Autowired
    private CustomerReportService customerReportService;

    @Autowired
    private ApplicationContext applicationContext;

    public BatchJobListener(Job accountJob) {
        this.accountJob = accountJob; // Inject the accountJob via constructor
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Do nothing before customer job
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus().isUnsuccessful()) {
            System.err.println("Customer job failed. Not proceeding with account job.");
        } else {
            try {
                System.out.println("Customer job completed. Now running account job.");
                jobLauncher.run(accountJob, new JobParameters());
                // Running the Customer Report Service after account job finishes
                customerReportService.exportCustomersWithBalanceGreaterThan1000("customersReport.json", "customersReport.xml");
                System.out.println("Customer report generated successfully.");
                //SpringApplication.exit(applicationContext, () -> 0);
            } catch (Exception e) {
                System.err.println("Failed to run account job: " + e.getMessage());
            }
        }
    }
}

