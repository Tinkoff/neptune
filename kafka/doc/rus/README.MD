# Интеграция с Apache Kafka-client

Данный модуль предоставляет удобную функциональность поверх клиента Kafka

## Оглавление

- [Настройка](./SETTINGS.MD)
    - [KAFKA_CONSUMER_PROPERTIES](./SETTINGS.MD#KAFKA_CONSUMER_PROPERTIES)
    - [KAFKA_PRODUCER_PROPERTIES](./SETTINGS.MD#KAFKA_PRODUCER_PROPERTIES)
    - [KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER)
    - [DEFAULT_TOPIC_FOR_SEND](./SETTINGS.MD#DEFAULT_TOPIC_FOR_SEND)
    - [DEFAULT_TOPICS_FOR_POLL](./SETTINGS.MD#DEFAULT_TOPICS_FOR_POLL)
    - [KAFKA_CALL_BACK](./SETTINGS.MD#KAFKA_CALL_BACK)
    - [KAFKA_KEY_TRANSFORMER](./SETTINGS.MD#KAFKA_KEY_TRANSFORMER)

- [Работа с RabbitMQ](./FUNCTION.MD)
    - [Получение сообщения](./FUNCTION.MD#Получение-сообщения)
        - [Получение данных сообщения как List](./FUNCTION.MD#Получение-данных-сообщения-как-List)
        - [Получение данных сообщения в виде массива](./FUNCTION.MD#Получение-данных-сообщения-в-виде-массива)
        - [Получение данных сообщения как Java-объект из Iterable](./FUNCTION.MD#Получение-данных-сообщения-как-Java-объект-из-Iterable)
    - [Публикация сообщений](./FUNCTION.MD#Публикация-сообщений)
