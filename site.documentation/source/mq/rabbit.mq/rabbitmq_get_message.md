# RabbitMQ. Получение сообщений и их данных

Полезные ссылки:
- [Свойство RABBIT_MQ_DEFAULT_QUEUE_NAME](settings/RABBIT_MQ_DEFAULT_QUEUE_NAME.md)
- [Шаги, возвращающие объекты](../../quick_start/steps/pattern_steps/get_step/index.md)
- [Свойство RABBIT_MQ_DEFAULT_DATA_TRANSFORMER](settings/RABBIT_MQ_DEFAULT_DATA_TRANSFORMER.md)
- [Сериализация и десериализация](./../../core/serialize_deserialize.rst)

## Получение сообщений

```java
import java.util.List;

import com.rabbitmq.client.GetResponse;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.GetResponseSupplier.responses;

public class MyTest {

    @Test
    public void myTest() {
        //получение сообщений целиком  
        List<GetResponse> result = rabbitMq().read(response("test_queue") //название очереди
                //Параметр необязательный. Если его не указывать, будет использовано значение
                //свойства RABBIT_MQ_DEFAULT_QUEUE_NAME
                .autoAck() //можно указать, что auto acknowledge
                //опции, уточняющие результат
                //
        );
    }
}
```

```{eval-rst}
.. include:: ../../shared_docs/steps_return_list_optiotal_parameters_async.rst
```

### Данные сообщений RabbitMQ, собранные в лист

```java
import java.util.List;

import com.rabbitmq.client.GetResponse;

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.GetResponseSupplier.responses;

public class MyTest {

    @Test
    public void myTest() {
        List<SomeData> someDataList = rabbitMq().read(response(/*параметры*/)
                //опции, уточняющие результат
                //
                .thenGetList(
                    "Описание / название результирующего листа", //описание того ЧТО следует получить,
                    //в свободной форме или бизнес
                    //терминологии
                    getResponse -> {
                        //Алгоритм функции, которая получает данные
                        //из каждого сообщения
                        return someDataObject; //эти объекты
                        //будут собраны в результирующий лист
                    }
                )
                //опции, уточняющие результат
                //
        );
    }
}
```

```{eval-rst}
.. include:: ../../shared_docs/steps_return_list_optiotal_parameters_sync.rst
```

```java
import java.util.List;

import com.rabbitmq.client.GetResponse;

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.GetResponseSupplier.responses;

public class MyTest {

    @Test
    public void myTest() {
        List<SomeData> someDataList = rabbitMq().read(response(/*параметры*/)
                //опции, уточняющие результат
                //
                .thenGetList(
                    "Описание / название результирующего листа", //описание того ЧТО следует получить,
                    //в свободной форме или бизнес
                    //терминологии
                    SomeBodyClass.class, //Класс, в объект которого 
                    // десериализуется тело каждого сообщения.
                    // Так же можно использовать объект 
                    // com.fasterxml.jackson.core.type.TypeReference
                    //
                    // Функция, с помощью которой извлекаются данные
                    // из десериализованного тела сообщения.
                    // Она не обязательная. Если ее не указывать,
                    // то вернется объект List<SomeBodyClass>.
                    someBodyObject -> {
                        //Алгоритм функции, которая получает данные
                        //из каждого десериализованного тела сообщения
                        return someDataObject; //эти объекты
                        //будут собраны в результирующий лист
                    }
                )
                // Указывается объект класса 
                // ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer,
                // c помощью которого десериализуется тело каждого сообщения.
                // Вызов метода необязателен. По умолчанию используется
                // значение свойства RABBIT_MQ_DEFAULT_DATA_TRANSFORMER
                .withDataTransformer(new CustomDataTransformer())
                //опции, уточняющие результат
                //
        );
    }
}
```

### Объект данных из сообщений RabbitMQ

