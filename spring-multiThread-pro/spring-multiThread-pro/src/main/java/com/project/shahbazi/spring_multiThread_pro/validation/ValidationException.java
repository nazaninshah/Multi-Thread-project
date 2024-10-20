package com.project.shahbazi.spring_multiThread_pro.validation;

//changing ValidationException class to use ValidationError properly
public class ValidationException extends Exception {
    private final ValidationError error;

    public ValidationException(String message, ValidationError error) {
        super(message);
        this.error = error;
    }

    public ValidationError getErrorCode() {
        return error;
    }

    //overriding getMessage() to include the error code description
    @Override
    public String getMessage() {
        return super.getMessage() + " (Error code: " + error.getCode() + ")";
    }
}
