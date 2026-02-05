package com.provisa.telegram.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("telegram")
public record TelegramProperties(
        String token,
        boolean enabled
) {
}
