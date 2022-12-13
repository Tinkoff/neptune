package ru.tinkoff.qa.neptune.kafka;

import org.apache.kafka.common.serialization.Deserializer;

final class InnerDeserializer<T> implements Deserializer<T> {

    private final Deserializer<T> wrapped;

    public InnerDeserializer(Deserializer<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return wrapped.deserialize(topic, data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
