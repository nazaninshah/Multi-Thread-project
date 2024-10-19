package com.project.shahbazi.spring_multiThread_pro.batch;

import com.project.shahbazi.spring_multiThread_pro.entity.AccountEntity;
import com.project.shahbazi.spring_multiThread_pro.entity.CustomerEntity;
import com.project.shahbazi.spring_multiThread_pro.repository.CustomerRepo;
import com.project.shahbazi.spring_multiThread_pro.service.AESUtils;
import com.project.shahbazi.spring_multiThread_pro.validation.ErrorRecord;
import com.project.shahbazi.spring_multiThread_pro.validation.ErrorWriter;
import com.project.shahbazi.spring_multiThread_pro.validation.ValidationError;
import com.project.shahbazi.spring_multiThread_pro.validation.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Set;


@Slf4j
public class AccountProcessor implements ItemProcessor<AccountEntity,AccountEntity> {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ErrorWriter errorWriter;

    @Autowired
    private Validator validator;

    public AccountEntity validateAndProcessAccount(AccountEntity account) throws ValidationException {

            // Validate account entity having no null field
            Set<ConstraintViolation<AccountEntity>> violations = validator.validate(account);
            if (!violations.isEmpty()) {
                throw new ValidationException("Account Validation Failed: ", ValidationError.FIELD_IS_NULL);
            }

            try {
            // Decrypt and validate account balance
            String decryptedAccountBalance = AESUtils.decrypt(account.getAccountBalance());
            if (Double.parseDouble(decryptedAccountBalance) > account.getAccountLimit()) {
                throw new ValidationException("Account Validation Failed", ValidationError.INVALID_ACCOUNT_BALANCE);
            }

            // Decrypt and validate account number
            String decryptedAccountNumber = AESUtils.decrypt(account.getAccountNumber());
            if (decryptedAccountNumber.length() != 22 || !decryptedAccountNumber.matches("\\d{22}")) {
                throw new ValidationException("Account Validation Failed", ValidationError.INVALID_ACCOUNT_NUMBER);
            }

        } catch (Exception e) {
            log.error("Decryption failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        // Validate account and customer matching
        CustomerEntity matchingCustomer = customerRepo.findByCustomerId(account.getAccountCustomerId());
        if (matchingCustomer == null) {
            throw new ValidationException("Account Validation Failed", ValidationError.INVALID_ACCOUNT_CUSTOMER_ID);
        }

        // If all validations pass, return the account object
        return account;
    }


    @Override
    public AccountEntity process(AccountEntity account) {
        log.info("Processing account for {}", account);
        try {
            return validateAndProcessAccount(account);
        } catch (ValidationException e) {
            log.error("Validation failed for account: {}. Error: {}", account, e.getMessage());
            errorWriter.writeErrorRecord(new ErrorRecord("AccountsError.csv", account.getRecordNumber(), e.getErrorCode()));
            return null;  // Skip this invalid record
        }
    }

}

