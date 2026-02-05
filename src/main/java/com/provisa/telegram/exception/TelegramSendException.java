package com.provisa.telegram.exception;

public class TelegramSendException extends RuntimeException {

    public TelegramSendException(String message) {
        super(message);
    }

    public TelegramSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
