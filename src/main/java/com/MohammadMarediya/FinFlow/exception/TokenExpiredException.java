package com.MohammadMarediya.FinFlow.exception;


public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
