# Получение сообщений Kafka и их данных

Важно:

- [Свойство DEFAULT_TOPICS_FOR_POLL](../settings/DEFAULT_TOPICS_FOR_POLL.md)
- [KAFKA_CONSUMER_PROPERTIES](../settings/KAFKA_CONSUMER_PROPERTIES.md)

## Получение сообщений Kafka

```java

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

    @Test
    public void myTest1() {
        List<ConsumerRecord<SomeKeyType, SomeValueType>> records
            = kafka().poll(consumerRecords(
                new SomeKeyTypeDeserializer(), //десериализатор для ключа
                new SomeValueTypeDeserializer()) //десериализатор для значения
                //Можно перечислить топики, из которых следует прочесть 
                //сообщения.
                //Если значение свойства DEFAULT_TOPICS_FOR_POLL непустое,
                //и нет необходимости указывать другие топики для чтения,
                //то метод можно не вызывать
                .fromTopics("testTopic", "testTopic2")
                //Можно указать, чтобы в результирующий лист не попадали
                //сообщения с пустыми значениями.
                //По умолчанию в результат попадают все сообщения.
                //Пустые значения могут быть из-за того, что они либо в самом
                //деле пустые, либо для каких-то сообщений указанный
                //десериализатор значения не подходит
                .excludeWithNullValues()
                //Можно указать, чтобы в результирующий лист не попадали
                //сообщения с пустыми ключами.
                //По умолчанию в результат попадают все сообщения.
                //Пустые ключи могут быть из-за того, что они либо в самом
                //деле пустые, либо для каких-то сообщений указанный
                //десериализатор ключа не подходит
                .excludeWithNullKeys()
                //Можно указать свойства и их значения,
                //которые могут дополнить/заменить свойства, 
                //получаемые при помощи KAFKA_CONSUMER_PROPERTIES.
                //ВНИМАНИЕ!!!!! Нельзя указывать свойство `auto.offset.reset`. 
                // Это приведет к IllegalArgumentException
                .setProperty(GROUP_ID_CONFIG, "someGroupId")
                //Можно указать действие, с момента начала выполнения которого
                //начнется чтение сообщений из топиков. Предполагается, что прочитанные сообщения - 
                //результат указанного действия. При этом свойство `auto.offset.reset` текущего консъюмера
                //меняется на `latest`. Если действие не указывать, будут прочитаны все сообщения из топиков,
                //и значение свойства `auto.offset.reset` текущего консъюмера равно `earliest`
                .pollLatestWith("Название или описание действия", () -> {
                    //тут можно выполнять шаги из других контекстов 
                    //или другой исполняемый код.
                })
                //прочие опции, уточняющие результат
        );
    }
}
```

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_list_optiotal_parameters_async.rst
```

Если не нужно использовать десериализаторы, то пример ниже

```java

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

    @Test
    public void myTest1() {
        List<ConsumerRecord<String, String>> records
            = kafka().poll(consumerRecords()
              //набор дополнительных опций тот же самый,
              //см пример выше
        );
    }
}
```

## Получение данных из выборки сообщений Kafka

### Данные сообщений Kafka, собранные в лист

```java

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

    @Test
    public void myTest1() {
        List<SomeData> someDataList
            = kafka().poll(consumerRecords(//параметры
            )
            //опции, уточняющие результат
            //
            .thenGetList(
                "Описание / название результирующего листа", //описание того ЧТО следует получить,
                //в свободной форме или бизнес
                //терминологии
                consumerRecord -> {
                    //Алгоритм функции, которая получает данные
                    //из каждого сообщения
                    return someDataObject; //эти объекты
                    //будут собраны в результирующий лист
                })
                //опции, уточняющие результат
        );
    }
}
```

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_list_optiotal_parameters_sync.rst
```

### Объект данных из сообщений Kafka

```java

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.poll.GetRecordSupplier.consumerRecords;

public class MyTest {

    @Test
    public void myTest1() {
        SomeData someData
            = kafka().poll(consumerRecords(//параметры
            )
            //опции, уточняющие результат
            //
            .thenGetItem(
                "Описание / название результирующего объекта", //описание того 
                // ЧТО следует получить,
                //в свободной форме или бизнес
                //терминологии
                consumerRecord -> {
                    //Алгоритм функции, которая получает данные
                    //из каждого сообщения
                    return someDataObject; //один из этих 
                    //объектов возвращается как результат,
                    //первый попавшийся или с учетом уточнений
                })
                //опции, уточняющие результат
        );
    }
}
```

```{eval-rst}
.. include:: ../../../shared_docs/steps_return_iterable_item_optiotal_parameters_sync.rst
```