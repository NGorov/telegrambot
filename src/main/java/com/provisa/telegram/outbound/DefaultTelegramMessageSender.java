package com.provisa.telegram.outbound;

import com.provisa.dao.Operator;
import com.provisa.telegram.exception.TelegramSendException;
import com.provisa.telegram.model.ChatTarget;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Objects;

public class DefaultTelegramMessageSender implements TelegramMessageSender {

    private static final int MAX_LENGTH = 4096;

    private final TelegramClient client;
    private final TelegramMessageFormatter formatter;

    public DefaultTelegramMessageSender(
            TelegramClient client,
            TelegramMessageFormatter formatter
    ) {
        this.client = Objects.requireNonNull(client);
        this.formatter = Objects.requireNonNull(formatter);
    }

    @Override
    public void send(String message, Operator operator, ChatTarget target) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(target);

        String chatId = resolveChatId(operator, target);
        String formatted = formatter.format(message);
        String truncated = truncate(formatted);

        SendMessage request = SendMessage.builder()
                .chatId(chatId)
                .text(truncated)
                .build();

        try {
            client.execute(request);
        } catch (TelegramApiException e) {
            throw new TelegramSendException("Telegram send failed", e);
        }
    }

    private String resolveChatId(Operator operator, ChatTarget target) {
        String chatId = switch (target) {
            case PERSONAL -> operator.getPersonalChatId();
            case GROUP -> operator.getGroupChatId();
        };

        if (chatId == null || chatId.isBlank()) {
            throw new TelegramSendException("ChatId is empty for target " + target);
        }

        return chatId;
    }

    private String truncate(String text) {
        if (text.length() <= MAX_LENGTH) {
            return text;
        }
        return text.substring(0, MAX_LENGTH - 1);
    }
}
