# Поддерживаемые версии amqp-client

Версии Neptune до `0.24.6-ALPHA` предоставляли следующую зависимость от amqp-client транзитивно:

- `group: 'com.rabbitmq', name: 'amqp-client', version: '5.14.2'`

Начиная с v`0.24.6-ALPHA` эта зависимость транзитивной не является. Ниже таблица версий Neptune
и поддерживаемых ими версий amqp-client:

| Диапазон версий Neptune | Диапазон поддерживаемых версий amqp-client |
|-------------------------|--------------------------------------------|
| [0.24.6-ALPHA,)         | [5.13.0,)                                  |
|                         |                                            |
|                         |                                            |