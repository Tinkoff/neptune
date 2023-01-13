# Purge queue

Полезные ссылки:
- [Свойство RABBIT_MQ_DEFAULT_QUEUE_NAME](settings/RABBIT_MQ_DEFAULT_QUEUE_NAME.md)
- [Шаги, выполняющие действие](../../quick_start/steps/pattern_steps/action_steps.md)

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем очистку содержимого очереди
        rabbitMq().purgeQueue("queue");
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве имени очереди используется 
        //значение RABBIT_MQ_DEFAULT_QUEUE_NAME
        rabbitMq().purgeQueue();
    }
}
```