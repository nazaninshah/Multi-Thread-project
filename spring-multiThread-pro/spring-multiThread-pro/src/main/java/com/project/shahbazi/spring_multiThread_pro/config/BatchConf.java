package com.project.shahbazi.spring_multiThread_pro.config;

import com.project.shahbazi.spring_multiThread_pro.batch.AccountProcessor;
import com.project.shahbazi.spring_multiThread_pro.batch.AccountWriter;
import com.project.shahbazi.spring_multiThread_pro.batch.CustomerWriter;
import com.project.shahbazi.spring_multiThread_pro.entity.AccountEntity;
import com.project.shahbazi.spring_multiThread_pro.entity.AccountType;
import com.project.shahbazi.spring_multiThread_pro.batch.CustomerProcessor;
import com.project.shahbazi.spring_multiThread_pro.entity.CustomerEntity;
import com.project.shahbazi.spring_multiThread_pro.repository.AccountRepo;
import com.project.shahbazi.spring_multiThread_pro.repository.CustomerRepo;
import com.project.shahbazi.spring_multiThread_pro.validation.ErrorWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
public class BatchConf {

    // Account Beans
    @Bean
    public Job accountJob (JobRepository jobRepository, PlatformTransactionManager transactionManager, AccountProcessor accountProcessor, AccountRepo accountRepo,
                           ErrorWriter errorWriter) {
        return  new JobBuilder("accountJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(accountStep(jobRepository, transactionManager, accountProcessor, accountRepo, errorWriter))
                .build();
    }

    @Bean
    public Step accountStep (JobRepository jobRepository, PlatformTransactionManager transactionManager, AccountProcessor accountProcessor, AccountRepo accountRepo,
                             ErrorWriter errorWriter) {
        return  new StepBuilder("accountStep", jobRepository)
                .<AccountEntity,AccountEntity>chunk(10, transactionManager)
                .reader(accountReader())
                .processor(accountProcessor(accountProcessor))
                .writer(accountWriter(accountRepo, errorWriter))
                .allowStartIfComplete(true) // Optionally restart even if previously completed
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<AccountEntity> accountReader(){
        return new FlatFileItemReaderBuilder<AccountEntity>()
                .name("accountReader")
                .resource(new ClassPathResource("Accounts.csv"))
                .delimited()
                .names(new String[]{"recordNumber", "accountNumber", "accountType", "accountCustomerId", "accountLimit", "accountOpenDate", "accountBalance"})
                .fieldSetMapper(fieldSet -> {
                    BeanWrapperFieldSetMapper<AccountEntity> mapper = new BeanWrapperFieldSetMapper<>();
                    mapper.setTargetType(AccountEntity.class);
                    AccountEntity account = mapper.mapFieldSet(fieldSet);
                    // Manually setting the custom field AccountType
                    account.setAccountType(AccountType.valueOf(fieldSet.readString("accountType")));
                    return account;
                })
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<AccountEntity, AccountEntity> accountProcessor(AccountProcessor accountProcessor){
        CompositeItemProcessor<AccountEntity, AccountEntity> processor = new CompositeItemProcessor<>();
        processor.setDelegates(List.of(accountProcessor)); // Use the injected accountProcessor bean
        return processor;
    }

    @Bean
    @StepScope
    public AccountProcessor accountProcessorBean() {
        return new AccountProcessor();
    }

    @Bean
    @StepScope
    public ItemWriter<AccountEntity> accountWriter(AccountRepo accountRepo, ErrorWriter errorWriter){
        return new AccountWriter(accountRepo, errorWriter);
    }

    //Customer Beans
    @Bean
    public Job customerJob(JobRepository jobRepository, PlatformTransactionManager transactionManager,CustomerProcessor customerProcessor,
                           CustomerRepo customerRepo, ErrorWriter errorWriter, BatchJobListener jobListener) {
        return new JobBuilder("customerJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(customerStep(jobRepository, transactionManager, customerProcessor, customerRepo, errorWriter))
                .listener(jobListener)
                .build();
    }

    @Bean
    public JobExecutionListener jobExecutionListener(Job accountJob) {
        return new BatchJobListener(accountJob); // Inject accountJob via constructor
    }

    @Bean
    public Step customerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, CustomerProcessor customerProcessor, CustomerRepo customerRepo, ErrorWriter errorWriter) {
        return new StepBuilder("customerStep", jobRepository)
                .<CustomerEntity, CustomerEntity>chunk(10, transactionManager)
                .reader(customerReader())
                .processor(customerProcessor(customerProcessor))
                .writer(customerWriter(customerRepo, errorWriter))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<CustomerEntity> customerReader() {
        return new FlatFileItemReaderBuilder<CustomerEntity>()
                .name("customerReader")
                .resource(new ClassPathResource("Customers.csv"))
                .delimited()
                .names(new String[]{"recordNumber", "customerId", "customerName", "customerSurname", "customerAddress", "customerZipCode", "customerNationalId", "customerBirthDate"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(CustomerEntity.class);
                }})
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<CustomerEntity, CustomerEntity> customerProcessor(CustomerProcessor customerProcessor) {
        CompositeItemProcessor<CustomerEntity, CustomerEntity> processor = new CompositeItemProcessor<>();
        processor.setDelegates(List.of(customerProcessor));
        return processor;
    }

    @Bean
    @StepScope
    public CustomerProcessor customerProcessorBean() {
        return new CustomerProcessor();
    }

    @Bean
    @StepScope
    public ItemWriter<CustomerEntity> customerWriter(CustomerRepo customerRepo, ErrorWriter errorWriter) {
        return new CustomerWriter(customerRepo, errorWriter);
    }
}
