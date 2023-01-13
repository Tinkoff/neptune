# Получение данных из Kafka

Отличие описанного ниже от того, что описано в [Получение сообщений Kafka и их данных](consumer_records.md) в том, что
здесь нет промежуточного действия в виде получения сообщений, из которых извлекаются данные. Данные получаются как бы
напрямую.

Важно:

- [Свойство DEFAULT_TOPICS_FOR_POLL](../settings/DEFAULT_TOPICS_FOR_POLL.md)
- [KAFKA_CONSUMER_PROPERTIES](../settings/KAFKA_CONSUMER_PROPERTIES.md)

## Получение объекта данных Kafka

```java

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        SomeData someDataItem = kafka().poll(consumedItem(
                "Описание / название результирующего объекта", //описание того 
                // ЧТО следует получить,
                //в свободной форме или бизнес
                //терминологии
                new SomeKeyTypeDeserializer(), //десериализатор для ключа
                new SomeValueTypeDeserializer(), //десериализатор для значения
                consumerRecord -> {
                    //Алгоритм функции, которая получает данные
                    //из каждого сообщения
                    return someDataObject; //один из этих 
                    //объектов возвращается как результат,
                    //первый попавшийся или с учетом уточнений
                })
                //Можно перечислить топики, из которых следует прочесть 
                //данные сообщений.
                //Если значение свойства DEFAULT_TOPICS_FOR_POLL непустое,
                //и нет необходимости указывать другие топики для чтения,
                //то метод можно не вызывать
                .fromTopics("testTopic", "testTopic2")
                //Можно указать свойства и их значения,
                //которые могут дополнить/заменить свойства, 
                //получаемые при помощи KAFKA_CONSUMER_PROPERTIES
                .setProperty(GROUP_ID_CONFIG, "someGroupId")
                //уточняющие параметры
        );
    }
}
```

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_iterable_item_optiotal_parameters_async.rst
```

Ниже другие варианты получения объекта из очередей

```java

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        SomeData someDataItem = kafka().poll(consumedItemKeyData(
                "Описание / название результирующего объекта", //описание того 
                // ЧТО следует получить,
                //в свободной форме или бизнес
                //терминологии
                new SomeKeyTypeDeserializer(), //десериализатор для ключа
                keyData -> {
                    //Алгоритм функции, которая получает данные
                    //из каждого ключа сообщения
                    return someDataObject; //один из этих 
                    //объектов возвращается как результат,
                    //первый попавшийся или с учетом уточнений
                })
            //уточняющие параметры
        );
    }
}
```

```java

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        //возврат одного из ключей сообщений
        SomeKeyType someKey = kafka().poll(consumedKey(
            new SomeKeyTypeDeserializer()) //десериализатор для ключа
            //уточняющие параметры
        );
    }
}
```

```java

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        //возврат одного из ключей сообщений
        //в виде текста
        String someKey = kafka().poll(consumedKey()
            //уточняющие параметры
        );
    }
}
```

```java

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        SomeData someDataItem = kafka().poll(consumedItemValueData(
                "Описание / название результирующего объекта", //описание того 
                // ЧТО следует получить,
                //в свободной форме или бизнес
                //терминологии
                new SomeValueTypeDeserializer(), //десериализатор для значения
                valueData -> {
                    //Алгоритм функции, которая получает данные
                    //из каждого значения сообщения
                    return someDataObject; //один из этих 
                    //объектов возвращается как результат,
                    //первый попавшийся или с учетом уточнений
                })
            //уточняющие параметры
        );
    }
}
```

```java

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        //возврат одного из значений сообщений
        SomeValueType someDataItem = kafka().poll(consumedValue(
            new SomeValueTypeDeserializer()) //десериализатор для значения
            //уточняющие параметры
        );
    }
}
```

```java

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableItemSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        //возврат одного из значений сообщений
        //в виде текста
        SomeValueType someDataItem = kafka().poll(consumedValue()
            //уточняющие параметры
        );
    }
}
```

## Получение объектов данных Kafka

### В виде листа

```java

import java.util.List;

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        List<SomeData> someDataList
            = kafka().poll(consumedList("Описание / название результирующего листа", //описание того 
                // ЧТО следует получить,
                //в свободной форме или бизнес
                //терминологии
                new SomeKeyTypeDeserializer(), //десериализатор для ключа
                new SomeValueTypeDeserializer(), //десериализатор для значения
                cr -> {
                    //Алгоритм функции, которая получает данные
                    //из каждого сообщения
                    return someDataObject; //эти объекты
                    //будут собраны в результирующий лист
                })
                //Можно перечислить топики, из которых следует прочесть 
                //данные сообщений.
                //Если значение свойства DEFAULT_TOPICS_FOR_POLL непустое,
                //и нет необходимости указывать другие топики для чтения,
                //то метод можно не вызывать
                .fromTopics("testTopic", "testTopic2")
                //Можно указать свойства и их значения,
                //которые могут дополнить/заменить свойства, 
                //получаемые при помощи KAFKA_CONSUMER_PROPERTIES
                .setProperty(GROUP_ID_CONFIG, "someGroupId")
                //уточняющие параметры
        );
    }
}
```

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_list_optiotal_parameters_async.rst
```

