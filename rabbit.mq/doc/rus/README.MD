# Интеграция с RabbitMQ AMQP-client

Данный модуль предоставляет удобную функциональность поверх клиента RabbitMq

## Оглавление

- [Настройка](./SETTINGS.MD)
    - [RABBIT_AMQP_PROPERTY](./SETTINGS.MD#RABBIT_AMQP_PROPERTY)
    - [RABBIT_MQ_PASSWORD](./SETTINGS.MD#RABBIT_MQ_PASSWORD)
    - [RABBIT_MQ_USERNAME](./SETTINGS.MD#RABBIT_MQ_USERNAME)
    - [RABBIT_MQ_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#RABBIT_MQ_DEFAULT_DATA_TRANSFORMER)
    - [RABBIT_MQ_CLUSTER](./SETTINGS.MD#RABBIT_MQ_CLUSTER)
    - [RABBIT_MQ_DEFAULT_EXCHANGE_NAME](./SETTINGS.MD#rabbit_mq_default_exchange_name)
    - [RABBIT_MQ_DEFAULT_QUEUE_NAME](./SETTINGS.MD#rabbit_mq_default_queue_name)
    - [RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME](./SETTINGS.MD#rabbit_mq_default_routing_key_name)
  

- [Работа с RabbitMQ](./FUNCTION.MD)
    - [Связывание](./FUNCTION.MD#Связывание)
        - [exchange к exchange](./FUNCTION.MD#exchange-к-exchange)
        - [queue к exchange](./FUNCTION.MD#queue-к-exchange)
    - [Отвязывание](./FUNCTION.MD#Отвязывание)
        - [exchange от exchange](./FUNCTION.MD#exchange-от-exchange)
        - [queue от exchange](./FUNCTION.MD#queue-от-exchange)
    - [Объявление](./FUNCTION.MD#Объявление)
        - [Объявление exchange](./FUNCTION.MD#Объявление-exchange)
        - [Объявление queue](./FUNCTION.MD#Объявление-queue)
    - [Удаление](./FUNCTION.MD#Удаление)
        - [Удаление exchange](./FUNCTION.MD#Удаление-exchange)
        - [Удаление queue](./FUNCTION.MD#Удаление-queue)
    - [Получение сообщения](./FUNCTION.MD#Получение-сообщения)
        - [Получение данных сообщения как Java-объект](./FUNCTION.MD#Получение-данных-сообщения-как-Java-объект)
            - [Получение сообщения в виде строки](./FUNCTION.MD#Получение-сообщения-в-виде-строки)
            - [Получение объекта при помощи десериализации](./FUNCTION.MD#Получение-объекта-при-помощи-десериализации)
        - [Получение данных сообщения как Iterable](./FUNCTION.MD#Получение-данных-сообщения-как-Iterable)
        - [Получение данных сообщения в виде массива](./FUNCTION.MD#Получение-данных-сообщения-в-виде-массива)
        - [Получение данных сообщения как Java-объект из Iterable](./FUNCTION.MD#Получение-данных-сообщения-как-Java-объект-из-Iterable)
        - [Получение данных сообщения как Java-объект из массива](./FUNCTION.MD#Получение-данных-сообщения-как-Java-объект-из-массива)
    - [Публикация сообщений](./FUNCTION.MD#Публикация-сообщений)
        - [Публикация текстового сообщения](./FUNCTION.MD#Публикация-текстового-сообщения)
        - [Публикация сериализованного объекта](./FUNCTION.MD#Публикация-сериализованного-объекта)
    - [Очистка очереди](./FUNCTION.MD#Очистка-очереди)