# Настройки Kafka модуля для дальнейшей работы

[Подробнее о механизме настроек](./../../../core.api/doc/rus/SETTINGS.MD).

Описанный ниже набор настроек/свойств
дополняет [базовый набор настроек/свойств](./../../../core.api/doc/rus/SETTINGS.MD#Основной-набор-настроексвойств),
которые могут быть включены в `neptune.global.properties` или в `neptune.properties`, или использованы в качестве
свойств/переменных окружения/среды в той среде, в которой запускаются тесты.

## Оглавление

- [KAFKA_CONSUMER_PROPERTIES](./SETTINGS.MD#KAFKA_CONSUMER_PROPERTIES)
- [KAFKA_PRODUCER_PROPERTIES](./SETTINGS.MD#KAFKA_PRODUCER_PROPERTIES)
- [KAFKA_DEFAULT_DATA_TRANSFORMER](./SETTINGS.MD#KAFKA_DEFAULT_DATA_TRANSFORMER)
- [DEFAULT_TOPIC_FOR_SEND](./SETTINGS.MD#DEFAULT_TOPIC_FOR_SEND)
- [DEFAULT_TOPICS_FOR_POLL](./SETTINGS.MD#DEFAULT_TOPICS_FOR_POLL)
- [KAFKA_CALL_BACK](./SETTINGS.MD#KAFKA_CALL_BACK)
- [KAFKA_KEY_TRANSFORMER](./SETTINGS.MD#KAFKA_KEY_TRANSFORMER)

## KAFKA_CONSUMER_PROPERTIES

Настройка предоставляет объект `Properties`, подготовленный нужным образом, и который используется kafka клиентом для
создания kafkaConsumer'a.

Примеры properties можно посмотреть [тут](https://kafka.apache.org/documentation/#consumerconfigs)

```java
//Создаем поставщика, который будет поставлять объект класса Properties
public class MyConsumerPropertiesSupplier implements ObjectPropertySupplier<Properties, Supplier<Properties>> {

    @Override
    public Properties get() {
        //Тут создается объект, который будет использован клиентом
    }
}
```

```properties
#Значение свойства указывается так
KAFKA_CONSUMER_PROPERTIES=org.my.pack.MyConsumerPropertiesSupplier
```

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.kafka.properties.KAFKA_CONSUMER_PROPERTIES;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        Properties consumerProperties = KAFKA_CONSUMER_PROPERTIES.get();
    }
}
```

## KAFKA_PRODUCER_PROPERTIES

Настройка предоставляет объект `Properties`, подготовленный нужным образом, и который используется kafka клиентом для
создания kafkaProducer'a.

Примеры properties можно посмотреть [тут](https://kafka.apache.org/documentation/#producerconfigs)

```java
//Создаем поставщика, который будет поставлять объект класса Properties
public class MyProducerPropertiesSupplier implements ObjectPropertySupplier<Properties, Supplier<Properties>> {

    @Override
    public Properties get() {
        //Тут создается объект, который будет использован клиентом
    }
}
```

```properties
#Значение свойства указывается так
KAFKA_PRODUCER_PROPERTIES=org.my.pack.MyProducerPropertiesSupplier
```

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.kafka.properties.KAFKA_PRODUCER_PROPERTIES;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        Properties producerProperties = KAFKA_PRODUCER_PROPERTIES.get();
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

## DEFAULT_TOPIC_FOR_SEND

Данная настройка предоставляет возможность указывать дефолтный топик для отправки сообщения.

```properties
#Значение свойства указывается так
DEFAULT_TOPIC_FOR_SEND=testTopic
```

```java
import static ru.tinkoff.qa.neptune.kafka.properties.DEFAULT_TOPIC_FOR_SEND;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        String topic = DEFAULT_TOPIC_FOR_SEND.get();
    }
}
```

## DEFAULT_TOPICS_FOR_POLL

Данная настройка предоставляет возможность указывать дефолтный список топиков для получения сообщений.

```properties
#Значение топиков указываются через запятую
DEFAULT_TOPICS_FOR_POLL=testTopic1,testTopic2,testTopic3
```

```java
import static ru.tinkoff.qa.neptune.kafka.properties.DEFAULT_TOPICS_FOR_POLL;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        String[] topics = DEFAULT_TOPICS_FOR_POLL.get();
    }
}
```

## KAFKA_CALL_BACK

Данная настройка предоставляет возможность указывать дефолтный CallBack.

```java
//Создаем поставщика, который будет поставлять объект класса CallBack
public class CallBackSupplier implements ObjectPropertySupplier<CallBack, Supplier<CallBack>> {

    @Override
    public CallBack get() {

    }
}
```

```properties
#Значение свойства указывается так
KAFKA_CALL_BACK=org.my.pack.CallBackSupplier
```

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.kafka.properties.KAFKA_CALL_BACK;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        CallBack myCallBack = KAFKA_CALL_BACK.get();
    }
}
```

## KAFKA_KEY_TRANSFORMER

Основная статья: [Сериализация и десериализация](./../../../core.api/doc/rus/SERIALIZATION_DESERIALIZATION.MD)
Используется: [Получение сообщения](FUNCTION.MD#Получение-сообщения)

```java
import org.my.pack;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

//Описываем сериализацию key для текущего проекта
public class MyKeyTransformer implements DataTransformer {


    @Override
    public <T> T deserialize(String message, Class<T> cls) {
        //тут описывается механизм десериализации
        // но для поля ключ нас интересует только метод serialize
    }

    @Override
    public <T> T deserialize(String string, TypeReference<T> type) {
        //тут описывается механизм десериализации
        // но для поля ключ нас интересует только метод serialize
    }

    @Override
    public String serialize(Object obj) {
        //тут описываем механизм сериализации ключа
    }
}
```

```properties
#Значение свойства указывается так
KAFKA_KEY_TRANSFORMER=org.my.pack.MyKeyTransformer
```

```java
import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

import static ru.tinkoff.qa.neptune.kafka.properties
        .KafkaDefaultDataTransformer.KAFKA_KEY_TRANSFORMER;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        DataTransformer transformer = KAFKA_KEY_TRANSFORMER.get();
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)