Ниже другие варианты получения объектов из очередей

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        List<SomeData> someDataList = kafka().poll(consumedListKeyData(
                "Описание / название результирующего листа", //описание того 
                // ЧТО следует получить,
                //в свободной форме или бизнес
                //терминологии
                new SomeKeyTypeDeserializer(), //десериализатор для ключа
                keyData -> {
                    //Алгоритм функции, которая получает данные
                    //из каждого ключа сообщения
                    return someDataObject; //эти объекты
                    //будут собраны в результирующий лист
                })
            //уточняющие параметры
        );
    }
}
```

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        //возврат листа ключей сообщений
        List<SomeKeyType> someKeyList = kafka().poll(consumedKeys(
            new SomeKeyTypeDeserializer()) //десериализатор для ключа
            //уточняющие параметры
        );
    }
}
```

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        //возврат листа ключей сообщений
        //в виде текстов
        List<String> someKeyList = kafka().poll(consumedKeys()
            //уточняющие параметры
        );
    }
}
```

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        List<SomeData> someDataList = kafka().poll(consumedListValueData(
                "Описание / название результирующего листа", //описание того 
                // ЧТО следует получить,
                //в свободной форме или бизнес
                //терминологии
                new SomeValueTypeDeserializer(), //десериализатор для значения
                valueData -> {
                    //Алгоритм функции, которая получает данные
                    //из каждого значения сообщения
                    return someDataObject; //эти объекты
                    //будут собраны в результирующий лист
                })
            //уточняющие параметры
        );
    }
}
```

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        //возврат листа значений сообщений
        List<SomeValueType> someValueList = kafka().poll(consumedValues(
            new SomeValueTypeDeserializer()) //десериализатор для значения
            //уточняющие параметры
        );
    }
}
```

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollIterableSupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        //возврат листа значений сообщений
        //в виде текстов
        List<String> someValueList = kafka().poll(consumedValues()
            //уточняющие параметры
        );
    }
}
```

### В виде массива

```java

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        SomeData[] someDataArray
            = kafka().poll(consumedArray("Описание / название результирующего массива", //описание того 
                // ЧТО следует получить,
                //в свободной форме или бизнес
                //терминологии
                new SomeKeyTypeDeserializer(), //десериализатор для ключа
                new SomeValueTypeDeserializer(), //десериализатор для значения
                SomeData.class, //класс результирующего массива
                //так же можно использовать объект
                //com.fasterxml.jackson.core.type.TypeReference
                cr -> {
                    //Алгоритм функции, которая получает данные
                    //из каждого сообщения
                    return someDataObject; //эти объекты
                    //будут собраны в результирующий массив
                })
                //Можно перечислить топики, из которых следует прочесть 
                //данные сообщений.
                //Если значение свойства DEFAULT_TOPICS_FOR_POLL непустое,
                //и нет необходимости указывать другие топики для чтения,
                //то метод можно не вызывать
                .fromTopics("testTopic", "testTopic2")
                //Можно указать свойства и их значения,
                //которые могут дополнить/заменить свойства, 
                //получаемые при помощи KAFKA_CONSUMER_PROPERTIES
                .setProperty(GROUP_ID_CONFIG, "someGroupId")
                //опции, уточняющие результат
        );
    }
}
```

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_array_optiotal_parameters_async.rst
```

Ниже другие варианты получения массива объектов из очередей

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        SomeData[] someDataArray = kafka().poll(consumedArrayKeyData(
                "Описание / название результирующего массива", //описание того 
                // ЧТО следует получить,
                //в свободной форме или бизнес
                //терминологии
                new SomeKeyTypeDeserializer(), //десериализатор для ключа
                SomeData.class, //класс результирующего массива
                //так же можно использовать объект
                //com.fasterxml.jackson.core.type.TypeReference
                keyData -> {
                    //Алгоритм функции, которая получает данные
                    //из каждого ключа сообщения
                    return someDataObject; //эти объекты
                    //будут собраны в результирующий массив
                })
            //уточняющие параметры
        );
    }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        //возврат массива ключей сообщений
        SomeKeyType[] someKeyArray = kafka().poll(consumedArrayKeys(
            SomeKeyType.class, //класс результирующего массива
            // так же можно использовать объект
            //com.fasterxml.jackson.core.type.TypeReference
            new SomeKeyTypeDeserializer()) //десериализатор для ключа
            //уточняющие параметры
        );
    }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        //возврат массива ключей сообщений
        //в виде текстов
        String[] someKeyArray = kafka().poll(consumedArrayKeys()
            //уточняющие параметры
        );
    }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        SomeData[] someDataArray = kafka().poll(consumedArrayValueData(
                "Описание / название результирующего массива", //описание того 
                // ЧТО следует получить,
                //в свободной форме или бизнес
                //терминологии
                new SomeValueTypeDeserializer(), //десериализатор для значения
                SomeData.class, //класс результирующего массива
                //так же можно использовать объект
                //com.fasterxml.jackson.core.type.TypeReference
                valueData -> {
                    //Алгоритм функции, которая получает данные
                    //из каждого значения сообщения
                    return someDataObject; //эти объекты
                    //будут собраны в результирующий массив
                })
            //уточняющие параметры
        );
    }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        //возврат массива значений сообщений
        SomeValueType[] someValueArray = kafka().poll(consumedArrayValues(
            SomeValueType.class, //класс результирующего массива
            // так же можно использовать объект
            //com.fasterxml.jackson.core.type.TypeReference
            new SomeValueTypeDeserializer()) //десериализатор для значения
            //уточняющие параметры
        );
    }
}
```

```java
import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.KafkaPollArraySupplier.*;

public class MyTest {

    @Test
    public void myTest1() {
        //возврат массива значений сообщений
        //в виде текстов
        String[] someValueArray = kafka().poll(consumedArrayValues()
            //уточняющие параметры
        );
    }
}
```