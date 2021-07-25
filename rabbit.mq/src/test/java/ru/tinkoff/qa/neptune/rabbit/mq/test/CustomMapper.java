package ru.tinkoff.qa.neptune.rabbit.mq.test;

import ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqMapper;

public class CustomMapper implements RabbitMqMapper {

    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        return (T) new Object();
    }

    @Override
    public String serialize(Object obj) {
        return "customSerialize";
    }
}