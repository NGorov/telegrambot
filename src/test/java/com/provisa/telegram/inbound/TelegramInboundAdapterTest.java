package com.provisa.telegram.inbound;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramInboundAdapterTest {

    @Test
    void shouldForwardTextMessage() {
        AtomicReference<String> received = new AtomicReference<>();

        TelegramInboundAdapter adapter =
                new TelegramInboundAdapter(received::set);

        Update update = new Update();
        Message message = new Message();
        message.setText("hello");
        update.setMessage(message);

        adapter.consume(update);

        assertThat(received.get()).isEqualTo("hello");
    }

    @Test
    void shouldIgnoreEmptyMessage() {
        AtomicReference<String> received = new AtomicReference<>();

        TelegramInboundAdapter adapter =
                new TelegramInboundAdapter(received::set);

        Update update = new Update();
        Message message = new Message();
        message.setText("   ");
        update.setMessage(message);

        adapter.consume(update);

        assertThat(received.get()).isNull();
    }
}
