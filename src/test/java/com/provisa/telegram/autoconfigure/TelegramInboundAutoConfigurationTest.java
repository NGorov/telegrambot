package com.provisa.telegram.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramInboundAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(
                            AutoConfigurations.of(TelegramInboundAutoConfiguration.class)
                    )
                    .withPropertyValues(
                            "telegram.enabled=false",
                            "telegram.token=dummy"
                    );

    @Test
    void inboundIsNotStartedWhenDisabled() {
        contextRunner.run(context -> {
            assertThat(context.getBeansOfType(TelegramBotsLongPollingApplication.class))
                    .isEmpty();
        });
    }
}
