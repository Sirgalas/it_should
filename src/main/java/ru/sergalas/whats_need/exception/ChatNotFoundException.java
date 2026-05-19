package ru.sergalas.whats_need.exception;

import java.util.NoSuchElementException;

public class ChatNotFoundException extends NoSuchElementException {
    public ChatNotFoundException(String message) {
        super(message);
    }
    public  ChatNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public  ChatNotFoundException(Throwable cause) {
        super(cause);
    }
}