```java
import java.util.List;

import com.rabbitmq.client.GetResponse;

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.GetResponseSupplier.responses;

public class MyTest {

    @Test
    public void myTest() {
        SomeData someData = rabbitMq().read(response(/*параметры*/)
                //опции, уточняющие результат
                //
                .thenGetItem(
                    "Описание / название результирующего объекта", //описание того 
                    // ЧТО следует получить,
                    //в свободной форме или бизнес
                    //терминологии
                    getResponse -> {
                        //Алгоритм функции, которая получает данные
                        //из каждого сообщения
                        return someDataObject; //один из этих 
                        //объектов возвращается как результат,
                        //первый попавшийся или с учетом уточнений
                })
                //опции, уточняющие результат
                //
        );
    }
}
```

```{eval-rst}
.. include:: ../../shared_docs/steps_return_iterable_item_optiotal_parameters_sync.rst
```

```java
import java.util.List;

import com.rabbitmq.client.GetResponse;

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.GetResponseSupplier.responses;

public class MyTest {

    @Test
    public void myTest() {
        SomeData someData = rabbitMq().read(response(/*параметры*/)
                //опции, уточняющие результат
                //
                .thenGetItem(
                    "Описание / название результирующего объекта", //описание того 
                    // ЧТО следует получить,
                    //в свободной форме или бизнес
                    //терминологии
                    SomeBodyClass.class, //Класс, в объект которого 
                    // десериализуется тело каждого сообщения.
                    // Так же можно использовать объект 
                    // com.fasterxml.jackson.core.type.TypeReference
                    //
                    // Функция, с помощью которой извлекаются данные
                    // из десериализованного тела сообщения.
                    // Она не обязательная. Если ее не указывать,
                    // то вернется объект SomeBodyClass.
                    someBodyObject -> {
                        //Алгоритм функции, которая получает данные
                        //из каждого десериализованного тела сообщения
                        return someDataObject; //один из этих 
                        //объектов возвращается как результат,
                        //первый попавшийся или с учетом уточнений
                    }
                )
                // Указывается объект класса 
                // ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer,
                // c помощью которого десериализуется тело каждого сообщения.
                // Вызов метода необязателен. По умолчанию используется
                // значение свойства RABBIT_MQ_DEFAULT_DATA_TRANSFORMER
                .withDataTransformer(new CustomDataTransformer())
                //опции, уточняющие результат
                //
        );
    }
}
```

## Получение данных сообщений

Отличие от примеров выше в том, что здесь нет промежуточного действия в виде получения сообщений, 
из которых извлекаются данные. Данные получаются как бы напрямую.

### Получение данных сообщений RabbitMQ как Java-объект

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableItemSupplier.*;

public class MyTest {

    @Test
    public void myTest() {
        SomeData result = rabbitMq().read(rabbitIterableItem(
                "Описание / название результирующего объекта", //описание того 
                // ЧТО следует получить,
                //в свободной форме или бизнес
                //терминологии
                "test_queue",  //Название очереди
                //Параметр необязательный. Если его не указывать, будет использовано значение
                //свойства RABBIT_MQ_DEFAULT_QUEUE_NAME
                SomeBodyClass.class, //Класс, в объект которого 
                // десериализуется тело каждого сообщения.
                // Так же можно использовать объект 
                // com.fasterxml.jackson.core.type.TypeReference
                //
                // Функция, с помощью которой извлекаются данные
                // из десериализованного тела сообщения.
                // Она не обязательная. Если ее не указывать,
                // то вернется объект SomeBodyClass.
                someBodyObject -> {
                    //Алгоритм функции, которая получает данные
                    //из каждого десериализованного тела сообщения
                    return someDataObject; //один из этих 
                    //объектов возвращается как результат,
                    //первый попавшийся или с учетом уточнений
                })
                // Указывается объект класса 
                // ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer,
                // c помощью которого десериализуется тело каждого сообщения.
                // Вызов метода необязателен. По умолчанию используется
                // значение свойства RABBIT_MQ_DEFAULT_DATA_TRANSFORMER
                .withDataTransformer(new CustomDataTransformer())
                //можно указать, что auto acknowledge
                .autoAck()
                //опции, уточняющие результат
                //
        );
    }
}
```

```{eval-rst}
.. include:: ../../shared_docs/steps_return_iterable_item_optiotal_parameters_async.rst
```

Ниже пример, как можно получить текст одного из прочитанных сообщений

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableItemSupplier.*;

public class MyTest {

    @Test
    public void myTest() {
        // Возвращает сообщение из очереди.
        String result = rabbitMq().read(rabbitRawMessage(
                "queue", //Название очереди
                //Параметр необязательный. Если его не указывать, будет использовано значение
                //свойства RABBIT_MQ_DEFAULT_QUEUE_NAME
                UTF_8) //Кодировка, параметр необязательный
                //можно указать, что auto acknowledge
                .autoAck()
                //опции, уточняющие результат,
                //
        );
    }
}
```

