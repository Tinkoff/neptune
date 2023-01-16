# Bind / Unbind - операции

Полезные ссылки:
- [Свойство RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME](settings/RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME.md)
- [Свойство RABBIT_MQ_DEFAULT_EXCHANGE_NAME](settings/RABBIT_MQ_DEFAULT_EXCHANGE_NAME.md)
- [Свойство RABBIT_MQ_DEFAULT_QUEUE_NAME](settings/RABBIT_MQ_DEFAULT_QUEUE_NAME.md)
- [Шаги, выполняющие действие](../../quick_start/steps/pattern_steps/action_steps.md)

## Bind

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //bind source exchange 
        //to destination exchange
        rabbitMq().bind(exchanges(
                "source", //указываются source 
                "destination") //и destination
                .withRoutingKey("routingKey") //можно указать ключ маршрутизации 
                .argument("key", "value") // можно указать набор дополнительных 
                .argument("key2", "value2")); //параметров
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().bind(exchanges(
            /*параметры*/)
            //в качестве ключа маршрутизации используется
            .withDefaultRoutingKey() //значение RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME
            //можно указать 
            //дополнительные параметры
        ); 
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве source exchange 
        //используется значение RABBIT_MQ_DEFAULT_EXCHANGE_NAME
        rabbitMq().bind(destinationExchange("destination")
                //можно указать 
                //дополнительные параметры
            )
            //в качестве destination exchange 
            //используется значение RABBIT_MQ_DEFAULT_EXCHANGE_NAME
            .bind(sourceExchange("source")
                //можно указать 
                //дополнительные параметры
            );
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //bind exchange and queue
        rabbitMq().bind(queueAndExchange(
            "queue", //очередь
            "exchange") // exchange
            .withRoutingKey("routingKey") //можно указать ключ маршрутизации 
             .argument("testKey", "testValue") // можно указать набор дополнительных 
             .argument("testKey", "testValue")); //параметров
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().bind(queueAndExchange(
            /*параметры*/) 
            //в качестве ключа маршрутизации используется
            .withDefaultRoutingKey() //значение RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME
            //можно указать 
            //дополнительные параметры
        );
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве exchange используется 
        // значение RABBIT_MQ_DEFAULT_EXCHANGE_NAME
        rabbitMq().bind(queueAndDefaultExchange("queue")
                //можно указать 
                //дополнительные параметры
            )
            //в качестве queue используется 
            // значение RABBIT_MQ_DEFAULT_QUEUE_NAME
            .bind(defaultQueueAndExchange("exchange")
                //можно указать 
                //дополнительные параметры
            );
    }
}
```

## Unbind

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //unbind source exchange 
        //and destination exchange
        rabbitMq().unbind(exchanges(
            "source", //указываются source 
            "destination") //и destination
            .withRoutingKey("routingKey") //можно указать ключ маршрутизации 
            .argument("key", "value") // можно указать набор дополнительных 
            .argument("key2", "value2")); //параметров
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().unbind(exchanges(
            /*параметры*/)
            //в качестве ключа маршрутизации используется
            .withDefaultRoutingKey() //значение RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME
            //можно указать 
            //дополнительные параметры
        ); 
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве source exchange 
        //используется значение RABBIT_MQ_DEFAULT_EXCHANGE_NAME
        rabbitMq().unbind(destinationExchange("destination")
                //можно указать 
                //дополнительные параметры
            )
            //в качестве destination exchange 
            //используется значение RABBIT_MQ_DEFAULT_EXCHANGE_NAME
            .unbind(sourceExchange("source")
                //можно указать 
                //дополнительные параметры
            );
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //unbind exchange and queue
        rabbitMq().unbind(queueAndExchange(
            "queue", //очередь
            "exchange") // exchange
            .withRoutingKey("routingKey") //можно указать ключ маршрутизации 
            .argument("key", "value") // можно указать набор дополнительных 
            .argument("key2", "value2")); //параметров
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().unbind(queueAndExchange(
            /*параметры*/) 
            //в качестве ключа маршрутизации используется
            .withDefaultRoutingKey() //значение RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME
            //можно указать 
            //дополнительные параметры
        );
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве exchange используется 
        // значение RABBIT_MQ_DEFAULT_EXCHANGE_NAME
        rabbitMq().unbind(queueAndDefaultExchange("queue")
                //можно указать 
                //дополнительные параметры
            )
            //в качестве queue используется 
            // значение RABBIT_MQ_DEFAULT_QUEUE_NAME
            .unbind(defaultQueueAndExchange("exchange")
                //можно указать 
                //дополнительные параметры
            );
    }
}
```