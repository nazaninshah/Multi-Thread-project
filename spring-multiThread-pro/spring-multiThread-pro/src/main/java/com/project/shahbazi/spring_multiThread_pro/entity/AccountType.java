package com.project.shahbazi.spring_multiThread_pro.entity;

public enum AccountType {
    SAVING(1),
    RECURRING_DEPOSIT(2),
    FIXED_DEPOSIT(3);

    private final int code;

    AccountType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AccountType fromCode(int code) throws IllegalArgumentException {
        for (AccountType type : AccountType.values()) {
            if (type.getCode() == code) {
                return type;  // Return the enum value, not a string
            }
        }
        throw new IllegalArgumentException("Invalid account type: " + code);
    }
}
