# RABBIT_MQ_DEFAULT_QUEUE_NAME

Настройка, в которой указывается имя часто используемой очереди

```properties
#Значение свойства указывается так
RABBIT_MQ_DEFAULT_QUEUE_NAME=queue_name
```

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMQRoutingProperties.DEFAULT_QUEUE_NAME;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        String queueName = DEFAULT_QUEUE_NAME.get();
    }
}
```