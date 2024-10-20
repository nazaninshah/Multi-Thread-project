package com.project.shahbazi.spring_multiThread_pro.validation;

import java.util.Date;

public class ErrorRecord {
    private String fileName;
    private int recordNumber;
    private String errorCode;
    private String errorClassification;
    private String errorDescription;
    private Date errorDate;

    public ErrorRecord(String fileName, int recordNumber, ValidationError error) {
        this.fileName = fileName;
        this.recordNumber = recordNumber;
        this.errorCode = error.getCode();
        this.errorClassification = error.getClassification();
        this.errorDescription = error.getDescription();
        this.errorDate = new Date();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorClassification() {
        return errorClassification;
    }

    public void setErrorClassification(String errorClassification) {
        this.errorClassification = errorClassification;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Date getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(Date errorDate) {
        this.errorDate = errorDate;
    }
}
