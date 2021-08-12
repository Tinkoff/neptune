# Работа с Kafka

В данном документе описано на примерах, как работать с брокером сообщений

## Оглавление

- [Получение сообщения](#Получение-сообщения)
    - [Получение данных сообщения как Iterable](#Получение-данных-сообщения-как-Iterable)
    - [Получение данных сообщения в виде массива](#Получение-данных-сообщения-в-виде-массива)
    - [Получение данных сообщения как Java-объект из Iterable](#Получение-данных-сообщения-как-Java-объект-из-Iterable)
    - [Получение данных сообщения как Java-объект из массива](#Получение-данных-сообщения-как-Java-объект-из-массива)
- [Публикация сообщений](#Публикация-сообщений)

## Получение сообщения

О принципах работы шагов, которые возвращают результат, можно прочитать
[здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных сообщения как Iterable

В примерах ниже предполагается, что настройка / свойство / переменная окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER) перед началом выполнения имеет непустое
значения

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
                of("test_topic"), //список топиков
                ClassToDeserialize.class) //класс, который реализует Iterable<ClassToDeserialize> 
                // или Collection<ClassToDeserialize>
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
                of("test_topic"), //список топиков
                new TypeReference<ClassToDeserialize>() {
                }) //тип объекта-модели для десериализации
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
                of("test_topic"), //список топиков
                ClassToDeserialize.class, //класс модели для десериализации
                o -> {
                    return /*описываем то, как получаем из ClassToDeserialize Object */;
                })
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать каждый элемент результирующей коллекции
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
        // если результат не был получен
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
                of("test_topic"),
                ClassToDeserialize.class),
                new MyDataTransformer());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных сообщения в виде массива

В примерах ниже предполагается, что настройка / свойство / переменная окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER) перед началом выполнения имеет непустое
значения

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
                of("testTopic"), //список топиков
                ClassToDeserialize.class) //класс массива
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
                of("test_topic"), //список топиков
                ClassToDeserialize.class, //класс модели для десериализации
                Object.class, //необходимо явно указать результирующий класс функции
                o -> {
                    return /*описываем то, как получаем из ClassToDeserialize в Object */;
                })
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
                of("testTopic"), //список топиков
                new TypeReference<ClassToDeserialize>() {
                }) //тип объекта-модели для десериализации
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
                of("testTopic"), //список топиков
                new TypeReference<ClassToDeserialize>() {
                }, //тип объекта-модели для десериализации
                Object.class, //необходимо явно указать результирующий класс функции
                o -> {
                    return /*описываем то, как получаем из ClassToDeserialize в Object */;
                })
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать каждый элемент результирующего массива
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
        // если результат не был получен
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
        kafka().poll(kafkaArray("Результирующий массив",
                of("testTopic"),
                ClassToDeserialize.class), //или ссылка на тип
                new MyDataTransformer());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных сообщения как Java-объект из Iterable

В примерах ниже предполагается, что настройка / свойство / переменная окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER) перед началом выполнения имеет непустое
значения

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
                of("testTopic"), //список топиков
                ClassToDeserialize.class)
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
                of("testTopic"),
                new TypeReference<ClassToDeserialize>() {
                }) //тип объекта-модели для десериализации
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
                of("testTopic"), //имя очереди
                ClassToDeserialize.class, //класс модели для десериализации
                o -> {
                    return /*описываем то, как получаем из объекта ClassToDeserialize объект  Object*/;
                })
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
                of("testTopic"),
                new TypeReference<ClassToDeserialize>() {
                }, //тип объекта-модели для десериализации
                o -> {
                    return /*описываем то, как получаем из объекта ClassToDeserialize объект  Object*/;
                })
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать результирующий элемент из коллекции
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
        // если результат не был получен
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
        kafka().poll(kafkaIterableItem("Нужный элемент листа",
                of("testTopic"),
                ClassToDeserialize.class),
                new MyDataTransformer());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Получение данных сообщения как Java-объект из массива

В примерах ниже предполагается, что настройка / свойство / переменная окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER) перед началом выполнения имеет непустое
значения

