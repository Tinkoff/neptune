# Работа с Kafka

В данном документе описано на примерах, как работать с брокером сообщений

## Оглавление

- [Получение сообщения](#Получение-сообщения)
    - [Получение данных сообщения как Iterable](#Получение-данных-сообщения-как-Iterable)
    - [Получение данных сообщения в виде массива](#Получение-данных-сообщения-в-виде-массива)
    - [Получение данных сообщения как Java-объект из Iterable](#Получение-данных-сообщения-как-Java-объект-из-Iterable)
- [Публикация сообщений](#Публикация-сообщений)

## Получение сообщения

О принципах работы шагов, которые возвращают результат, можно прочитать
[здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных сообщения как Iterable

В примерах ниже предполагается, что настройки / свойства / переменные окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER),
[DEFAULT_TOPICS_FOR_POLL](./SETTINGS.MD#DEFAULT_TOPICS_FOR_POLL)
перед началом выполнения имеют непустое значения

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaIterable;

public class MyTest {

    @Test
    public void myTest() {
        // Возвращает сообщение из очереди, десериализованное в коллекцию/Iterable
        //Из общего сообщения (Iterable/коллекции) вернется под-множество подходящих элементов.
        List<ClassToDeserialize> results = kafka().poll(kafkaIterable("Результирующий лист", //описываем то, 
                // что нужно получить
                ClassToDeserialize.class, //класс, который реализует Iterable<ClassToDeserialize>
                // или Collection<ClassToDeserialize>
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
    // Из общего сообщения (Iterable/коллекции) вернется под-множество подходящих элементов.
    List<ClassToDeserialize> result = kafka().poll(kafkaIterable("Результирующий лист", //описываем то, 
            // что нужно получить
            new TypeReference<ClassToDeserialize>() {
            }, //тип объекта-модели для десериализации
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
    // Возвращается Iterable или коллекция. Сообщение не обязательно является коллекцией 
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
import static java.time.Duration.ofSeconds;
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
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaRawMessagesIterable;

public class MyTest {

  @Test
  public void myTest() {
    //Если необходимо получить 'сырые' строки, которые возвращаются из kafka
    List<String> results = kafka().poll(kafkaRawMessagesIterable("testTopic"));
  }
}
```

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaRawMessagesIterable;

public class MyTest {

  @Test
  public void myTest() {
    //Если необходимо получить 'сырые' строки, которые возвращаются из kafka
    //Если не указывать топики, то значение топиков возьмется из DEFAULT_TOPICS_FOR_POLL
    List<String> results = kafka().poll(kafkaRawMessagesIterable());
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

### Получение данных сообщения в виде массива

В примерах ниже предполагается, что настройки / свойства / переменные окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER),
[DEFAULT_TOPICS_FOR_POLL](./SETTINGS.MD#DEFAULT_TOPICS_FOR_POLL)
перед началом выполнения имеют непустое значения

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
      // Возвращается массив. Сообщение не обязательно является массивом. 
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
    // Возвращается массив. Сообщение не обязательно является массивом. 
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
      // Возвращается массив. Сообщение не обязательно является массивом. 
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
import static java.time.Duration.ofSeconds;
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
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaRawMessagesArray;

public class MyTest {

  @Test
  public void myTest() {
    //Если необходимо получить 'сырые' строки, которые возвращаются из kafka
    String[] results = kafka().poll(kafkaRawMessagesArray("testTopic"));
  }
}
```

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaRawMessagesArray;

public class MyTest {

  @Test
  public void myTest() {
    //Если необходимо получить 'сырые' строки, которые возвращаются из kafka
    //Если не указывать топики, то значение топиков возьмется из DEFAULT_TOPICS_FOR_POLL
    String[] results = kafka().poll(kafkaRawMessagesArray());
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

### Получение данных сообщения как Java-объект из Iterable

В примерах ниже предполагается, что настройки / свойства / переменные окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER),
[DEFAULT_TOPICS_FOR_POLL](./SETTINGS.MD#DEFAULT_TOPICS_FOR_POLL)
перед началом выполнения имеют непустое значения

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
    //Возвращается элемент коллекции / Iterable.  Сообщение не обязательно является коллекцией 
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
    //Возвращается элемент коллекции / Iterable.  Сообщение не обязательно является коллекцией 
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
import static java.time.Duration.ofSeconds;
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
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaRawMessageIterableItem;

public class MyTest {

  @Test
  public void myTest() {
    //Если необходимо получить 'сырую' строку, которая возвращается из kafka
    String results = kafka().poll(kafkaRawMessageIterableItem("testTopic"));
  }
}
```

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.function.poll.KafkaPollIterableSupplier.kafkaRawMessageIterableItem;

public class MyTest {

  @Test
  public void myTest() {
    //Если необходимо получить 'сырую' строку, которая возвращается из kafka
    //Если не указывать топики, то значение топиков возьмется из DEFAULT_TOPICS_FOR_POLL
    String results = kafka().poll(kafkaRawMessageIterableItem());
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
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER),
[DEFAULT_TOPIC_FOR_SEND](./SETTINGS.MD#DEFAULT_TOPIC_FOR_SEND)
перед началом выполнения имеют непустое значения

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.serializedMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Выполняем отправку сообщения
    //сериализатор берется дефолтный из KAFKA_DEFAULT_DATA_TRANSFORMER
    kafka.send(serializedMessage(new SomeObject()) //new SomeObject() - объект, который нужно сериализовать и отправить в kafka
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
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.serializedMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Если не указывать топик для отправки сообщения, то он возьмет дефолтное значение из DEFAULT_TOPIC_FOR_SEND
    kafka.send(serializedMessage(new SomeObject()));
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
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.serializedMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Выполняем отправку сообщения
    //сериализатор берется дефолтный из KAFKA_DEFAULT_DATA_TRANSFORMER
    kafka.send(serializedMessage(new SomeObject()) //new SomeObject() - объект, который нужно сериализовать и отправить в kafka
            .dataTransformer(new MyDataTransformer()) // устанавливаем кастомный DataTransformer
    );
  }
}
```

Так же Neptune поддерживает отправку обычных строк, которые не нужно сериализовать

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.textMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Выполняем отправку сообщения
    kafka.send(textMessage("I'm a String!") //строка, которую нужно отправить в kafka
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
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.textMessage;

public class MyTest {

  @Test
  public void myTest() {
    //Если не указывать топик для отправки сообщения, то он возьмет дефолтное значение из DEFAULT_TOPIC_FOR_SEND
    kafka.send(textMessage("I'm a String!"));
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)