package com.project.shahbazi.spring_multiThread_pro.batch;

import com.project.shahbazi.spring_multiThread_pro.entity.AccountEntity;
import com.project.shahbazi.spring_multiThread_pro.repository.AccountRepo;
import com.project.shahbazi.spring_multiThread_pro.validation.ErrorWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class AccountWriter implements ItemWriter<AccountEntity > {

    private final AccountRepo accountRepo;
    private final ErrorWriter errorWriter;

    // Constructor injection
    public AccountWriter(AccountRepo accountRepo, ErrorWriter errorWriter) {
        this.accountRepo = accountRepo;
        this.errorWriter = errorWriter;
    }

    //@Override
    public void write(Chunk<? extends AccountEntity> chunk) throws Exception {
        log.info("Writing : {}", chunk.getItems().size());

        // Save valid accounts to the database
        accountRepo.saveAll(chunk.getItems());

        // Save all errors to the JSON file
        errorWriter.saveErrors();
    }
}
