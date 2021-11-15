# Работа с RabbitMq

В данном документе описано на примерах, как работать с брокером сообщений

## Оглавление

- [Связывание](#Связывание)
  - [exchange к exchange](#exchange-к-exchange)
  - [queue к exchange](#queue-к-exchange)
- [Отвязывание](#Отвязывание)
  - [exchange от exchange](#exchange-от-exchange)
  - [queue от exchange](#queue-от-exchange)
- [Объявление](#Объявление)
  - [Объявление exchange](#Объявление-exchange)
  - [Объявление queue](#Объявление-queue)
- [Удаление](#Удаление)
  - [Удаление exchange](#Удаление-exchange)
  - [Удаление queue](#Удаление-queue)
- [Получение сообщения](#Получение-сообщения)
  - [Получение данных сообщения как Java-объект](#Получение-данных-сообщения-как-Java-объект)
    - [Получение сообщения в виде строки](#Получение-сообщения-в-виде-строки)
    - [Получение объекта при помощи десериализации](#Получение-объекта-при-помощи-десериализации)
  - [Получение данных сообщения как List](#Получение-данных-сообщения-как-List)
  - [Получение данных сообщения в виде массива](#Получение-данных-сообщения-в-виде-массива)
  - [Получение данных сообщения как Java-объект из Iterable](#Получение-данных-сообщения-как-Java-объект-из-Iterable)
  - [Получение данных сообщения как Java-объект из массива](#Получение-данных-сообщения-как-Java-объект-из-массива)
- [Публикация сообщений](#Публикация-сообщений)
  - [Публикация текстового сообщения](#Публикация-текстового-сообщения)
  - [Публикация сериализованного объекта](#Публикация-сериализованного-объекта)
- [Очистка очереди](#Очистка-очереди)

## Связывание

### exchange к exchange

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().bind(exchanges(
                "source", //указываются source и destination
                "destination")
                .withRoutingKey("routingKey") //можно указать ключ маршрутизации 
                .argument("key", "value") // можно указать набор дополнительных 
                .argument("key2", "value2")); //параметров
    }
}
```

Если в качестве ключа маршрутизации должно быть использовано значение свойства / переменной окружения 
[RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME), то 

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().bind(exchanges(
                "source",
                "destination")
                //в качестве ключа маршрутизации используется
                .withDefaultRoutingKey()); //значение RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME
    }
}
```

Можно использовать значение свойства / переменной окружения [RABBIT_MQ_DEFAULT_EXCHANGE_NAME](./SETTINGS.MD#rabbit_mq_default_exchange_name)
в качестве `source` или `destination`.

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве source используется значение RABBIT_MQ_DEFAULT_EXCHANGE_NAME
        rabbitMq().bind(destinationExchange("destination"))
                //в качестве destination используется значение RABBIT_MQ_DEFAULT_EXCHANGE_NAME
                .bind(sourceExchange("source"));
    }
}
```

### queue к exchange

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().bind(queueAndExchange(
                "queue", //очередь
                "exchange") // exchange
                .withRoutingKey("routingKey") //можно указать ключ маршрутизации 
                .argument("testKey", "testValue") // можно указать набор дополнительных 
                .argument("testKey", "testValue")); //параметров
    }
}
```

Если в качестве ключа маршрутизации должно быть использовано значение свойства / переменной окружения
[RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME), то

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().bind(queueAndExchange(
                "queue", 
                "exchange") 
                //в качестве ключа маршрутизации используется
                .withDefaultRoutingKey()); //значение RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME
    }
}
```

Можно использовать значения свойств / переменных окружения [RABBIT_MQ_DEFAULT_EXCHANGE_NAME](./SETTINGS.MD#rabbit_mq_default_exchange_name)
и [RABBIT_MQ_DEFAULT_QUEUE_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_QUEUE_NAME).

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве exchange используется значение RABBIT_MQ_DEFAULT_EXCHANGE_NAME
        rabbitMq().bind(queueAndDefaultExchange("queue")
                //в качестве queue используется значение RABBIT_MQ_DEFAULT_QUEUE_NAME
                .defaultQueueAndExchange("exchange"));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Отвязывание

### exchange от exchange

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().unbind(exchanges(/* параметры */)
                /*дополнительные параметры*/);
    }
}
```

Варианты вызова статических методов `ru.tinkoff.qa.neptune.rabbit.mq.function.binding.ExchangesBindUnbindParameters.*` 
описаны [выше](#exchange-к-exchange) 

### queue от exchange

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().unbind(queueAndExchange(/* параметры */)
                /*дополнительные параметры*/);
    }
}
```

Варианты вызова статических методов `ru.tinkoff.qa.neptune.rabbit.mq.function.binding.QueueBindUnbindParameters.`
описаны [выше](#queue-к-exchange)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Объявление

### Объявление exchange

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

Можно использовать значение свойства / переменной окружения [RABBIT_MQ_DEFAULT_EXCHANGE_NAME](./SETTINGS.MD#rabbit_mq_default_exchange_name)

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.DeclareExchangeParameters.*;


public class MyTest {

    @Test
    public void myTest() {
        //в качестве имени нового exchange используется 
        // значение RABBIT_MQ_DEFAULT_EXCHANGE_NAME
        rabbitMq().declare(newExchange()); 
    }
}
```

Пассивное объявление exchange

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.DeclareExchangeParameters.*;


public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().declare(newExchange("exchange")
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


### Объявление queue

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

Можно использовать значение свойства / переменной окружения [RABBIT_MQ_DEFAULT_QUEUE_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_QUEUE_NAME).

```java

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.DeclareQueueParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве имени новой очереди используется 
        // значение RABBIT_MQ_DEFAULT_QUEUE_NAME
        rabbitMq().declare(newQueue());
    }
}
```

Создание новой очереди самим сервером без передачи параметров

```java

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.ServerNamedQueueSequentialGetSupplier.*;

public class MyTest {

    @Test
    public void myTest() {
      //возвращается имя новой очереди  
      String queue = rabbitMq().declare(newQueueServerNamed());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Удаление

### Удаление exchange

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

Можно использовать значение свойства / переменной окружения [RABBIT_MQ_DEFAULT_EXCHANGE_NAME](./SETTINGS.MD#rabbit_mq_default_exchange_name)

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.delete.ExchangeDeleteParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве имени удаляемого exchange используется значение 
        //RABBIT_MQ_DEFAULT_EXCHANGE_NAME
        rabbitMq().delete(exchange());
    }
}
```

### Удаление queue

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

Можно использовать значение свойства / переменной окружения [RABBIT_MQ_DEFAULT_QUEUE_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_QUEUE_NAME).

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.delete.QueueDeleteParameters.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве имени удаляемой очереди используется 
        //значение RABBIT_MQ_DEFAULT_QUEUE_NAME
        rabbitMq().delete(queue()); 
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Получение сообщения

О принципах работы шагов, которые возвращают результат, можно прочитать
[здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных сообщения как Java-объект

#### Получение сообщения в виде строки

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.*;

public class MyTest {

    @Test
    public void myTest() {
        // Возвращает сообщение из очереди.
        String result = rabbitMq().read(rabbitRawMessage("queue") //имя очереди
                //можно указать, что auto acknowledge
                .autoAck()
                //можно указать время на получение ожидаемого результата = 5 секунд
                .timeOut(ofSeconds(5))
                //можно указать критерий, которому должен соответствовать текст сообщения
                .criteria("String criteria", s -> /*описание критерия, которому должна соответствовать строка*/)
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult());
    }
}
```

Можно указать кодировку строки

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.*;
import static java.nio.charset.StandardCharsets.*;

public class MyTest {

    @Test
    public void myTest() {
        String result = rabbitMq().read(rabbitRawMessage("queue", UTF_8));
    }
}
```

Можно использовать значение свойства / переменной окружения [RABBIT_MQ_DEFAULT_QUEUE_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_QUEUE_NAME).

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.*;
import static java.nio.charset.StandardCharsets.*;

public class MyTest {

    @Test
    public void myTest() {
        //в качестве имени удаляемой очереди используется 
        //значение RABBIT_MQ_DEFAULT_QUEUE_NAME
        String result = rabbitMq().read(rabbitRawMessage());
        String result2 = rabbitMq().read(rabbitRawMessage(UTF_8));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Получение объекта при помощи десериализации

В примерах ниже предполагается, что настройка / свойство / переменная окружения
[RABBIT_MQ_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#RABBIT_MQ_DEFAULT_DATA_TRANSFORMER) перед началом выполнения имеет
непустое значения

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.rabbitBody;

public class MyTest {

    @Test
    public void myTest() {
        // Возвращает десериализованное сообщение из очереди.
        ClassToDeserialize result = rabbitMq().read(rabbitBody(
                "test_queue", //имя очереди
                ClassToDeserialize.class) //класс модели для десериализации
                //можно указать критерий, которому должно соответствовать тело сообщения
                .criteria("Описание критерия", o -> o.getSomeField().equals("someValue"))
                //можно указать время на получение ожидаемого результата = 5 секунд
                .timeOut(ofSeconds(5))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult()
                //можно указать, что auto acknowledge
                .autoAck());
    }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.rabbitBody;

public class MyTest {

    @Test
    public void myTest() {
        // Возвращает десериализованное сообщение из очереди.
        ClassToDeserialize<ParamClass> result = rabbitMq().read(rabbitBody(
                "test_queue",
                new TypeReference<ClassToDeserialize<ParamClass>>() {
                }) //тип объекта-модели для десериализации
                //можно указать критерий, которому должно соответствовать тело сообщения
                .criteria("Описание критерия", o -> o.getSomeField().equals("someValue"))
                //можно указать время на получение ожидаемого результата = 5 секунд
                .timeOut(ofSeconds(5))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult()
                //можно указать, что auto acknowledge
                .autoAck());
    }
}
```

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.rabbitObject;

public class MyTest {

    @Test
    public void myTest() {
        // Возвращается объект, получаемый из десериализованного сообщения 
        // или вычисляемый с использованием данных этого сообщения.
        Object result = rabbitMq().read(rabbitObject("Вычисляемый объект", //описываем 
                // то, что нужно получить
                "test_queue", //имя очереди
                ClassToDeserialize.class, //класс модели для десериализации
                o -> {
                    return /*описываем то, как получаем или вычисляем нужный объект*/
                })
                //можно указать критерий, которому должно соответствовать вычисленные значение
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue"))
                //можно указать время на получение ожидаемого результата = 5 секунд
                .timeOut(ofSeconds(5))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult()
                //можно указать, что auto acknowledge
                .autoAck());
    }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.rabbitObject;

public class MyTest {

    @Test
    public void myTest() {
        // Возвращается объект, получаемый из десериализованного сообщения 
        // или вычисляемый с использованием данных этого сообщения.
        Object result = rabbitMq().read(rabbitObject("Вычисляемый объект",
                "test_queue",
                new TypeReference<ClassToDeserialize<ParamClass>>() {
                }, //тип объекта-модели для десериализации
                o -> {
                    return /*описываем то, как получаем или вычисляем нужный объект*/
                })
                //можно указать критерий, которому должно соответствовать вычисленные значение
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue"))
                //можно указать время на получение ожидаемого результата = 5 секунд
                .timeOut(ofSeconds(5))
                //можно указать, что должно быть выброшено исключение, если результат не был получен
                .throwOnNoResult()
                //можно указать, что auto acknowledge
                .autoAck());
    }
}
```

Можно использовать значение свойства / переменной окружения [RABBIT_MQ_DEFAULT_QUEUE_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_QUEUE_NAME).
В этом случае в примерах выше не надо указывать имя очереди.

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.rabbitBody;

public class MyTest {

    @Test
    public void myTest() {
        ClassToDeserialize result = rabbitMq().read(rabbitBody(
                ClassToDeserialize.class) 
                //указание других опциональных параметров
                );
    }
}
```

Ниже примеры ситуаций, когда [десериализатор](./../../../core.api/doc/rus/SERIALIZATION_DESERIALIZATION.MD) нужно
передать явно.

```java
import org.my.pack;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

//Описываем сериализацию и десериализацию для текущего проекта
public class MyDataTransformer implements DataTransformer {


    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        //тут описываем механизм десериализации
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        //тут описываем механизм десериализации
    }

    @Override
    public String serialize(Object obj) {
        //тут описываем механизм сериализации
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.rabbitBody;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().read(rabbitBody(
                "test_queue",
                ClassToDeserialize.class) //или ссылка на тип
                /* +прочие параметры*/
                .withDataTransformer(new MyDataTransformer()));
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.rabbitObject;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().read(rabbitObject("Вычисляемый объект",
                "test_queue",
                ClassToDeserialize.class, //или ссылка на тип
                o -> {
                    return /*описываем то, как получаем или вычисляем нужный объект*/
                })
                /* +прочие параметры*/
                .withDataTransformer(new MyDataTransformer()));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных сообщения как List

В примерах ниже предполагается, что настройка / свойство / переменная окружения
[RABBIT_MQ_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#RABBIT_MQ_DEFAULT_DATA_TRANSFORMER) перед началом выполнения имеет
непустое значения

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableSupplier.rabbitIterable;

public class MyTest {

  @Test
  public void myTest() {
    // Возвращает сообщение из очереди, десериализованное в коллекцию/Iterable
    // Из общего сообщения (Iterable/коллекции) вернется под-множество(немутабельный лист) подходящих элементов.
    List<ClassToDeserialize> result = rabbitMq().read(rabbitIterable("Результирующий лист", //описываем то, 
            // что нужно получить
            "test_queue", //имя очереди
            ListOfClassToDeserialize.class) //класс, который реализует Iterable<ClassToDeserialize> 
            // или Collection<ClassToDeserialize>
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать каждый элемент результирующей коллекции
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .throwOnNoResult() //можно указать, что должно быть выброшено исключение, 
            // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableSupplier.rabbitIterable;

public class MyTest {

    @Test
    public void myTest() {
      // Возвращает сообщение из очереди, десериализованное в коллекцию/Iterable
      // Из общего сообщения (Iterable/коллекции) вернется под-множество(немутабельный лист) подходящих элементов.
      List<ClassToDeserialize> result = rabbitMq().read(rabbitIterable("Результирующий лист", //описываем то, 
                // что нужно получить
                "test_queue", //имя очереди
                new TypeReference<List<ClassToDeserialize>>() {
                }) //тип объекта-модели для десериализации
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать каждый элемент результирующей коллекции
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult() //можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableSupplier.rabbitIterable;

public class MyTest {

    @Test
    public void myTest() {
      // Возвращается немутабельный лист. Сообщение необязательно является коллекцией 
      // или объектом Iterable. Промежуточная коллекция или Iterable получаются в ходе дополнительных 
        // вычислений. Из коллекции вернется под-множество подходящих элементов.
        List<Object> result = rabbitMq().read(rabbitIterable("Результирующий лист", //описываем 
                // то, что нужно получить
                "test_queue", //имя очередий
                ClassToDeserialize.class, //класс модели для десериализации
                o -> {
                    return /*описываем то, как получаем или вычисляем List<Object>*/
                })
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать каждый элемент результирующей коллекции
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult() //можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableSupplier.rabbitIterable;

public class MyTest {

    @Test
    public void myTest() {
      // Возвращается немутабельный лист. Сообщение необязательно является коллекцией 
      // или объектом Iterable. Промежуточная коллекция или Iterable получаются в ходе дополнительных 
        // вычислений. Из коллекции вернется под-множество подходящих элементов.
        List<Object> result = rabbitMq().read(rabbitIterable("Результирующий лист",
                "test_queue",
                new TypeReference<ClassToDeserialize<ParamClass>>() {
                }, //тип объекта-модели для десериализации
                o -> {
                    return /*описываем то, как получаем или вычисляем List<Object>*/
                })
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать каждый элемент результирующей коллекции
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult() //можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

Можно использовать значение свойства / переменной окружения [RABBIT_MQ_DEFAULT_QUEUE_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_QUEUE_NAME).
В этом случае в примерах выше не надо указывать имя очереди.

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableSupplier.rabbitIterable;

public class MyTest {

  @Test
  public void myTest() {
    List<ClassToDeserialize> result = rabbitMq().read(rabbitIterable("Результирующий лист",
                    ListOfClassToDeserialize.class)//класс, который реализует Iterable<ClassToDeserialize> 
                // или Collection<ClassToDeserialize>
                //указание других опциональных параметров
                );
    }
}
```

Ниже примеры ситуаций, когда [десериализатор](./../../../core.api/doc/rus/SERIALIZATION_DESERIALIZATION.MD) нужно
передать явно.

```java
import org.my.pack;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

//Описываем сериализацию и десериализацию для текущего проекта
public class MyDataTransformer implements DataTransformer {


    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        //тут описываем механизм десериализации
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        //тут описываем механизм десериализации
    }

    @Override
    public String serialize(Object obj) {
        //тут описываем механизм сериализации
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableSupplier.rabbitIterable;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().read(rabbitIterable("Результирующий лист",
                "test_queue",
                ListOfClassToDeserialize.class, //или ссылка на тип
                /* +прочие параметры*/)
                /* +прочие параметры*/
                .withDataTransformer(new MyDataTransformer()));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных сообщения в виде массива

В примерах ниже предполагается, что настройка / свойство / переменная окружения
[RABBIT_MQ_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#RABBIT_MQ_DEFAULT_DATA_TRANSFORMER) перед началом выполнения имеет
непустое значения

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArraySupplier.rabbitArray;

public class MyTest {

    @Test
    public void myTest() {
        // Возвращает сообщение из очереди, десериализованное в массив
        // Из общего сообщения-массива вернется под-массив подходящих элементов.
        ClassToDeserialize[] result = rabbitMq().read(rabbitArray("Результирующий массив", //описываем то, 
                // что нужно получить
                "test_queue", //имя очереди
                ClassToDeserialize[].class) //класс массива
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать каждый элемент результирующего массива
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult() //можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArraySupplier.rabbitArray;

public class MyTest {

    @Test
    public void myTest() {
        // Возвращает сообщение из очереди, десериализованное в массив
        // Из общего сообщения-массива вернется под-массив подходящих элементов.
        ClassToDeserialize<ParamClass>[] result = rabbitMq().read(rabbitArray("Результирующий массив", //описываем то, 
                // что нужно получить
                "test_queue", //имя очереди
                new TypeReference<ClassToDeserialize<ParamClass>[]>() {
                }) //тип объекта-модели для десериализации
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать каждый элемент результирующего массива
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()//можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

```java

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArraySupplier.rabbitArray;

public class MyTest {

  @Test
  public void myTest() {
    // Возвращается массив. Сообщение необязательно является массивом. 
    // Промежуточный массив получается в ходе дополнительных 
    // вычислений. Из массива вернется под-массив подходящих элементов.
    Object[] result = rabbitMq().read(rabbitArray("Результирующий массив", //описываем 
            // то, что нужно получить
            "test_queue", //имя очереди
            ClassToDeserialize.class, //класс модели для десериализации
            o -> {
              return /*описываем то, как получаем или вычисляем Object[]*/
            })
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать каждый элемент результирующего массива
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()//можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArraySupplier.rabbitArray;

public class MyTest {

  @Test
  public void myTest() {
    // Возвращается массив. Сообщение необязательно является массивом. 
    // Промежуточный массив получается в ходе дополнительных 
    // вычислений. Из массива вернется под-массив подходящих элементов.
    Object[] result = rabbitMq().read(rabbitArray("Результирующий массив",
            "test_queue",
            new TypeReference<ClassToDeserialize<ParamClass>>() {
            }, //тип объекта-модели для десериализации
            o -> {
              return /*описываем то, как получаем или вычисляем Object[]*/
            })
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать каждый элемент результирующего массива
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()//можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

Можно использовать значение свойства / переменной окружения [RABBIT_MQ_DEFAULT_QUEUE_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_QUEUE_NAME).
В этом случае в примерах выше не надо указывать имя очереди.

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArraySupplier.rabbitArray;

public class MyTest {

    @Test
    public void myTest() {
        ClassToDeserialize result = rabbitMq().read(rabbitArray("Результирующий массив",
                ClassToDeserialize[].class)
                //указание других опциональных параметров
                );
    }
}
```

Ниже примеры ситуаций, когда [десериализатор](./../../../core.api/doc/rus/SERIALIZATION_DESERIALIZATION.MD) нужно
передать явно.

```java
import org.my.pack;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

//Описываем сериализацию и десериализацию для текущего проекта
public class MyDataTransformer implements DataTransformer {


    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        //тут описываем механизм десериализации
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        //тут описываем механизм десериализации
    }

    @Override
    public String serialize(Object obj) {
        //тут описываем механизм сериализации
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArraySupplier.rabbitArray;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().read(rabbitArray("Результирующий массив",
                "test_queue",
                ClassToDeserialize[].class, //или ссылка на тип
                /* +прочие параметры*/)
                /* +прочие параметры*/
                .withDataTransformer(new MyDataTransformer()));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных сообщения как Java-объект из Iterable

В примерах ниже предполагается, что настройка / свойство / переменная окружения
[RABBIT_MQ_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#RABBIT_MQ_DEFAULT_DATA_TRANSFORMER) перед началом выполнения имеет
непустое значения

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableItemSupplier.rabbitIterableItem;

public class MyTest {

    @Test
    public void myTest() {
        //Возвращается элемент коллекции / Iterable. Сообщение из очереди десериализуется в коллекцию/Iterable
        //Из общего сообщения (Iterable/коллекции) вернется первый подходящий элемент.
        ClassToDeserialize result = rabbitMq().read(rabbitIterableItem("Нужный элемент листа", //описываем то, 
                // что нужно получить
                "test_queue", //имя очереди
                ListOfClassToDeserialize.class) //класс, который реализует Iterable<ClassToDeserialize> 
                // или Collection<ClassToDeserialize>
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать результирующий элемент из коллекции
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()//можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableItemSupplier.rabbitIterableItem;

public class MyTest {

    @Test
    public void myTest() {
        //Возвращается элемент коллекции / Iterable. Сообщение из очереди десериализуется в коллекцию/Iterable
        //Из общего сообщения (Iterable/коллекции) вернется первый подходящий элемент.
        ClassToDeserialize result = rabbitMq().read(rabbitIterableItem("Нужный элемент листа",
                "test_queue",
                new TypeReference<List<ClassToDeserialize>>() {
                }) //тип объекта-модели для десериализации
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать результирующий элемент из коллекции
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()//можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableItemSupplier.rabbitIterableItem;

public class MyTest {

    @Test
    public void myTest() {
      //Возвращается элемент коллекции / Iterable.  Сообщение необязательно является коллекцией 
      // или объектом Iterable. Промежуточная коллекция или Iterable получаются в ходе дополнительных 
        // вычислений. Из коллекции вернется первый подходящий элемент.
        Object result = rabbitMq().read(rabbitIterableItem("Нужный элемент листа", //описываем 
                // то, что нужно получить
                "test_queue", //имя очереди
                ClassToDeserialize.class, //класс модели для десериализации
                o -> {
                    return /*описываем то, как получаем или вычисляем List<Object>*/
                })
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать результирующий элемент из коллекции
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()//можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableItemSupplier.rabbitIterableItem;

public class MyTest {

    @Test
    public void myTest() {
      //Возвращается элемент коллекции / Iterable.  Сообщение необязательно является коллекцией 
      // или объектом Iterable. Промежуточная коллекция или Iterable получаются в ходе дополнительных 
        // вычислений. Из коллекции вернется первый подходящий элемент.
        Object result = rabbitMq().read(rabbitIterableItem("Нужный элемент листа",
                "test_queue",
                new TypeReference<ClassToDeserialize<ParamClass>>() {
                }, //тип объекта-модели для десериализации
                o -> {
                    return /*описываем то, как получаем или вычисляем List<Object>*/
                })
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать результирующий элемент из коллекции
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()//можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

Можно использовать значение свойства / переменной окружения [RABBIT_MQ_DEFAULT_QUEUE_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_QUEUE_NAME).
В этом случае в примерах выше не надо указывать имя очереди.

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableItemSupplier.rabbitIterableItem;

public class MyTest {

    @Test
    public void myTest() {
        ClassToDeserialize result = rabbitMq().read(rabbitIterableItem("Нужный элемент листа", 
                ListOfClassToDeserialize.class) //класс, который реализует Iterable<ClassToDeserialize>
                // или Collection<ClassToDeserialize>
                //указание других опциональных параметров
                );
    }
}
```

Ниже примеры ситуаций, когда [десериализатор](./../../../core.api/doc/rus/SERIALIZATION_DESERIALIZATION.MD) нужно
передать явно.

```java
import org.my.pack;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

//Описываем сериализацию и десериализацию для текущего проекта
public class MyDataTransformer implements DataTransformer {


    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        //тут описываем механизм десериализации
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        //тут описываем механизм десериализации
    }

    @Override
    public String serialize(Object obj) {
        //тут описываем механизм сериализации
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableItemSupplier.rabbitIterableItem;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().read(rabbitIterableItem("Нужный элемент листа",
                "test_queue",
                ListOfClassToDeserialize.class, //или ссылка на тип
                /* +прочие параметры*/)
                /* +прочие параметры*/
                .withDataTransformer(new MyDataTransformer()));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных сообщения как Java-объект из массива

В примерах ниже предполагается, что настройка / свойство / переменная окружения
[RABBIT_MQ_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#RABBIT_MQ_DEFAULT_DATA_TRANSFORMER) перед началом выполнения имеет
непустое значения

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArrayItemSupplier.rabbitArrayItem;

public class MyTest {

    @Test
    public void myTest() {
        //Возвращается элемент массива. Сообщение из очереди десериализуется в массив.
        //Из общего сообщения-массива вернется первый подходящий элемент.
        ClassToDeserialize result = rabbitMq().read(rabbitArrayItem("Нужный элемент массива", //описываем то, 
                // что нужно получить
                "test_queue", //имя очереди
                ClassToDeserialize[].class) //класс массива
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать результирующий элемент из массива
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()//можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArrayItemSupplier.rabbitArrayItem;

public class MyTest {

    @Test
    public void myTest() {
        //Возвращается элемент массива. Сообщение из очереди десериализуется в массив.
        //Из общего сообщения-массива вернется первый подходящий элемент.
        ClassToDeserialize<ParamClass> result = rabbitMq().read(rabbitArrayItem("Нужный элемент массива",
                "test_queue",
                new TypeReference<ClassToDeserialize<ParamClass>[]>() {
                }) //тип объекта-модели для десериализации
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать результирующий элемент из массива
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()//можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArrayItemSupplier.rabbitArrayItem;

public class MyTest {

  @Test
  public void myTest() {
    //Возвращается элемент массива. Сообщение необязательно является массивом. 
    // Промежуточный массив получается в ходе дополнительных вычислений. 
    // Из массива вернется первый подходящий элемент.
    Object result = rabbitMq().read(rabbitArrayItem("Нужный элемент массива", //описываем 
            // то, что нужно получить
            "test_queue", //имя очереди
            ClassToDeserialize.class, //класс модели для десериализации
            o -> {
              return /*описываем то, как получаем или вычисляем Object[]*/
            })
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать результирующий элемент массива
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .throwOnNoResult()//можно указать, что должно быть выброшено исключение, 
            // если результат не был получен
            .autoAck()); //можно указать, что auto acknowledge
  }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArrayItemSupplier.rabbitArrayItem;

public class MyTest {

  @Test
  public void myTest() {
    //Возвращается элемент массива. Сообщение необязательно является массивом. 
    // Промежуточный массив получается в ходе дополнительных вычислений. 
    // Из массива вернется первый подходящий элемент.
    Object result = rabbitMq().read(rabbitArrayItem("Нужный элемент массива",
            "test_queue",
            new TypeReference<ClassToDeserialize<ParamClass>>() {
            }, //тип объекта-модели для десериализации
            o -> {
              return /*описываем то, как получаем или вычисляем Object[]*/
            })
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать результирующий элемент из массива
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()//можно указать, что должно быть выброшено исключение, 
                // если результат не был получен
                .autoAck()); //можно указать, что auto acknowledge
    }
}
```

Можно использовать значение свойства / переменной окружения [RABBIT_MQ_DEFAULT_QUEUE_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_QUEUE_NAME).
В этом случае в примерах выше не надо указывать имя очереди.

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableItemSupplier.rabbitArrayItem;

public class MyTest {

    @Test
    public void myTest() {
        ClassToDeserialize result = rabbitMq().read(rabbitArrayItem("Нужный элемент массива", 
                ClassToDeserialize[].class)
                //указание других опциональных параметров
                );
    }
}
```

Ниже примеры ситуаций, когда [десериализатор](./../../../core.api/doc/rus/SERIALIZATION_DESERIALIZATION.MD) нужно
передать явно.

```java
import org.my.pack;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

//Описываем сериализацию и десериализацию для текущего проекта
public class MyDataTransformer implements DataTransformer {


    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        //тут описываем механизм десериализации
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        //тут описываем механизм десериализации
    }

    @Override
    public String serialize(Object obj) {
        //тут описываем механизм сериализации
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArrayItemSupplier.rabbitArrayItem;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().read(rabbitArrayItem("Нужный элемент массива",
                "test_queue",
                ClassToDeserialize[].class, //или ссылка на тип
                /* +прочие параметры*/)
                /* +прочие параметры*/
                .withDataTransformer(new MyDataTransformer()));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Публикация сообщений

### Публикация текстового сообщения

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier.rabbitTextMessage;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().publish(rabbitTextMessage("Hello world") //публикуемый текст
                .exchange("exchange1") //можно указать exchange, можно не указывать, тогда будет 
                //использовано значение переменной окружения / свойства RABBIT_MQ_DEFAULT_EXCHANGE_NAME
                .routingKey("routing_key1") //можно указать ключ маршрутизации, можно не указывать, тогда будет 
                //использовано значение переменной окружения / свойства RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME
                .immediate() //и при необходимости 
                .mandatory() //можно указать набор дополнительных 
                .header("header1", "value1") //параметров
                .replyTo("ReplyTo")
                .deliveryMode(1)
                .priority(2)
                .contentType("plain text")
                .contentEncoding("UTF-8")
                .correlationId("!@#4")
                .expiration("1234567")
                .messageId("hjhjkhjk")
                .timestamp(timeStamp)
                .userId("UserId")
                .appId("AppId")
                .clusterId("ClasterId"));
    }
}
```

[RABBIT_MQ_DEFAULT_EXCHANGE_NAME](./SETTINGS.MD#rabbit_mq_default_exchange_name)

[RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Публикация сериализованного объекта

В примерах ниже предполагается, что настройка / свойство / переменная окружения
[RABBIT_MQ_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#RABBIT_MQ_DEFAULT_DATA_TRANSFORMER) перед началом выполнения имеет
непустое значения

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier.rabbitSerializedMessage;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().publish(rabbitSerializedMessage(
                new DraftDto().setName("test")) //объект, который надо превратить в сообщение 
                // и опубликовать
                .exchange("exchange1") //можно указать exchange, можно не указывать, тогда будет 
                //использовано значение переменной окружения / свойства RABBIT_MQ_DEFAULT_EXCHANGE_NAME
                .routingKey("routing_key1") //можно указать ключ маршрутизации, можно не указывать, тогда будет 
                //использовано значение переменной окружения / свойства RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME
                .immediate() //и при необходимости 
                .mandatory() //можно указать набор дополнительных 
                .header("header1", "value1") //параметров
                .replyTo("ReplyTo")
                .deliveryMode(1)
                .priority(2)
                .contentType("plain text")
                .contentEncoding("UTF-8")
                .correlationId("!@#4")
                .expiration("1234567")
                .messageId("hjhjkhjk")
                .timestamp(timeStamp)
                .userId("UserId")
                .appId("AppId")
                .clusterId("ClasterId"));
    }
}
```

[RABBIT_MQ_DEFAULT_EXCHANGE_NAME](./SETTINGS.MD#rabbit_mq_default_exchange_name)

[RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_ROUTING_KEY_NAME)

Ниже примеры ситуаций, когда [сериализатор](./../../../core.api/doc/rus/SERIALIZATION_DESERIALIZATION.MD) нужно передать
явно.

```java
import org.my.pack;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

//Описываем сериализацию и десериализацию для текущего проекта
public class MyDataTransformer implements DataTransformer {


    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        //тут описываем механизм десериализации
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        //тут описываем механизм десериализации
    }

    @Override
    public String serialize(Object obj) {
        //тут описываем механизм сериализации
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.RabbitMqPublishSupplier.rabbitSerializedMessage;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().publish(rabbitSerializedMessage(
                new DraftDto().setName("test"))
                .withDataTransformer(new MyDataTransformer()));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Очистка очереди

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем очищение содержимого очереди
        rabbitMq().purgeQueue("queue");
    }
}
```

Можно использовать значение свойства / переменной окружения [RABBIT_MQ_DEFAULT_QUEUE_NAME](./SETTINGS.MD#RABBIT_MQ_DEFAULT_QUEUE_NAME).
В этом случае в примерах выше не надо указывать имя очереди.

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        rabbitMq().purgeQueue();
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
