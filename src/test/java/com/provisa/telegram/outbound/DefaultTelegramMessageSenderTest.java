package com.provisa.telegram.outbound;

import com.provisa.dao.Operator;
import com.provisa.telegram.exception.TelegramSendException;
import com.provisa.telegram.model.ChatTarget;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DefaultTelegramMessageSenderTest {

    private final TelegramClient telegramClient = mock(TelegramClient.class);
    private final TelegramMessageFormatter formatter = new TelegramMessageFormatter();
    private final DefaultTelegramMessageSender sender =
            new DefaultTelegramMessageSender(telegramClient, formatter);

    @Test
    void shouldSendPersonalMessage() throws Exception {
        Operator operator = new Operator();
        operator.setPersonalChatId("123");

        sender.send("hello", operator, ChatTarget.PERSONAL);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramClient).execute(captor.capture());

        SendMessage msg = captor.getValue();
        assertThat(msg.getChatId()).isEqualTo("123");
        assertThat(msg.getText()).isEqualTo("Bot: hello");
    }

    @Test
    void shouldSendGroupMessage() throws Exception {
        Operator operator = new Operator();
        operator.setGroupChatId("456");

        sender.send("result ready", operator, ChatTarget.GROUP);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramClient).execute(captor.capture());

        assertThat(captor.getValue().getChatId()).isEqualTo("456");
    }

    @Test
    void shouldFailWhenChatIdMissing() {
        Operator operator = new Operator();

        assertThrows(TelegramSendException.class,
                () -> sender.send("test", operator, ChatTarget.GROUP));
    }

    @Test
    void shouldTruncateLongMessages() throws Exception {
        Operator operator = new Operator();
        operator.setPersonalChatId("123");

        String longMessage = "a".repeat(5000);
        sender.send(longMessage, operator, ChatTarget.PERSONAL);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramClient).execute(captor.capture());

        assertThat(captor.getValue().getText().length()).isLessThanOrEqualTo(4095);
    }
}
