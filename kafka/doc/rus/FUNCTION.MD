# Работа с Kafka

В данном документе описано на примерах, как работать с брокером сообщений

## Оглавление

- [Получение сообщения](#Получение-сообщения)
  - [Получение consumerRecords](#Получение-consumerRecords)
  - [Получение десериализованных сообщений как List](#Получение-десериализованных-сообщений-как-List)
  - [Получение десериализованных сообщений в виде массива](#Получение-десериализованных-сообщений-в-виде-массива)
  - [Получение десериализованного сообщения как Java-объект из Iterable](#Получение-десериализованного-сообщения-как-Java-объект-из-Iterable)
- [Публикация сообщений](#Публикация-сообщений)

## Получение сообщения

О принципах работы шагов, которые возвращают результат, можно прочитать
[здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение consumerRecords

В примерах ниже предполагается, что настройки / свойства / переменные окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER),
[DEFAULT_TOPICS_FOR_POLL](./SETTINGS.MD#DEFAULT_TOPICS_FOR_POLL)
перед началом выполнения имеют непустые значения

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

  @Test
  public void myTest() {
    //Возвращает коллекцию ConsumerRecord из указанных топиков
    List<ConsumerRecord> results = kafka().poll(consumerRecords("Описание", 
            "test_topic1", "test_topic2") //топики для получения сообщений
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .criteria("description", c->c.value().contains("29"))//можно указать критерий, 
            // которому должен соответствовать каждый элемент результирующей коллекции
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если результат не был получен
  }
}
```

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

  @Test
  public void myTest() {
    List<Object> results = kafka().poll(consumerRecords("Описание", 
            "test_topic1", "test_topic2") 
            //Вернет лист объектов, полученных вызовом метода value у каждой ConsumerRecord
            .thenGetList("value from ConsumerRecords", ConsumerRecord::value)); 
  }
}
```

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

  @Test
  public void myTest() {
    List<DraftDto> results = kafka().poll(consumerRecords("Описание", 
            "test_topic1", "test_topic2") 
            //Вернет лист десериализованных объектов
            .thenGetList("Описание", DraftDto.class)); 
  }
}
```

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

  @Test
  public void myTest() {
    List<String> results = kafka().poll(consumerRecords("Описание", 
            "test_topic1", "test_topic2") 
            //Сначала десериализует каждый consumerRecord в объект DraftDto и выполнит функцию "DraftDto::getName"
            .thenGetList("DraftDto-> name", DraftDto.class, DraftDto::getName)); 
            //В результате вернется коллекция строк
  }
}
```

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

  @Test
  public void myTest() {
    List<DraftDto> results = kafka().poll(consumerRecords("Описание", 
            "test_topic1", "test_topic2") 
            .thenGetList("Описание", new TypeReference<DraftDto>() {})); 
  }
}
```

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

  @Test
  public void myTest() {
    List<String> results = kafka().poll(consumerRecords("Описание", 
            "test_topic1", "test_topic2") 
            .thenGetList("DraftDto-> name", new TypeReference<>() {}, DraftDto::getName)); 
  }
}
```

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

  @Test
  public void myTest() {
    //Вернет значение value для найденной consumerRecord
    var results = kafka().poll(consumerRecords("Описание", 
            "test_topic1", "test_topic2")
            .thenGetItem("value",ConsumerRecord::value));
  }
}
```

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

  @Test
  public void myTest() {
    //Вернет десериализованный объект DraftDto
    DraftDto results = kafka().poll(consumerRecords("Описание", 
            "test_topic1", "test_topic2")
            .thenGetItem("description",DraftDto.class));
  }
}
```

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

  @Test
  public void myTest() {
    //Вернет десериализованный объект DraftDto
    DraftDto results = kafka().poll(consumerRecords("Описание", 
            "test_topic1", "test_topic2")
            .thenGetItem("description",new TypeReference<DraftDto>() {}));
  }
}
```

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

  @Test
  public void myTest() {
    String results = kafka().poll(consumerRecords("Описание", 
            "test_topic1", "test_topic2")
            .thenGetItem("description",DraftDto.class, DraftDto::getName));
  }
}
```

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

  @Test
  public void myTest() {
    String results = kafka().poll(consumerRecords("Описание", 
            "test_topic1", "test_topic2")
            .thenGetItem("description",new TypeReference<DraftDto>() {}, DraftDto::getName));
  }
}
```

### Получение десериализованных сообщений как List

В примерах ниже предполагается, что настройки / свойства / переменные окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER),
[DEFAULT_TOPICS_FOR_POLL](./SETTINGS.MD#DEFAULT_TOPICS_FOR_POLL)
перед началом выполнения имеют непустые значения

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaIterable;

public class MyTest {

  @Test
  public void myTest() {
    // Возвращает сообщение из очереди, десериализованное в коллекцию/Iterable
    // Из общего сообщения (Iterable/коллекции) вернется под-множество(немутабельный лист) подходящих элементов.
    List<ClassToDeserialize> results = kafka().poll(kafkaIterable("Результирующий лист", //описываем то, 
            // что нужно получить
            ClassToDeserialize.class, //класс элемента результирующего списка
            "test_topic1", "test_topic2") //топики для получения сообщений
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать каждый элемент результирующей коллекции
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, если результат не был получен
  }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaIterable;

public class MyTest {

  @Test
  public void myTest() {
    // Возвращает сообщение из очереди, десериализованное в коллекцию/Iterable
    // Из общего сообщения (Iterable/коллекции) вернется под-множество(немутабельный лист) подходящих элементов.
    List<ClassToDeserialize<SomeType>> result = kafka().poll(kafkaIterable("Результирующий лист", //описываем то, 
            // что нужно получить
            new TypeReference<ClassToDeserialize<SomeType>>() {
            }, //тип элемента результирующего списка
            "test_topic1", "test_topic2") //топики для получения сообщений
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать каждый элемент результирующей коллекции
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если результат не был получен
  }
}
```

```java
import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaIterable;

public class MyTest {

  @Test
  public void myTest() {
    // Возвращается немутабельный лист. Сообщение необязательно является коллекцией 
    // или объектом Iterable. Промежуточная коллекция или Iterable получаются в ходе дополнительных 
    // вычислений. Из коллекции вернется под-множество подходящих элементов.
    List<Object> result = kafka().poll(kafkaIterable("Результирующий лист", //описываем 
            // то, что нужно получить
            ClassToDeserialize.class, //класс модели для десериализации
            o -> {
              return /*описываем то, как получаем из ClassToDeserialize Object */;
            },
            "test_topic1", "test_topic2") //топики для получения сообщений
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать каждый элемент результирующей коллекции
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если результат не был получен
  }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaIterable;

public class MyTest {

  @Test
  public void myTest() {
    // Возвращается немутабельный лист. Сообщение необязательно является коллекцией 
    // или объектом Iterable. Промежуточная коллекция или Iterable получаются в ходе дополнительных 
    // вычислений. Из коллекции вернется под-множество подходящих элементов.
    List<Object> result = kafka().poll(kafkaIterable("Результирующий лист", //описываем 
            // то, что нужно получить
            new TypeReference<ClassToDeserialize<SomeType>>() {
            }, //тип объекта-модели для десериализации
            o -> {
              return /*описываем то, как получаем из ClassToDeserialize Object */;
            },
            "test_topic1", "test_topic2") //топики для получения сообщений
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать каждый элемент результирующей коллекции
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если результат не был получен
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaIterable;

public class MyTest {

  @Test
  public void myTest() {
    //Если не указывать топики, то значение топиков возьмется из DEFAULT_TOPICS_FOR_POLL
    List<ClassToDeserialize> results = kafka().poll(kafkaIterable("Результирующий лист", ClassToDeserialize.class));
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaRawMessages;

public class MyTest {

  @Test
  public void myTest() {
    //Если необходимо получить 'сырые' строки, которые возвращаются из kafka
    List<String> results = kafka().poll(kafkaRawMessages("testTopic"));
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaRawMessages;

public class MyTest {

  @Test
  public void myTest() {
    //Если необходимо получить 'сырые' строки, которые возвращаются из kafka
    //Если не указывать топики, то значение топиков возьмется из DEFAULT_TOPICS_FOR_POLL
    List<String> results = kafka().poll(kafkaRawMessages());
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
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaIterable;

public class MyTest {

  @Test
  public void myTest() {
    kafka().poll(kafkaIterable("Результирующий лист",
            ClassToDeserialize.class,
            "test_topic1", "test_topic2")
            .withDataTransformer(new MyDataTransformer()));
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение десериализованных сообщений в виде массива

В примерах ниже предполагается, что настройки / свойства / переменные окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER),
[DEFAULT_TOPICS_FOR_POLL](./SETTINGS.MD#DEFAULT_TOPICS_FOR_POLL)
перед началом выполнения имеют непустые значения

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollArraySupplier.kafkaArray;

public class MyTest {

  @Test
  public void myTest() {
    // Возвращает сообщение из очереди, десериализованное в массив
    // Из общего сообщения-массива вернется под-массив подходящих элементов.
    ClassToDeserialize[] result = kafka().poll(kafkaArray("Результирующий массив", //описываем то, 
            // что нужно получить
            ClassToDeserialize.class, //класс массива
            "test_topic1", "test_topic2") //топики для получения сообщений
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать каждый элемент результирующего массива
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если результат не был получен
  }
}
```

```java

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollArraySupplier.kafkaArray;

public class MyTest {

    @Test
    public void myTest() {
      // Возвращается массив. Сообщение необязательно является массивом. 
      // Промежуточный массив получаются в ходе дополнительных 
      // вычислений. Из массива вернется под-массив подходящих элементов.
      Object[] result = kafka().poll(kafkaArray("Результирующий массив", //описываем 
              // то, что нужно получить
              ClassToDeserialize.class, //класс модели для десериализации
              Object.class, //необходимо явно указать результирующий класс функции
              o -> {
                return /*описываем то, как получаем из ClassToDeserialize в Object */;
              },
              "test_topic1", "test_topic2") //топики для получения сообщений
              .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
              // которому должен соответствовать каждый элемент результирующего массива
              .timeOut(ofSeconds(5)) //можно указать время на получение 
              // ожидаемого результата = 5 секунд
              .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
      // если результат не был получен
    }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollArraySupplier.kafkaArray;

public class MyTest {

  @Test
  public void myTest() {
    // Возвращается массив. Сообщение необязательно является массивом. 
    // Промежуточный массив получаются в ходе дополнительных 
    // вычислений. Из массива вернется под-массив подходящих элементов.
    ClassToDeserialize[] result = kafka().poll(kafkaArray("Результирующий массив", //описываем то, 
            // что нужно получить
            new TypeReference<ClassToDeserialize>() {
            }, //тип объекта-модели для десериализации
            "test_topic1", "test_topic2") //топики для получения сообщений
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать каждый элемент результирующего массива
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если результат не был получен
  }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollArraySupplier.kafkaArray;

public class MyTest {

    @Test
    public void myTest() {
      // Возвращается массив. Сообщение необязательно является массивом. 
      // Промежуточный массив получаются в ходе дополнительных 
      // вычислений. Из массива вернется под-массив подходящих элементов.
      Object[] result = kafka().poll(kafkaArray("Результирующий массив", //описываем то, 
              // что нужно получить
              new TypeReference<ClassToDeserialize>() {
              }, //тип объекта-модели для десериализации
              Object.class, //необходимо явно указать результирующий класс функции
              o -> {
                return /*описываем то, как получаем из ClassToDeserialize в Object */;
              },
              "test_topic1", "test_topic2") //топики для получения сообщений
              .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
              // которому должен соответствовать каждый элемент результирующего массива
              .timeOut(ofSeconds(5)) //можно указать время на получение 
              // ожидаемого результата = 5 секунд
              .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
      // если результат не был получен
    }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollArraySupplier.kafkaArray;

public class MyTest {

  @Test
  public void myTest() {
    //Если не указывать топики, то значение топиков возьмется из DEFAULT_TOPICS_FOR_POLL
    ClassToDeserialize[] result = kafka().poll(kafkaArray("Результирующий массив", ClassToDeserialize.class));
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaArrayOfRawMessages;

public class MyTest {

  @Test
  public void myTest() {
    //Если необходимо получить 'сырые' строки, которые возвращаются из kafka
    String[] results = kafka().poll(kafkaArrayOfRawMessages("testTopic"));
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaArrayOfRawMessages;

public class MyTest {

  @Test
  public void myTest() {
    //Если необходимо получить 'сырые' строки, которые возвращаются из kafka
    //Если не указывать топики, то значение топиков возьмется из DEFAULT_TOPICS_FOR_POLL
    String[] results = kafka().poll(kafkaArrayOfRawMessages());
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
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollArraySupplier.kafkaArray;

public class MyTest {

  @Test
  public void myTest() {
    kafka().poll(kafkaArray("Результирующий массив", ClassToDeserialize.class)
            .withDataTransformer(new MyDataTransformer()));
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение десериализованного сообщения как Java-объект из Iterable

В примерах ниже предполагается, что настройки / свойства / переменные окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER),
[DEFAULT_TOPICS_FOR_POLL](./SETTINGS.MD#DEFAULT_TOPICS_FOR_POLL)
перед началом выполнения имеют непустые значения

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaIterableItem;

public class MyTest {

  @Test
  public void myTest() {
    //Возвращается элемент коллекции / Iterable. Сообщение из очереди десериализуется в коллекцию/Iterable
    //Из общего сообщения (Iterable/коллекции) вернется первый подходящий элемент.
    ClassToDeserialize result = kafka().poll(kafkaIterableItem("Нужный элемент листа", //описываем то, 
            // что нужно получить
            ClassToDeserialize.class,
            "test_topic1", "test_topic2") //топики для получения сообщений
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать результирующий элемент из коллекции
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если результат не был получен
  }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaIterableItem;

public class MyTest {

  @Test
  public void myTest() {
    //Возвращается элемент коллекции / Iterable. Сообщение из очереди десериализуется в коллекцию/Iterable
    //Из общего сообщения (Iterable/коллекции) вернется первый подходящий элемент.
    ClassToDeserialize result = kafka().poll(kafkaIterableItem("Нужный элемент листа",
            new TypeReference<ClassToDeserialize>() {
            }, //тип объекта-модели для десериализации
            "test_topic1", "test_topic2") //топики для получения сообщений
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать результирующий элемент из коллекции
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если результат не был получен
  }
}
```

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaIterableItem;

public class MyTest {

  @Test
  public void myTest() {
    //Возвращается элемент коллекции / Iterable.  Сообщение необязательно является коллекцией 
    // или объектом Iterable. Промежуточная коллекция или Iterable получаются в ходе дополнительных 
    // вычислений. Из коллекции вернется первый подходящий элемент.
    Object result = kafka().poll(kafkaIterableItem("Нужный элемент листа", //описываем 
            // то, что нужно получить
            ClassToDeserialize.class, //класс модели для десериализации
            o -> {
              return /*описываем то, как получаем из объекта ClassToDeserialize объект  Object*/;
            },
            "test_topic1", "test_topic2") //топики для получения сообщений
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать результирующий элемент из коллекции
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если результат не был получен
  }
}
```

```java
import com.fasterxml.jackson.core.type.TypeReference;

import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaIterableItem;

public class MyTest {

  @Test
  public void myTest() {
    //Возвращается элемент коллекции / Iterable.  Сообщение необязательно является коллекцией 
    // или объектом Iterable. Промежуточная коллекция или Iterable получаются в ходе дополнительных 
    // вычислений. Из коллекции вернется первый подходящий элемент.
    Object result = kafka().poll(kafkaIterableItem("Нужный элемент листа",
            new TypeReference<ClassToDeserialize>() {
            }, //тип объекта-модели для десериализации
            o -> {
              return /*описываем то, как получаем из объекта ClassToDeserialize объект  Object*/;
            },
            "test_topic1", "test_topic2") //топики для получения сообщений
            .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
            // которому должен соответствовать результирующий элемент из коллекции
            .timeOut(ofSeconds(5)) //можно указать время на получение 
            // ожидаемого результата = 5 секунд
            .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
    // если результат не был получен
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaIterableItem;

public class MyTest {

  @Test
  public void myTest() {
    //Если не указывать топики, то значение топиков возьмется из DEFAULT_TOPICS_FOR_POLL
    ClassToDeserialize result = kafka().poll(kafkaIterableItem("Нужный элемент листа", ClassToDeserialize.class));
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaRawMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Если необходимо получить 'сырую' строку, которая возвращается из kafka
    String results = kafka().poll(kafkaRawMessage("testTopic"));
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaRawMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Если необходимо получить 'сырую' строку, которая возвращается из kafka
    //Если не указывать топики, то значение топиков возьмется из DEFAULT_TOPICS_FOR_POLL
    String results = kafka().poll(kafkaRawMessage());
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
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.kafkaIterableItem;

public class MyTest {

  @Test
  public void myTest() {
    kafka().poll(kafkaIterableItem("Нужный элемент листа", ClassToDeserialize.class)
            .withDataTransformer(new MyDataTransformer()));
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Публикация сообщений

В примерах ниже предполагается, что настройки / свойства / переменные окружения
[KAFKA_KEY_TRANSFORMER](./SETTINGS.MD#KAFKA_KEY_TRANSFORMER),
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER),
[DEFAULT_TOPIC_FOR_SEND](./SETTINGS.MD#DEFAULT_TOPIC_FOR_SEND)
[KAFKA_CALL_BACK](./SETTINGS.MD#kafka_call_back), перед началом выполнения имеют непустые значения

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.kafkaSerializedMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Выполняем отправку сообщения
    //сериализатор берется дефолтный из KAFKA_DEFAULT_DATA_TRANSFORMER
    kafka.send(kafkaSerializedMessage(new SomeObject()) //new SomeObject() - объект, который нужно сериализовать и отправить в kafka
            .topic("testTopic") //топик для публикации сообщения
            .partition(1) // значение partition
            .timestamp(10L) // значение timestamp
            // значение callback
            .callback(new Callback() {
              @Override
              public void onCompletion(RecordMetadata metadata, Exception exception) {

              }
            })
            //header можно добавить как объект RecordHeader
            .header(new RecordHeader("Header key1", "Value1".getBytes()))
            //header можно добавить передав строковое значение key и строковое значение value
            .header("Header key2", "Value2")
            //header можно добавить передав строковое значение key и byte[] для value
            .header("Header key3", "Value3".getBytes())
    );
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.kafkaSerializedMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Если не указывать топик для отправки сообщения, то он возьмет дефолтное значение из DEFAULT_TOPIC_FOR_SEND
    kafka.send(kafkaSerializedMessage(new SomeObject()));
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.kafkaSerializedMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Если настройка KAFKA_CALLBACK имеет непустое значение, то сообщение будет автоматом отправлено с данным значением
    kafka.send(kafkaSerializedMessage(new SomeObject()));
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.kafkaSerializedMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Ключ можно отправлять 'сырой' строкой
    kafka.send(kafkaSerializedMessage(new SomeObject())
            .key("SomeString"));
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.kafkaSerializedMessage;

public class MyTest {

  @Test
  public void myTest() {
    //В качестве ключа можно отправлять объект, который будет сериализован с помощью DataTransformer из KAFKA_KEY_TRANSFORMER.get()
    kafka.send(kafkaSerializedMessage(new SomeObject())
            .key(draftDto));
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.kafkaSerializedMessage;

public class MyTest {

  @Test
  public void myTest() {
    //В качестве ключа можно отправлять объект и объект DataTransformer, с помощью которого объект будет сериализован
    kafka.send(kafkaSerializedMessage(new SomeObject())
            .key(draftDto, new MyDataTransformer()));
  }
}
```

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
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.kafkaSerializedMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Выполняем отправку сообщения
    //сериализатор берется дефолтный из KAFKA_DEFAULT_DATA_TRANSFORMER
    kafka.send(kafkaSerializedMessage(new SomeObject()) //new SomeObject() - объект, который нужно сериализовать и отправить в kafka
            .dataTransformer(new MyDataTransformer()) // устанавливаем кастомный DataTransformer
    );
  }
}
```

Так же Neptune поддерживает отправку обычных строк, которые не нужно сериализовать§

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.kafkaTextMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Выполняем отправку сообщения
    kafka.send(kafkaTextMessage("I'm a String!") //строка, которую нужно отправить в kafka
            .topic("testTopic") //топик для публикации сообщения
            .partition(1) // значение partition
            .timestamp(10L) // значение timestamp
            // значение callback
            .callback(new Callback() {
              @Override
              public void onCompletion(RecordMetadata metadata, Exception exception) {

              }
            })
            .headers(headers)// значение headers
    );
  }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.kafkaTextMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Если не указывать топик для отправки сообщения, то он возьмет дефолтное значение из DEFAULT_TOPIC_FOR_SEND
    kafka.send(kafkaTextMessage("I'm a String!"));
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)