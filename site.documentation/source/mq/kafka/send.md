# Send-операции Kafka

Важно:

- [Свойство DEFAULT_TOPIC_FOR_SEND](settings/DEFAULT_TOPIC_FOR_SEND.md)
- [KAFKA_PRODUCER_PROPERTIES](settings/KAFKA_PRODUCER_PROPERTIES.md)
- [KAFKA_CALL_BACK](settings/KAFKA_CALL_BACK.md)
- [Шаги, выполняющие действие](./../../core/steps/steps/action_supplier.md)

```java
import static org.apache.kafka.clients.producer.ProducerConfig.MAX_REQUEST_SIZE_CONFIG;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.producerRecord;

public class MyTest {

    @Test
    public void myTest1() {
        kafka().send(producerRecord(
            new SomeTypeSerializer(), //сериализатор для значения 
            //оправляемого сообщения
            someTypeInstance) //Значение отправляемого сообщения
            //Можно указать топик, в который следует отправить сообщение.
            //Если значение свойства DEFAULT_TOPIC_FOR_SEND непустое,
            //и нет необходимости указывать другой топик для отправки,
            //то метод можно не вызывать
            .topic("testTopic")
            //Метод вызывается, когда нужно передать кол-бэк,
            //и он отличается от того, который полуют при помощи
            //свойства KAFKA_CALL_BACK
            .callback(customCallBack)
            //Можно указать свойства и их значения,
            //которые могут дополнить/заменить свойства, 
            //получаемые при помощи KAFKA_PRODUCER_PROPERTIES
            .setProperty(MAX_REQUEST_SIZE_CONFIG, requestSizeConf)
            //по умолчанию считается, что ключ сообщения 
            //будет оправлен в виде текста, и ключ пустой
            .setKey("Some String") //можно указать ключ в виде объекта,
            .setKey(someKeyInstance, new SomeKeyTypeSerializer()) //либо
            //в виде объекта вместе с сериализатором, 
            // если тип нового объекта-ключа отличается от предыдущего
            //типа объектов-ключей.
            //
            //Опционально
            .partition(1)
            //опционально
            .timestamp(10L)
            //опционально можно передать заголовки
            .header("Header key", "Value1") //в разных форматах
            .header(new RecordHeader("Header key2", "Value2".getBytes()))
        );
    }
}
```

Ниже упрощенный пример, если надо оправить сообщение с текстовым значением

```java
import static org.apache.kafka.clients.producer.ProducerConfig.MAX_REQUEST_SIZE_CONFIG;

import static ru.tinkoff.qa.neptune.kafka.KafkaStepContext.kafka;
import static ru.tinkoff.qa.neptune.kafka.functions.send.KafkaSendRecordsActionSupplier.producerRecord;

public class MyTest {

    @Test
    public void myTest1() {
        kafka().send(producerRecord("I'm a String!")
            //опциональные уточняющие 
            //параметры те же самые, что в примере выше 
        );
    }
}
```
