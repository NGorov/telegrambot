package com.provisa.telegram.inbound;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

public class TelegramInboundAdapter implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramInboundSink sink;

    public TelegramInboundAdapter(TelegramInboundSink sink) {
        this.sink = Objects.requireNonNull(sink);
    }

    @Override
    public void consume(Update update) {
        if (update == null || !update.hasMessage()) {
            return;
        }

        if (!update.getMessage().hasText()) {
            return;
        }

        String text = update.getMessage().getText();
        if (text == null || text.isBlank()) {
            return;
        }

        sink.accept(text);
    }
}
