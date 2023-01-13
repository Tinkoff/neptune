# Declare - операции

Полезные ссылки:
- [Свойство RABBIT_MQ_DEFAULT_EXCHANGE_NAME](settings/RABBIT_MQ_DEFAULT_EXCHANGE_NAME.md)
- [Свойство RABBIT_MQ_DEFAULT_QUEUE_NAME](settings/RABBIT_MQ_DEFAULT_QUEUE_NAME.md)
- [Шаги, выполняющие действие](../../quick_start/steps/pattern_steps/action_steps.md)

## Exchange declare

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.DeclareExchangeParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().declare(newExchange("exchange") //название 
                .type("type") //тип можно указать
                .durable() //можно указать
                .autoDelete() //можно указать
                .internal() //можно указать
                .argument("key", "value") // можно указать набор дополнительных 
                .argument("key2", "value2")); //параметров
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.DeclareExchangeParameters.*;


public class MyTest {

    @Test
    public void myTest() {
        //в качестве имени нового exchange используется 
        // значение RABBIT_MQ_DEFAULT_EXCHANGE_NAME
        rabbitMq().declare(newExchange()
            //можно указать 
            //дополнительные параметры
        ); 
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.DeclareExchangeParameters.*;


public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().declare(newExchange("exchange")
                //Пассивное объявление exchange
                .passive() 
                /*.type("type") //игнорируется
                .durable() //игнорируется
                .autoDelete() //игнорируется
                .internal() //игнорируется
                .argument("key", "value") //игнорируется
                .argument("key2", "value2")*/); //игнорируется
    }
}
```

## Queue declare

```java

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.DeclareQueueParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().declare(newQueue("queue") //название 
                .exclusive() //можно указать
                .autoDelete() //можно указать
                .durable() //можно указать
                .argument("name", "value")// можно указать набор дополнительных 
                .argument("name2", "value2")); //параметров
    }
}
```

```java

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.DeclareQueueParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве имени новой очереди используется 
        // значение RABBIT_MQ_DEFAULT_QUEUE_NAME
        rabbitMq().declare(newQueue()
            //можно указать 
            //дополнительные параметры
        );
    }
}
```

```java

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.ServerNamedQueueSequentialGetSupplier.*;

public class MyTest {

    @Test
    public void myTest() {
      //Создание новой очереди самим сервером без передачи параметров  
      //возвращается имя новой очереди  
      String queue = rabbitMq().declare(newQueueServerNamed());
    }
}
```