### Получение данных сообщений RabbitMQ, собранных в лист

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableSupplier.*;

public class MyTest {

  @Test
  public void myTest() {
    List<SomeData> result = rabbitMq().read(rabbitIterable(
            "Описание / название результирующего листа", //описание того ЧТО следует получить,
            //в свободной форме или бизнес
            //терминологии
            "test_queue", //Название очереди
            //Параметр необязательный. Если его не указывать, будет использовано значение
            //свойства RABBIT_MQ_DEFAULT_QUEUE_NAME
            SomeBodyClass.class, //Класс, в объект которого 
            // десериализуется тело каждого сообщения.
            // Так же можно использовать объект 
            // com.fasterxml.jackson.core.type.TypeReference
            //
            // Функция, с помощью которой извлекаются данные
            // из десериализованного тела сообщения.
            // Она не обязательная. Если ее не указывать,
            // то вернется объект List<SomeBodyClass>.
            someBodyObject -> {
                //Алгоритм функции, которая получает данные
                //из каждого десериализованного тела сообщения
                return someDataObject; //эти объекты
                //будут собраны в результирующий лист
            })
            // Указывается объект класса 
            // ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer,
            // c помощью которого десериализуется тело каждого сообщения.
            // Вызов метода необязателен. По умолчанию используется
            // значение свойства RABBIT_MQ_DEFAULT_DATA_TRANSFORMER
            .withDataTransformer(new CustomDataTransformer())
            //можно указать, что auto acknowledge
            .autoAck()
            //опции, уточняющие результат
            //
        );
    }
}
```

```{eval-rst}
.. include:: ../../shared_docs/steps_return_list_optiotal_parameters_async.rst
```

Ниже пример, как можно получить тексты прочитанных сообщений

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;
import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetIterableSupplier.*;

public class MyTest {

  @Test
  public void myTest() {
      // Возвращает сообщения из очереди.
      List<String> result = rabbitMq().read(rabbitIterableOfRawMessages(
              "queue", //Название очереди
              //Параметр необязательный. Если его не указывать, будет использовано значение
              //свойства RABBIT_MQ_DEFAULT_QUEUE_NAME
              UTF_8) //Кодировка, параметр необязательный
              //можно указать, что auto acknowledge
              .autoAck()
          //опции, уточняющие результат,
          //
      );
  }
}
```

### Получение данных сообщений RabbitMQ, собранных в массив

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArraySupplier.*;

public class MyTest {

