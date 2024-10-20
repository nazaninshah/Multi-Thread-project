package com.project.shahbazi.spring_multiThread_pro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "customer_table")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Transient
    private Integer recordNumber; // Used for processing but not stored in DB

    @NotNull
    @Column(name = "customer_id", nullable = false, unique = true)
    private Integer customerId;

    @NotEmpty
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @NotEmpty
    @Column(name = "customer_surname", nullable = false)
    private String customerSurname;

    @NotEmpty
    @Column(name = "customer_address", nullable = false)
    private String customerAddress;

    @NotEmpty
    @Column(name = "customer_zip_code", nullable = false)
    private String customerZipCode;

    @NotEmpty
    @Column(name = "customer_national_id", nullable = false)
    private String customerNationalId;  // AES Encrypted national ID

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "customer_birth_date", nullable = false)
    private Date customerBirthDate;

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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerZipCode() {
        return customerZipCode;
    }

    public void setCustomerZipCode(String customerZipCode) {
        this.customerZipCode = customerZipCode;
    }

    public String getCustomerNationalId() {
        return customerNationalId;
    }

    public void setCustomerNationalId(String customerNationalId) {
        this.customerNationalId = customerNationalId;
    }

    public Date getCustomerBirthDate() {
        return customerBirthDate;
    }

    public void setCustomerBirthDate(Date customerBirthDate) {
        this.customerBirthDate = customerBirthDate;
    }
}
