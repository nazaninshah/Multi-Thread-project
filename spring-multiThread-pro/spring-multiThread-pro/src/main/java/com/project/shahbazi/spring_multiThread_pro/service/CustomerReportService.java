package com.project.shahbazi.spring_multiThread_pro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.project.shahbazi.spring_multiThread_pro.dto.CustomerDTO;
import com.project.shahbazi.spring_multiThread_pro.entity.AccountEntity;
import com.project.shahbazi.spring_multiThread_pro.entity.CustomerEntity;
import com.project.shahbazi.spring_multiThread_pro.repository.AccountRepo;
import com.project.shahbazi.spring_multiThread_pro.repository.CustomerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerReportService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private CustomerRepo customerRepo;

    // Method to find accounts with decrypted balance > 1000 and corresponding customer data
    public List<CustomerDTO> getCustomersWithAccountBalanceGreaterThan1000() {
        List<AccountEntity> accounts = accountRepo.findAll();
        List<AccountEntity> filteredAccounts = accounts.stream()
                .filter(account -> {
                    String decryptedBalance = null;
                    try {
                        decryptedBalance = AESUtils.decrypt(account.getAccountBalance());
                    } catch (Exception e) {
                        log.error("Decryption failed: {}", e.getMessage());
                        throw new RuntimeException(e);
                    }
                    return Double.parseDouble(decryptedBalance) > 1000;
                })
                .toList();

        // Map the accounts to their corresponding customer details
        return filteredAccounts.stream()
                .map(account -> {
                    CustomerEntity customer = customerRepo.findByCustomerId(account.getAccountCustomerId());

                    // Create DTO to hold customer info
                    CustomerDTO dto = new CustomerDTO();
                    dto.setCustomerName(customer.getCustomerName());
                    dto.setCustomerSurname(customer.getCustomerSurname());
                    dto.setCustomerAddress(customer.getCustomerAddress());
                    dto.setCustomerZipCode(customer.getCustomerZipCode());
                    dto.setCustomerNationalId(customer.getCustomerNationalId()); // didn't save decrypted NathionalId for the sake of security
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Method to write customers with balance > 1000 to JSON
    public void writeCustomersToJson(List<CustomerDTO> customers, String filePath) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // enabling pretty printing
        objectMapper.writeValue(new File(filePath), customers);
    }

    // Method to write customers with balance > 1000 to XML
    public void writeCustomersToXml(List<CustomerDTO> customers, String filePath) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT); // enabling pretty printing
        xmlMapper.writeValue(new File(filePath), customers);
    }

    // Method to export customers with balance > 1000 to both JSON and XML
    public void exportCustomersWithBalanceGreaterThan1000(String jsonFilePath, String xmlFilePath) throws Exception {
        List<CustomerDTO> customers = getCustomersWithAccountBalanceGreaterThan1000();
        writeCustomersToJson(customers, jsonFilePath);
        writeCustomersToXml(customers, xmlFilePath);
    }
}
