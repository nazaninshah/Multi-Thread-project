package com.project.shahbazi.spring_multiThread_pro.batch;

import com.project.shahbazi.spring_multiThread_pro.entity.AccountEntity;
import com.project.shahbazi.spring_multiThread_pro.entity.AccountType;
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

    StringBuilder decryptedAccountBalance = new StringBuilder();
    StringBuilder decryptedAccountNumber = new StringBuilder();

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

        // Validate account balance
        try {
            decryptedAccountBalance.append(AESUtils.decrypt(account.getAccountBalance()));
        } catch (Exception e) {
            log.error("Decryption failed for account with recordNumber: {}. Error: {}", account.getRecordNumber(), e.getMessage());
            throw new RuntimeException(e);
        }
        // Proceed with validation only if decryption was successful
        if (decryptedAccountBalance.length() > 0) {
            // Validate customer national ID is exactly 10 digits
            if (Double.parseDouble(decryptedAccountBalance.toString()) > account.getAccountLimit()) {
                // Clear the decrypted string after usage
                decryptedAccountBalance.setLength(0);
                throw new ValidationException("Account Validation Failed", ValidationError.INVALID_ACCOUNT_BALANCE);
            }
            // Clear the decrypted string after usage
            decryptedAccountBalance.setLength(0);
        }

        // Validate account number
        try {
            decryptedAccountNumber.append(AESUtils.decrypt(account.getAccountNumber()));
        } catch (Exception e) {
            log.error("Decryption failed for account with recordNumber: {}. Error: {}", account.getRecordNumber(), e.getMessage());
            throw new RuntimeException(e);
        }
        // Proceed with validation only if decryption was successful
        if (decryptedAccountNumber.length() > 0) {
            // Validate customer national ID is exactly 10 digits
            if (decryptedAccountNumber.length() != 22 || !decryptedAccountNumber.toString().matches("\\d{22}")) {
                // Clear the decrypted string after usage
                decryptedAccountNumber.setLength(0);
                throw new ValidationException("Account Validation Failed", ValidationError.INVALID_ACCOUNT_NUMBER);
            }
            // Clear the decrypted string after usage
            decryptedAccountNumber.setLength(0);
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
            errorWriter.writeErrorRecord(new ErrorRecord("Accounts.csv", account.getRecordNumber(), e.getErrorCode()));
            return null;  // Skip this invalid record
        }
    }

}

