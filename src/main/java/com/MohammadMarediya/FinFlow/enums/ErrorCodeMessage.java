package com.MohammadMarediya.FinFlow.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeMessage {

    EMAIL_ALREADY_EXIST(10001, "Email is already registered. Please log in or use a different email."),
    MOBILE_NUMBER_ALREADY_EXIST(10002, "Mobile number is already registered. Please log in or use a different mobile number."),
    METHOD_ARGUMENT_NOT_VALID(20001, "Invalid input provided. Please check your data and try again."),
    CONSTRAINT_VIOLATION(20002, "Constraint violation occurred."),
    NULL_POINTER_EXCEPTION(20003, "An unexpected internal error occurred."),
    ILLEGAL_ARGUMENT(20004, "Invalid request parameters."),
    BAD_CREDENTIALS(20005, "Invalid email or password."),
    TOKEN_EXPIRED(20006, "JWT token has expired."),
    INVALID_USERNAME_OR_PASSWORD(20007, "Invalid username or password."),
    ACCESS_DENIED(20008, "Access denied. You do not have permission to perform this action."),
    RESOURCE_NOT_FOUND(20009, "Requested resource not found."),
    INTERNAL_SERVER_ERROR(9999, "Something went wrong. Please try again later.");

    private final int errorCode;
    private final String message;
}


