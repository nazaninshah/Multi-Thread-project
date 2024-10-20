package com.project.shahbazi.spring_multiThread_pro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "account_table")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  long id;

    @NotNull
    @Transient
    private Integer recordNumber; // Used for processing but not stored in DB

    @NotEmpty
    @Column(name = "account_number", nullable = false)
    private String accountNumber;  // This stores the AES encrypted value

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @NotNull
    @Column(name = "account_customer_id", nullable = false)
    private Long accountCustomerId;

    @NotNull
    @Column(name = "account_limit", nullable = false)
    private Double accountLimit;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "account_open_date", nullable = false)
    private Date accountOpenDate;

    @NotEmpty
    @Column(name = "account_balance", nullable = false)
    private String accountBalance;  // AES Encrypted balance

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Integer recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getAccountCustomerId() {
        return accountCustomerId;
    }

    public void setAccountCustomerId(Long accountCustomerId) {
        this.accountCustomerId = accountCustomerId;
    }

    public Double getAccountLimit() {
        return accountLimit;
    }

    public void setAccountLimit(Double accountLimit) {
        this.accountLimit = accountLimit;
    }

    public Date getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(Date accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }
}
