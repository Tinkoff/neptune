package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.clients.producer.Callback;

import java.util.function.Supplier;

public class DefaultCallBackSupplier implements Supplier<Callback> {

    public static final Callback CALLBACK = (metadata, exception) -> {
    };

    @Override
    public Callback get() {
        return CALLBACK;
    }
}
