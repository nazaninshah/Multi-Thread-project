package com.project.shahbazi.spring_multiThread_pro.validation;

public enum ValidationError {
    INVALID_ACCOUNT_BALANCE("ERR001", "Account Validation", "Account balance exceeds the account limit"),
    INVALID_ACCOUNT_TYPE("ERR002", "Account Validation", "Invalid account type"),
    INVALID_ACCOUNT_CUSTOMER_ID("ERR003", "Account Customer Matching Validation", "Invalid customer ID"),
    INVALID_ACCOUNT_NUMBER("ERR004", "Account Validation", "Invalid account number"),
    INVALID_CUSTOMER_NATIONAL_ID("ERR005", "Customer Validation", "Invalid customer national ID"),
    INVALID_CUSTOMER_BIRTH_DATE("ERR006", "Customer Validation", "Customer is under the legal age"),
    FIELD_IS_NULL("ERR007", "Not Null Validation", "Field is null");

    private final String code;
    private final String classification;
    private final String description;

    ValidationError(String code, String classification, String description) {
        this.code = code;
        this.classification = classification;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getClassification() {
        return classification;
    }

    public String getDescription() {
        return description;
    }
}
