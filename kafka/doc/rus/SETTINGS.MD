# Настройки Kafka модуля для дальнейшей работы

[Подробнее о механизме настроек](./../../../core.api/doc/rus/SETTINGS.MD).

Описанный ниже набор настроек/свойств
дополняет [базовый набор настроек/свойств](./../../../core.api/doc/rus/SETTINGS.MD#Основной-набор-настроексвойств),
которые могут быть включены в `neptune.global.properties` или в `neptune.properties`, или использованы в качестве
свойств/переменных окружения/среды в той среде, в которой запускаются тесты.

## Оглавление

- [KAFKA_CONSUMER](./SETTINGS.MD#KAFKA_CONSUMER)
- [KAFKA_PRODUCER](./SETTINGS.MD#KAFKA_PRODUCER)
- [KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER)

## KAFKA_CONSUMER

Настройка предоставляет объект `KafkaConsumer`, подготовленный нужным образом, и который используется kafka клиентом по
умолчанию.

```java
//Создаем поставщика, который будет поставлять объекты класса KafkaConsumer
public class MyConsumerSupplier implements ObjectPropertySupplier<KafkaConsumer<Object, Object>, Supplier<KafkaConsumer<Object, Object>>> {

    @Override
    public KafkaConsumer<Object, Object> get() {
        //Тут создается объект, который будет использован клиентом
    }
}
```

```properties
#Значение свойства указывается так
KAFKA_CONSUMER=org.my.pack.MyConsumerSupplier
```

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.kafka.properties.KAFKA_CONSUMER;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        KafkaConsumer<Object, Object> consumer = KAFKA_CONSUMER.get();
    }
}
```

## KAFKA_PRODUCER

Настройка предоставляет объект `KafkaProducer`, подготовленный нужным образом, и который используется kafka клиентом по
умолчанию.

```java
//Создаем поставщика, который будет поставлять объекты класса KafkaConsumer
public class MyProducerSupplier implements ObjectPropertySupplier<KafkaProducer<Object, Object>, Supplier<KafkaProducer<Object, Object>>> {

    @Override
    public KafkaProducer<Object, Object> get() {
        //Тут создается объект, который будет использован клиентом
    }
}
```

```properties
#Значение свойства указывается так
KAFKA_PRODUCER=org.my.pack.MyProducerSupplier
```

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.kafka.properties.KAFKA_PRODUCER;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        KafkaProducer<Object, Object> producer = KAFKA_PRODUCER.get();
    }
}
```

## KAFKA_DEFAULT_DATA_TRANSFORMER

Основная статья: [Сериализация и десериализация](./../../../core.api/doc/rus/SERIALIZATION_DESERIALIZATION.MD)
Используется: [Получение сообщения](FUNCTION.MD#Получение-сообщения)

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

```properties
#Значение свойства указывается так
KAFKA_DEFAULT_DATA_TRANSFORMER=org.my.pack.MyDataTransformer
```

```java
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

import static ru.tinkoff.qa.neptune.kafka.properties
        .KafkaDefaultDataTransformer.KAFKA_DEFAULT_DATA_TRANSFORMER;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        DataTransformer transformer = KAFKA_DEFAULT_DATA_TRANSFORMER.get();
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)