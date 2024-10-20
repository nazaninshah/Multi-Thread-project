package com.project.shahbazi.spring_multiThread_pro.batch;

import com.project.shahbazi.spring_multiThread_pro.entity.CustomerEntity;
import com.project.shahbazi.spring_multiThread_pro.repository.CustomerRepo;
import com.project.shahbazi.spring_multiThread_pro.validation.ErrorWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class CustomerWriter implements ItemWriter<CustomerEntity> {

    private final CustomerRepo customerRepo;
    private final ErrorWriter errorWriter;

    public CustomerWriter(CustomerRepo customerRepo, ErrorWriter errorWriter) {
        this.customerRepo = customerRepo;
        this.errorWriter = errorWriter;
    }

    //@Override
    public void write(Chunk<? extends CustomerEntity> chunk) throws Exception {
        log.info("Writing : {}", chunk.getItems().size());

        // Save valid customers to the database
        customerRepo.saveAll(chunk.getItems());

        // Save all errors to the JSON file
        errorWriter.saveErrors();
    }
}
