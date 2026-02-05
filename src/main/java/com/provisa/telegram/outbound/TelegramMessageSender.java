package com.provisa.telegram.outbound;

import com.provisa.dao.Operator;
import com.provisa.telegram.model.ChatTarget;

public interface TelegramMessageSender {

    void send(String message, Operator operator, ChatTarget target);

}
