package com.provisa.telegram.outbound;

public class TelegramMessageFormatter {

    public String format(String message) {
        if (message == null) {
            throw new IllegalArgumentException("message is null");
        }

        return message
                .replaceAll("\\s{2,}", " ")
                .trim();
    }
}
