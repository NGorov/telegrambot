package com.provisa.telegram.outbound;

import com.provisa.dao.Operator;
import com.provisa.telegram.exception.TelegramSendException;
import com.provisa.telegram.model.ChatTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DefaultTelegramMessageSenderTest {

    private TelegramClient telegramClient;
    private TelegramMessageSender sender;

    @BeforeEach
    void setUp() {
        telegramClient = mock(TelegramClient.class);
        sender = new DefaultTelegramMessageSender(
                telegramClient,
                new TelegramMessageFormatter()
        );
    }

    @Test
    void sendPersonalMessage() throws Exception {
        Operator operator = new Operator();
        operator.setPersonalChatId("123");

        sender.send("hello", operator, ChatTarget.PERSONAL);

        ArgumentCaptor<SendMessage> captor =
                ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramClient).execute(captor.capture());

        SendMessage msg = captor.getValue();
        assertThat(msg.getChatId()).isEqualTo("123");
        assertThat(msg.getText()).isEqualTo("hello");
    }

    @Test
    void sendGroupMessage() throws Exception {
        Operator operator = new Operator();
        operator.setGroupChatId("456");

        sender.send("result ready", operator, ChatTarget.GROUP);

        ArgumentCaptor<SendMessage> captor =
                ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramClient).execute(captor.capture());

        assertThat(captor.getValue().getChatId()).isEqualTo("456");
    }

    @Test
    void truncateTooLongMessage() throws Exception {
        Operator operator = new Operator();
        operator.setPersonalChatId("123");

        String longText = "a".repeat(5000);

        sender.send(longText, operator, ChatTarget.PERSONAL);

        ArgumentCaptor<SendMessage> captor =
                ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramClient).execute(captor.capture());

        assertThat(captor.getValue().getText().length()).isEqualTo(4095);
    }

    @Test
    void failWhenChatIdMissing() {
        Operator operator = new Operator();

        assertThatThrownBy(() ->
                sender.send("test", operator, ChatTarget.PERSONAL)
        ).isInstanceOf(TelegramSendException.class);
    }

    @Test
    void wrapTelegramException() throws Exception {
        Operator operator = new Operator();
        operator.setPersonalChatId("123");

        doThrow(new TelegramApiException("fail"))
                .when(telegramClient)
                .execute(any(SendMessage.class));

        assertThatThrownBy(() ->
                sender.send("hello", operator, ChatTarget.PERSONAL)
        ).isInstanceOf(TelegramSendException.class);
    }
}
