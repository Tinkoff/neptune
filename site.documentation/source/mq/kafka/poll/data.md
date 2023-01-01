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
                // можно указать время, 
                // за которое подходящий объект должен быть получен
                .timeOut(ofSeconds(5))
                //Можно указать один или несколько критериев, 
                //которым должен соответствовать
                //результирующий элемент из набора.
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("Описание критерия, которому должен соответствовать " +
                    "результирующий элемент", dto -> {
                    /*предикат, как работает критерий*/
                })
                //Можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела 
                // или не был получен результирующий элемент (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, 
                // или тело десериализовалось в пустой объект Iterable)
                .throwOnNoResult()
                //ТАКЖЕ ЕСТЬ СЛЕДУЮЩИЕ ОПЦИИ:
                //Можно указать индекс элемента, который следует вернуть.
                //Индекс - индекс объекта в наборе элементов, 
                //которые соответствуют критериям 
                .returnItemOfIndex(1)
                //-----------------------------------------------
                //можно указать, при достижении какого количества 
                //ВСЕХ объектов, которые соответствуют критериям, 
                //должен быть возвращен результат 
                //----------------------------------------------
                .returnIfEntireSize(isEqual(8))
                //можно указать, при достижении каких условий,
                //которым должен соответствовать набор ВСЕХ объектов,
                //соответствующих критериям,
                //можно возвращать результирующий элемент 
                .returnOnCondition("Описание условия", iterable -> {
                    /*предикат, как работает критерий*/
                })
            //так же доступны returnOnConditionOr(criteria...), 
            // returnOnConditionOnlyOne(criteria...)
            // returnOnConditionNot(criteria...)
            //------------------------------------------
            //Если не нашлось столько подходящих объектов, чтобы вернуть результат,
            //или вся полученная коллекция не соответствует каким-то критериям
            // будет выброшено исключение с подробным описанием
        );
    }
}
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
                // можно указать время, 
                // за которое непустой лист подходящих объектов 
                // должен быть получен
                .timeOut(ofSeconds(5))
                //Можно указать один или несколько критериев, 
                //которым должен соответствовать
                //каждый элемент результирующего листа.
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("Описание критерия, которому должен соответствовать " +
                    "каждый элемент, который попадет в результирующий лист", someDataObject -> {
                    /*предикат, как работает критерий*/
                })
                //Можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела 
                // или результирующий лист пустой (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, 
                // или тело десериализовалось в пустой лист)
                .throwOnNoResult()
                //ТАКЖЕ ЕСТЬ СЛЕДУЮЩИЕ ОПЦИИ:
                //можно указать сколько объектов,
                //которые соответствуют критериям,
                //нужно вернуть
                .returnListOfSize(3)
                //-------------------------------------------
                //можно указать, до элемента с каким индексом
                //нужно собрать результирующие элементы,
                //индекс - индекс объекта в наборе элементов, 
                //которые соответствуют критериям 
                .returnBeforeIndex(7)
                //.returnAfterIndex(8) либо после какого элемента
                //----------------------------------------------
                //Либо можно перечислить индексы элементов, 
                // которые следует вернуть.
                //Индексы - индексы объектов в наборе элементов, 
                //которые соответствуют критериям 
                .returnItemsOfIndexes(0, 3, 5)
                //-----------------------------------------------
                //можно указать, при достижении какого количества 
                //ВСЕХ объектов, которые соответствуют критериям, 
                //должен быть возвращен результат 
                //----------------------------------------------
                .returnIfEntireSize(isEqual(8))
                //можно указать, при достижении каких условий,
                //которым должен соответствовать лист ВСЕХ объектов,
                //соответствующих критериям,
                //можно возвращать результирующий лист/суб-лист 
                .returnOnCondition("Описание условия", list -> {
                    /*предикат, как работает критерий*/
                })
            //так же доступны returnOnConditionOr(criteria...), 
            // returnOnConditionOnlyOne(criteria...)
            // returnOnConditionNot(criteria...)
            //------------------------------------------
            //Если не нашлось столько подходящих объектов, чтобы вернуть результат,
            //или вся полученная коллекция не соответствует каким-то критериям
            //будет выброшено исключение с подробным описанием
        );
    }
}
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
                // можно указать время, 
                // за которое непустой массив подходящих объектов 
                // должен быть получен
                .timeOut(ofSeconds(5))
                //Можно указать один или несколько критериев, 
                //которым должен соответствовать
                //каждый элемент результирующего массива.
                // Так же доступны criteriaOr(criteria...), 
                // criteriaOnlyOne(criteria...)
                // criteriaNot(criteria...)
                .criteria("Описание критерия, которому должен соответствовать " +
                    "каждый элемент, который попадет в результирующий массив", dto -> {
                    /*предикат, как работает критерий*/
                })
                //Можно указать, что должно быть выброшено исключение, 
                // если у ответа нет тела 
                // или результирующий массив пустой (не было ни одного элемента, 
                //который бы соответствовал перечисленным критериям, 
                // или тело десериализовалось в пустой массив)
                .throwOnNoResult()
                //ТАКЖЕ ЕСТЬ СЛЕДУЮЩИЕ ОПЦИИ:
                //можно указать сколько объектов,
                //которые соответствуют критериям,
                //нужно вернуть
                .returnArrayOfLength(3)
                //-------------------------------------------
                //можно указать, до элемента с каким индексом
                //нужно собрать результирующие элементы,
                //индекс - индекс объекта в наборе элементов, 
                //которые соответствуют критериям 
                .returnBeforeIndex(7)
                //.returnAfterIndex(8) либо после какого элемента
                //----------------------------------------------
                //Либо можно перечислить индексы элементов, 
                // которые следует вернуть.
                //Индексы - индексы объектов в наборе элементов, 
                //которые соответствуют критериям 
                .returnItemsOfIndexes(0, 3, 5)
                //-----------------------------------------------
                //можно указать, при достижении какого количества
                //ВСЕХ объектов, которые соответствуют критериям, 
                //должен быть возвращен результат 
                //----------------------------------------------
                .returnIfEntireLength(isEqual(8))
                //можно указать, при достижении каких условий,
                //которым должен соответствовать массив ВСЕХ объектов,
                //соответствующих критериям,
                //можно возвращать результирующий массив/суб-массив
                .returnOnCondition("Описание условия", array -> {
                    /*предикат, как работает критерий*/
                })
            //так же доступны returnOnConditionOr(criteria...), 
            // returnOnConditionOnlyOne(criteria...)
            // returnOnConditionNot(criteria...)
            //------------------------------------------
            //Если не нашлось столько подходящих объектов, чтобы вернуть результат,
            //или весь полученный массив не соответствует каким-то критериям
            // будет выброшено исключение с подробным описанием
        );
    }
}
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

