package com.provisa.telegram.autoconfigure;

import com.provisa.telegram.inbound.TelegramInboundSink;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramInboundAutoConfigurationEnabledTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(
                            AutoConfigurations.of(TelegramInboundAutoConfiguration.class)
                    )
                    .withPropertyValues(
                            "telegram.enabled=true",
                            "telegram.token=dummy"
                    )
                    .withBean(
                            TelegramBotsLongPollingApplication.class,
                            () -> Mockito.mock(TelegramBotsLongPollingApplication.class)
                    )
                    .withBean(
                            TelegramInboundSink.class,
                            () -> message -> {}
                    );

    @Test
    void inboundIsStartedWhenEnabled() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(TelegramInboundAutoConfiguration.class);
        });
    }
}
