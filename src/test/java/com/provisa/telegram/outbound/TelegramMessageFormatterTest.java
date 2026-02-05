package com.provisa.telegram.outbound;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TelegramMessageFormatterTest {

    private final TelegramMessageFormatter formatter = new TelegramMessageFormatter();

    @Test
    void normalizeSpaces() {
        assertThat(formatter.format("hello   world"))
                .isEqualTo("hello world");
    }

    @Test
    void trimText() {
        assertThat(formatter.format("  hello  "))
                .isEqualTo("hello");
    }

    @Test
    void keepTextAsIs() {
        assertThat(formatter.format("service check status"))
                .isEqualTo("service check status");
    }

    @Test
    void rejectNull() {
        assertThatThrownBy(() -> formatter.format(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
