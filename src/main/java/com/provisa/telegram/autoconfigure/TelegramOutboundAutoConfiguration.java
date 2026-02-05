package com.provisa.telegram.autoconfigure;

import com.provisa.telegram.config.TelegramProperties;
import com.provisa.telegram.outbound.DefaultTelegramMessageSender;
import com.provisa.telegram.outbound.TelegramMessageFormatter;
import com.provisa.telegram.outbound.TelegramMessageSender;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

@AutoConfiguration
@ConditionalOnProperty(prefix = "telegram", name = "token")
public class TelegramOutboundAutoConfiguration {

    @AutoConfiguration
    static class Beans {

        public TelegramMessageFormatter telegramMessageFormatter() {
            return new TelegramMessageFormatter();
        }

        public TelegramClient telegramClient(TelegramProperties props) {
            return new OkHttpTelegramClient(props.token());
        }

        public TelegramMessageSender telegramMessageSender(
                TelegramClient client,
                TelegramMessageFormatter formatter
        ) {
            return new DefaultTelegramMessageSender(client, formatter);
        }
    }
}
