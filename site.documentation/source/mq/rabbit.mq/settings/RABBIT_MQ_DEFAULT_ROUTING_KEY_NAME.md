# RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME

Настройка, в которой указывается имя часто используемого ключа маршрутизации

```properties
#Значение свойства указывается так
RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME=routing_key_name
```

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_ROUTING_KEY_NAME;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        String routingKey = DEFAULT_ROUTING_KEY_NAME.get();
    }
}
```