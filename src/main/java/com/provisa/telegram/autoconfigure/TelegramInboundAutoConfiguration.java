package com.provisa.telegram.autoconfigure;

import com.provisa.telegram.config.TelegramProperties;
import com.provisa.telegram.inbound.TelegramInboundAdapter;
import com.provisa.telegram.inbound.TelegramInboundSink;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@AutoConfiguration
@EnableConfigurationProperties(TelegramProperties.class)
@ConditionalOnProperty(prefix = "telegram", name = "enabled", havingValue = "true")
public class TelegramInboundAutoConfiguration implements AutoCloseable {

    private final TelegramBotsLongPollingApplication application;

    public TelegramInboundAutoConfiguration(
            TelegramProperties properties,
            TelegramInboundSink sink,
            TelegramBotsLongPollingApplication application
    ) {
        this.application = application;
        try {
            this.application.registerBot(
                    properties.token(),
                    new TelegramInboundAdapter(sink)
            );
        } catch (TelegramApiException e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    TelegramBotsLongPollingApplication telegramBotsLongPollingApplication() {
        return new TelegramBotsLongPollingApplication();
    }

    @Bean
    @ConditionalOnMissingBean
    TelegramInboundSink telegramInboundSink() {
        return message -> {};
    }

    @Override
    public void close() throws Exception {
        application.close();
    }
}
