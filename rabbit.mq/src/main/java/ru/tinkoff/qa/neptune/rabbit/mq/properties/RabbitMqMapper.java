package ru.tinkoff.qa.neptune.rabbit.mq.properties;

public interface RabbitMqMapper {

    <T> T deserialize(String message, Class<T> cls);

    String serialize(Object obj);
}
