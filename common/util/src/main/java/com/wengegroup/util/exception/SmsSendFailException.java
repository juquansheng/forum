package com.wengegroup.util.exception;

public class SmsSendFailException extends RuntimeException{

    public SmsSendFailException(String message) {
        super(message);
    }
}
