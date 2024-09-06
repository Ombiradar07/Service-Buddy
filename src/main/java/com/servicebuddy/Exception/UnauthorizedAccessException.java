package com.servicebuddy.Exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String s) {
         super(s);
    }
}
