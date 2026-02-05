package com.provisa.telegram.outbound;

import com.provisa.dao.Operator;
import com.provisa.telegram.model.ChatTarget;

public interface TelegramMessageSender {

    void send(String message, Operator operator, ChatTarget target);

    default void sendPersonal(String message, Operator operator) {
        send(message, operator, ChatTarget.PERSONAL);
    }

    default void sendGroup(String message, Operator operator) {
        send(message, operator, ChatTarget.GROUP);
    }
}