  @Test
  public void myTest() {
      ArrayItemClass[] result = rabbitMq().read(rabbitArray(
            "Описание / название результирующего массива", //описание того ЧТО следует получить,
            //в свободной форме или бизнес
            //терминологии
            "test_queue", //Название очереди
            //Параметр необязательный. Если его не указывать, будет использовано значение
            //свойства RABBIT_MQ_DEFAULT_QUEUE_NAME
            SomeBodyClass.class, //Класс, в объект которого 
            // десериализуется тело каждого сообщения.
            // Так же можно использовать объект 
            // com.fasterxml.jackson.core.type.TypeReference
            //
            ArrayItemClass.class, //Класс каждого элемента получаемого массива
            // Параметр обязателен, если из каждого десериализованного
            // тела сообщения нужно получить какие-то данные,
            // из которых будет собран результирующий массив.
            // Получение этих данных описывается функцией ниже.
            //
            // Функция, с помощью которой извлекаются данные
            // из десериализованного тела сообщения.
            // Она не обязательная. Если ее не указывать,
            // то вернется объект SomeBodyClass[]
            someBodyObject -> {
                //Алгоритм функции, которая получает данные
                //из каждого десериализованного тела сообщения
                return someArrayItemObject; //эти объекты
                //будут собраны в результирующий массив
            })
            // Указывается объект класса 
            // ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer,
            // c помощью которого десериализуется тело каждого сообщения.
            // Вызов метода необязателен. По умолчанию используется
            // значение свойства RABBIT_MQ_DEFAULT_DATA_TRANSFORMER
            .withDataTransformer(new CustomDataTransformer())
            //можно указать, что auto acknowledge
            .autoAck()
            //опции, уточняющие результат,
            //
      );
  }
}
```

```{eval-rst}
.. include:: ../../shared_docs/steps_return_array_optiotal_parameters_async.rst
```

Ниже пример, как можно получить тексты прочитанных сообщений в виде массива

```java
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArraySupplier.*;

public class MyTest {

  @Test
  public void myTest() {
      // Возвращает сообщения из очереди.
      String[] result = rabbitMq().read(rabbitArrayOfRawMessages(
              "queue", //Название очереди
              //Параметр необязательный. Если его не указывать, будет использовано значение
              //свойства RABBIT_MQ_DEFAULT_QUEUE_NAME
              UTF_8) //Кодировка, параметр необязательный
              //можно указать, что auto acknowledge
              .autoAck()
          //опции, уточняющие результат,
          //
      );
  }
}
```

### Получение данных сообщений RabbitMQ как элемент массива

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

import static ru.tinkoff.qa.neptune.rabbit.mq.function.get.RabbitMqBasicGetArrayItemSupplier.*;

public class MyTest {

  @Test
  public void myTest() {
      ArrayItemClass result = rabbitMq().read(rabbitArrayItem(
            "Описание / название результирующего элемента массива", //описание того ЧТО следует получить,
            //в свободной форме или бизнес
            //терминологии
            "test_queue", //Название очереди
            //Параметр необязательный. Если его не указывать, будет использовано значение
            //свойства RABBIT_MQ_DEFAULT_QUEUE_NAME
            SomeBodyClass.class, //Класс, в объект которого 
            // десериализуется тело каждого сообщения.
            // Так же можно использовать объект 
            // com.fasterxml.jackson.core.type.TypeReference
            //
            ArrayItemClass.class, //Класс каждого элемента массива, из 
            // которого будет выбран результирующий объект.
            // Параметр обязателен, если из каждого десериализованного
            // тела сообщения нужно получить какие-то данные,
            // из которых будет выбран результат.
            //
            // Функция, с помощью которой извлекаются данные
            // из десериализованного тела сообщения.
            // Она не обязательная. Если ее не указывать,
            // то вернется объект SomeBodyClass
            someBodyObject -> {
                //Алгоритм функции, которая получает данные
                //из каждого десериализованного тела сообщения
                return someArrayItemObject; //один из этих 
                //объектов возвращается как результат,
                //первый попавшийся или с учетом уточнений
            })
            // Указывается объект класса 
            // ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer,
            // c помощью которого десериализуется тело каждого сообщения.
            // Вызов метода необязателен. По умолчанию используется
            // значение свойства RABBIT_MQ_DEFAULT_DATA_TRANSFORMER
            .withDataTransformer(new CustomDataTransformer())
            //можно указать, что auto acknowledge
            .autoAck()
            //опции, уточняющие результат,
            //
      );
  }
}
```

```{eval-rst}
.. include:: ../../shared_docs/steps_return_array_item_optiotal_parameters_async.rst
```

