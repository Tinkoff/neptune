# RABBIT_MQ_DEFAULT_EXCHANGE_NAME

Настройка, в которой указывается имя часто используемого exchange

```properties
#Значение свойства указывается так
RABBIT_MQ_DEFAULT_EXCHANGE_NAME=exchange.name
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_EXCHANGE_NAME;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        String exchangeName = DEFAULT_EXCHANGE_NAME.get();
    }
}
```