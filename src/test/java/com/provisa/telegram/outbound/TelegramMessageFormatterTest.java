package com.provisa.telegram.outbound;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TelegramMessageFormatterTest {

    private final TelegramMessageFormatter formatter = new TelegramMessageFormatter();

    @Test
    void shouldPrefixBotMessages() {
        assertThat(formatter.format("hello"))
                .isEqualTo("Bot: hello");
    }

    @Test
    void shouldNormalizeSpaces() {
        assertThat(formatter.format("hello   world"))
                .isEqualTo("Bot: hello world");
    }

    @Test
    void shouldStripServicePrefix() {
        assertThat(formatter.format("service check status"))
                .isEqualTo("check status");
    }

    @Test
    void shouldTrimResult() {
        assertThat(formatter.format("  hello  "))
                .isEqualTo("Bot: hello");
    }

    @Test
    void shouldRejectNullMessage() {
        assertThrows(IllegalArgumentException.class,
                () -> formatter.format(null));
    }
}
