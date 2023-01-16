# RABBIT_MQ_CLUSTER

Настройка позволяет указать список конечных точек, которые будут использоваться при подключении. Будет использоваться
первая достижимая конечная точка. В случае сбоев подключения использование списка конечных точек позволяет приложению
подключиться к другому узлу, если исходный узел не работает.

```properties
#Значение свойства указывается так
RABBIT_MQ_CLUSTER_PROPERTY=host:port,host:port
```

```java
import com.rabbitmq.client.Address;

import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqClusterProperty.RABBIT_MQ_CLUSTER_PROPERTY;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        Addreses[] addresses = RABBIT_MQ_CLUSTER_PROPERTY.get();
    }
}
```