```java
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArrayItemSupplier.kafkaArrayItem;

public class MyTest {

    @Test
    public void myTest() {
        //Возвращается элемент массива. Сообщение из очереди десериализуется в массив.
        //Из общего сообщения-массива вернется первый подходящий элемент.
        ClassToDeserialize result = kafka().poll(kafkaArrayItem("Нужный элемент массива", //описываем то, 
                // что нужно получить
                of("testTopic"), //список топиков
                ClassToDeserialize.class) //класс массива
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать результирующий элемент из массива
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
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArrayItemSupplier.kafkaArrayItem;

public class MyTest {

    @Test
    public void myTest() {
        //Возвращается элемент массива. Сообщение из очереди десериализуется в массив.
        //Из общего сообщения-массива вернется первый подходящий элемент.
        ClassToDeserialize result = kafka().poll(kafkaArrayItem("Нужный элемент массива",
                of("testTopic"),
                new TypeReference<ClassToDeserialize>() {
                }) //тип объекта-модели для десериализации
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать результирующий элемент из массива
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
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArrayItemSupplier.kafkaArrayItem;

public class MyTest {

    @Test
    public void myTest() {
        //Возвращается элемент массива. Сообщение не обязательно является массивом. 
        // Промежуточный массив получаются в ходе дополнительных вычислений. 
        // Из массива вернется первый подходящий элемент.
        Object result = kafka().poll(kafkaArrayItem("Нужный элемент массива", //описываем 
                // то, что нужно получить
                of("testTopic"),
                ClassToDeserialize.class, //класс модели для десериализации
                o -> {
                    return /*описываем то, как получить из объекта ClassToDeserialize объект Object */;
                })
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать результирующий элемент массива
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
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArrayItemSupplier.kafkaArrayItem;

public class MyTest {

    @Test
    public void myTest() {
        //Возвращается элемент массива. Сообщение не обязательно является массивом. 
        // Промежуточный массив получаются в ходе дополнительных вычислений. 
        // Из массива вернется первый подходящий элемент.
        Object result = kafka().poll(kafkaArrayItem("Нужный элемент массива",
                of("testTopic"),
                ClassToDeserializ.class, //тип объекта-модели для десериализации
                o -> {
                    return /*описываем то, как получить из объекта ClassToDeserialize объект Object*/;
                })
                .criteria("Описание критерия", cls -> cls.getSomeField().equals("someValue")) //можно указать критерий, 
                // которому должен соответствовать результирующий элемент из массива
                .timeOut(ofSeconds(5)) //можно указать время на получение 
                // ожидаемого результата = 5 секунд
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
        // если результат не был получен
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
        kafka().poll(kafkaArrayItem("Нужный элемент массива",
                of("testTopic"),
                ClassToDeserialize.class),
                new MyDataTransformer());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Публикация сообщений

В примерах ниже предполагается, что настройка / свойство / переменная окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER) перед началом выполнения имеет непустое
значения

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
        rabbitMq().publishMessage("exchange",
                "routingKey",
                new SomeObject());
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
        rabbitMq().publishMessage("exchange",
                "routingKey",
                parameters().mandatory().immediate(),
                new SomeObject());
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
        //      Неявные параметры которые уходят:
        //         mandatory со значением false
        //         mmediate со значением false
        //  new MyDataTransformer() - переданный сериализатор
        rabbitMq().publishMessage("exchange",
                "routingKey",
                new SomeObject(),
                new MyDataTransformer());
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
        //      new MyDataTransformer() - переданный сериализатор
        rabbitMq().publishMessage("exchange",
                "routingKey",
                parameters().mandatory().immediate(),
                new SomeObject(),
                new MyDataTransformer());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Публикация сообщений

В примерах ниже предполагается, что настройка / свойство / переменная окружения
[KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER) перед началом выполнения имеет непустое
значения

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем отправку сообщения
        //  Параметры:
        //      testTopic - топик для публикации сообщения
        //      new SomeObject() - объект, который нужно сериализовать и отправить в kafka

        kafka().sendMessage("testTopic", new SomeObject());

    }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем отправку сообщения
        //  Параметры:
        //      testTopic - топик для публикации сообщения
        //      new SomeObject() - объект, который нужно сериализовать и отправить в kafka
        //      callBack - обратного вызова, который пользователь может реализовать, 
        //      чтобы разрешить выполнение кода после завершения запроса. 

        kafka().sendMessage("testTopic", new SomeObject(), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                    }
                }
        );
    }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем отправку сообщения
        //  Параметры:
        //      testTopic - топик для публикации сообщения
        //      new SomeObject() - объект, который нужно сериализовать и отправить в kafka
        //      ParametersForSend - объект, необходимый для изменения установки дополнительных параметров, таких как:
        //                           partition, timestamp, key, headers

        kafka().sendMessage("testTopic", new SomeObject(), parameters());
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
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import static ru.tinkoff.qa.neptune.rabbit.mq.RabbitMqStepContext.rabbitMq;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем отправку сообщения
        //  Параметры:
        //      testTopic - топик для публикации сообщения
        //      new SomeObject() - объект, который нужно сериализовать и отправить в kafka
        //      new CustomMapper() - переданный сериализатор

        kafka().sendMessage("testTopic", new SomeObject(), new CustomMapper());
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
        kafka().sendMessage("testTopic",
                new SomeObject(),
                parameters().partition(1).timestamp(10L),
                callBack,
                new CustomMapper());
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)