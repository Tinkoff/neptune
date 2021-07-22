# Работа с RabbitMq

В данном документе описано на примерах, как работать с брокером сообщений

## Оглавление

- [Связываение](#связывание)
    - [exchange к exchange](#exchange-к-exchange)
    - [queue к exchange](#queue-к-exchange)
- [Отвязывание](#отвязывание)
    - [exchange от exchange](#exchange-от-exchange)
    - [queue от exchange](#queue-от-exchange)
- [Объявление](#declare)
    - [exchange](#объявление-exchange)
    - [queue](#объявление-queue)
- [Удаление](#удаление)
    - [exchange](#удаление-exchange)
    - [queue](#удаление-queue)
- [Получение сообщения](#Получение-сообщения)
- [Публикация сообщения](#Публикация-сообщения)
- [Очистка очереди](#Очистка-очереди)

### Связывание

#### exchange к exchange

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем связывание exchange к exchange без дополнительных аргументов
        //destination - имя exchange, к которому сообщения передаются через привязку
        //source - имя exchange, из которого сообщения проходят через привязку
        //routingKey - ключ маршрутизации, используемый для привязки
        rabbitMq().exchangeBind("destination", "source", "routingKey");
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments.arguments;


public class MyTest {

    @Test
    public void myTest() {
        //Выполняем связывание exchange к exchange c дополнительными аргументами
        //destination - имя exchange, к которому сообщения передаются через привязку
        //source - имя exchange, из которого сообщения проходят через привязку
        //routingKey - ключ маршрутизации, используемый для привязки
        //arguments - другие свойства (параметры привязки)
        rabbitMq().exchangeBind("destination", "source", "routingKey", arguments().setArgument("key", "value"));
    }
}
```

#### queue к exchange

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем связывание queue к exchange без дополнительных аргументов
        //queue - имя очереди
        //exchange - имя exchange
        //routingKey - ключ маршрутизации, используемый для привязки
        rabbitMq().queueBind("queue", "exchange", "routingKey");
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments.arguments;


public class MyTest {

    @Test
    public void myTest() {
      //Выполняем связывание queue к exchange c дополнительными аргументами
      //queue - имя очереди
      //exchange - имя exchange
      //routingKey - ключ маршрутизации, используемый для привязки
      //arguments - другие свойства (параметры привязки)
      rabbitMq().queueBind("queue", "exchange", "routingKey", arguments().setArgument("key", "value"));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Отвязывание

#### exchange от exchange

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

  @Test
    public void myTest() {
        //Отвязывание exchange от exchange без дополнительных аргументов
        //destination - имя exchange, к которому сообщения передаются через привязку
        //source - имя exchange, из которого сообщения проходят через привязку
        //routingKey - ключ маршрутизации, используемый для привязки
        rabbitMq().exchangeUnbind("destination", "source", "routingKey");
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments.arguments;


public class MyTest {

    @Test
    public void myTest() {
        //Выполняем отвязывание exchange от exchange c  дополнительными аргументами
        //destination - имя exchange, к которому сообщения передаются через привязку
        //source - имя exchange, из которого сообщения проходят через привязку
        //routingKey - ключ маршрутизации, используемый для привязки
        //arguments - другие свойства (параметры привязки)
        rabbitMq().exchangeUnbind("destination", "source", "routingKey", arguments().setArgument("key", "value"));
    }
}
```

#### queue от exchange

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем отвязывание queue от exchange без дополнительных аргументов
        //queue - имя очереди
        //exchange - имя exchange
        //routingKey - ключ маршрутизации, используемый для привязки
        rabbitMq().queueUnbind("queue", "exchange", "routingKey");
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments.arguments;


public class MyTest {

    @Test
    public void myTest() {
      //Выполняем отвязывание queue от exchange c дополнительными аргументами
      //queue - имя очереди
      //exchange - имя exchange
      //routingKey - ключ маршрутизации, используемый для привязки
      //arguments - другие свойства (параметры привязки)
      rabbitMq().queueUnbind("queue", "exchange", "routingKey", arguments().setArgument("key", "value"));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Объявление

#### exchange

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments.arguments;


public class MyTest {

    @Test
    public void myTest() {
        //Выполняем пассивное объявление exchange
        //exchange - имя exchange
        rabbitMq().exchangeDeclare("exchange");
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments.arguments;


public class MyTest {

    @Test
    public void myTest() {
        //Выполняем объявление exchange
        //exchange - имя exchange
        //type - тип обмена
        rabbitMq().exchangeDeclare("exchange", "type");
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments.arguments;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.exchange.ParametersForDeclareExchange.exchangeParams;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем объявление exchange
        //exchange - имя exchange
        //type - тип обмена
        //exchangeParams - дополнительные параметры при объявлении
        //          durable - true, если мы объявляем долговременный exchange 
        //                      (exchange выдержит перезапуск сервера)
        //          autoDelete - true, если сервер должен удалить exchange, 
        //                      когда он больше не используется
        //          internal - true, если exchange является внутренним, 
        //                      т.е. не может быть напрямую опубликован клиентом.
        //          arguments - другие свойства (аргументы конструкции) для exchange
        rabbitMq().exchangeDeclare(
                "exchange",
                "type",
                exchangeParams()
                        .autoDelete()
                        .durable()
                        .internal()
                        .argument("name", "value"));
    }
}
```

#### queue

```java

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем объявление queue, которая будет названа сервером, exclusive, autodelete, non-durable;
        rabbitMq().queueDeclare();
    }
}
```

```java

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем пассивное обьявление очереди
        rabbitMq().queueDeclare("queue");
    }
}
```

```java

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.declare.queue.ParametersForDeclareQueue.queueParams;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем обьявление очереди
        //      queue - имя очереди   
        //      queueParams - дополнительные аргументы при объявлении   
        //              durable -  true, если мы объявляем долговременную 
        //                          очередь (очередь выдержит перезапуск сервера)
        //              exclusive - true, если мы объявляем эксклюзивную 
        //                          очередь (ограниченную этим соединением)  
        //              autoDelete - true, если мы объявляем очередь автоудаления 
        //                          (сервер удалит ее, когда она больше не используется)  
        //              arguments - другие свойства (аргументы конструкции) для очереди  
        rabbitMq().queueDeclare(
                "queue",
                queueParams()
                        .durable()
                        .exclusive()
                        .autoDelete()
                        .argument("name1", new Object())
                        .argument("name2", new Object()));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Удаление

#### exchange

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

  @Test
    public void myTest() {
        //Выполняем удаление exchange, независимо от того, используется он или нет
        rabbitMq().exchangeDelete("exchange");
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем удаление exchange
        //exchange - название обмена
        //ifUnused - true, чтобы указать, что exchange подлежит удалению только в том случае, если он не используется
        rabbitMq().exchangeDelete("exchange", true);
    }
}
```

#### queue

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем удаление очереди
        rabbitMq().deleteQueue("queue");
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
      //Выполняем удаление очереди
      //queue – имя очереди
      //ifUnused – true, если очередь должна быть удалена, только если она не используется
      //ifEmpty – true, если очередь должна быть удалена, только если она пуста
      rabbitMq().deleteQueue("queue", deleteParams().empty().unused());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение сообщения

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.valueOf;

public class MyTest {

  @Test
    public void myTest() {
        //Выполняем получение сообщения из очереди
        //В метод реад используется метод valueOf(String queue, boolean autoAck, Class<T> classT), где:
        //      queue - имя очереди
        //      autoAck - true, если сервер должен считать сообщения подтвержденными после доставки; 
        //                false, если сервер должен ожидать явных подтверждений
        //      classT - моделька для десериализации
        rabbitMq().read(valueOf("queue", true, ClassToDeserialize.class));
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.valueOf;

public class MyTest {

  @Test
  public void myTest() {
    //Выполняем получение сообщения из очереди
    //В метод read используется метод valueOf(String queue, boolean autoAck, Class<T> classT), где:
    //      queue - имя очереди
    //      autoAck - true, если сервер должен считать сообщения подтвержденными после доставки; 
    //                false, если сервер должен ожидать явных подтверждений
    //      classT - моделька для десериализации
    //В данном примере мы передаем в метод read уже определенный объект CustomMapper, который реализует RabbitMqMapper
    //и будет перекрывать дефолтный
    rabbitMq().read(valueOf("queue", true, ClassToDeserialize.class), new CustomMapper());
  }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.valueOf;

public class MyTest {

  @Test
    public void myTest() {
        //Выполняем получение сообщения из очереди, которое удовлетворяет определенному критерию
        //для этого используется метод criteria(String description, Predicate<? super T> predicate)
        //первый параметр которого это описание критерия
        //второй параметр это предикат или то, как вы проверяете объект, что он удовлетворяет вашему критерию
        rabbitMq().read(valueOf("queue", true, ClassToDeserialize.class)
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")));
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetSupplier.valueOf;

public class MyTest {

  @Test
  public void myTest() {
    //Пытаемся получить сообщение из очереди за определенное время 
    //для этого используется метод timeOut(Duration timeOut)
    //по дефолту сообщение ищется 10 секунд
    rabbitMq().read(valueOf("queue", true, ClassToDeserialize.class).timeOut(ofSeconds(30)));
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Публикация сообщений

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

  @Test
  public void myTest() {
        //Выполняем отправку сообщения
        //  Параметры:
        //      exchange - обмен для публикации сообщения
        //      routingKey - ключ маршрутизации
        //      new SomeObject() - объект, который нужно сериализовать и отправить в rabbit
        //Неявные параметры которые уходят:
        //      mandatory со значением false
        //      mmediate со значением false
        //Для сериализации используется mapper по дефолту RABBIT_MQ_DEFAULT_MAPPER
        rabbitMq().publishMessage("exchange", "routingKey", new SomeObject());
    }
}
```

Иногда необходимо использовать другой ObjectMapper, для этого можно в метод передать необходимый

```java
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем отправку сообщения
        //  Параметры:
        //      exchange - обмен для публикации сообщения
        //      routingKey - ключ маршрутизации
        //      new SomeObject() - объект, который нужно сериализовать и отправить в rabbit
        //Неявные параметры которые уходят:
        //      mandatory со значением false
      //      mmediate со значением false
      rabbitMq().publishMessage("exchange", "routingKey", new SomeObject(), new CustomMapper());
    }
}
```

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.ParametersForPublish.parameters;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем отправку сообщения
        //  Параметры:
        //      exchange - обмен для публикации сообщения
        //      routingKey - ключ маршрутизации
        //      ParametersForPublish - объект, необходимый для изменения дефолтных значений mandatory и immediate
        //                             в данном примере 'parameters().mandatory().immediate()' 
        //                             сообщение отправится с mandatory = true и immediate = true
        //      new SomeObject() - объект, который нужно сериализовать и отправить в rabbit
        rabbitMq().publishMessage("exchange", "routingKey", parameters().mandatory().immediate(), new SomeObject());
    }
}
```

```java
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.publish.ParametersForPublish.parameters;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем отправку сообщения
        //  Параметры:
        //      exchange - обмен для публикации сообщения
      //      routingKey - ключ маршрутизации
      //      ParametersForPublish - объект, необходимый для изменения дефолтных значений mandatory и immediate
      //                             в данном примере 'parameters().mandatory().immediate()' 
      //                             сообщение отправится с mandatory = true и immediate = true
      //      new SomeObject() - объект, который нужно сериализовать и отправить в rabbit
      //      new XmlMapper() - новый ObjectMapper
      rabbitMq().publishMessage("exchange", "routingKey", parameters().mandatory().immediate(), new SomeObject(), new CustomMapper());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Очистка очереди

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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
