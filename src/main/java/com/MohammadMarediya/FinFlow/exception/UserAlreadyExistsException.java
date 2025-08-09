package com.MohammadMarediya.FinFlow.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final Integer errorCode;

    public UserAlreadyExistsException(Integer errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

