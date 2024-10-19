package com.project.shahbazi.spring_multiThread_pro.batch;

import com.project.shahbazi.spring_multiThread_pro.entity.CustomerEntity;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

@Slf4j
public class CustomerProcessor implements ItemProcessor<CustomerEntity,CustomerEntity> {

    @Autowired
    private ErrorWriter errorWriter;

    @Autowired
    private Validator validator;

    public CustomerEntity validateAndProcessCustomer(CustomerEntity customer) throws ValidationException {

        //Validate customer entity having no null field
        Set<ConstraintViolation<CustomerEntity>> violations = validator.validate(customer);
        if (!violations.isEmpty()) {
            throw new ValidationException("Customer Validation Failed: ", ValidationError.FIELD_IS_NULL);
        }

        //Validate customer birth date > 1995
        Date birthDate = customer.getCustomerBirthDate();
        LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (birthLocalDate.getYear() > 1995) {
            throw new ValidationException("Customer validation field", ValidationError.INVALID_CUSTOMER_BIRTH_DATE);
        }

        try {
            //Validate customer national ID is exactly 10 digits
            String decryptedNationalId = AESUtils.decrypt(customer.getCustomerNationalId());
            if (decryptedNationalId.length() != 10 || !decryptedNationalId.matches("\\d{10}")) {
                throw new ValidationException("Customer validation field", ValidationError.INVALID_CUSTOMER_NATIONAL_ID);
            }
        } catch (Exception e) {
            log.error("Decryption failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        // If all validations pass, return the customer object
        return customer;
    }

    @Override
    public CustomerEntity process(CustomerEntity customer) throws ValidationException {
        log.info("Processing customer for {}", customer);
        try {
            return validateAndProcessCustomer(customer);
        } catch (ValidationException e) {
            log.error("Validation failed for account: {}. Error: {}", customer, e.getMessage());
            errorWriter.writeErrorRecord(new ErrorRecord("Customers.csv", customer.getRecordNumber(), e.getErrorCode()));
            return null;  // Skip this invalid record
        }
    }

}
