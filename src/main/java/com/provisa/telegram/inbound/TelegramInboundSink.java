package com.provisa.telegram.inbound;

@FunctionalInterface
public interface TelegramInboundSink {

    void accept(String message);

}
