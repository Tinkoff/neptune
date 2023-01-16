# Delete - операции

Полезные ссылки:
- [Свойство RABBIT_MQ_DEFAULT_EXCHANGE_NAME](settings/RABBIT_MQ_DEFAULT_EXCHANGE_NAME.md)
- [Свойство RABBIT_MQ_DEFAULT_QUEUE_NAME](settings/RABBIT_MQ_DEFAULT_QUEUE_NAME.md)
- [Шаги, выполняющие действие](../../quick_start/steps/pattern_steps/action_steps.md)

## Exchange delete

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.delete.ExchangeDeleteParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().delete(exchange("exchange2") //имя exchange
                .ifUnused()); //if unused
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.delete.ExchangeDeleteParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве имени удаляемого exchange используется значение 
        //RABBIT_MQ_DEFAULT_EXCHANGE_NAME
        rabbitMq().delete(exchange()
            //можно указать 
            //дополнительные параметры
        );
    }
}
```

## Queue delete

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.delete.QueueDeleteParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().delete(queue(
                "queue") //имя очереди
                .ifUnused() //if unused
                .ifEmpty()); //if empty
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.delete.QueueDeleteParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве имени удаляемой очереди используется 
        //значение RABBIT_MQ_DEFAULT_QUEUE_NAME
        rabbitMq().delete(queue()
            //можно указать 
            //дополнительные параметры
        ); 
    }
}
```
