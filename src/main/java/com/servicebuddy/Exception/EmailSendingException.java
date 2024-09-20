package com.servicebuddy.Exception;

public class EmailSendingException extends RuntimeException{
     public EmailSendingException(String message) {
          super(message);
     }
}
