## DEFAULT_TOPIC_FOR_SEND

Данная настройка предоставляет возможность указывать дефолтный топик для отправки сообщения.

```properties
#Значение свойства указывается так
DEFAULT_TOPIC_FOR_SEND=testTopic
```

```java
import static ru.tinkoff.qa.neptune.kafka.properties
        .KafkaDefaultTopicForSendProperty.DEFAULT_TOPIC_FOR_SEND;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        String topic = DEFAULT_TOPIC_FOR_SEND.get();
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## DEFAULT_TOPICS_FOR_POLL

Данная настройка предоставляет возможность указывать дефолтный список топиков для получения сообщений.

```properties
#Значение топиков указываются через запятую
DEFAULT_TOPICS_FOR_POLL=testTopic1,testTopic2,testTopic3
```

```java
import static ru.tinkoff.qa.neptune.kafka.properties
        .KafkaDefaultTopicsForPollProperty.DEFAULT_TOPICS_FOR_POLL;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        String[] topics = DEFAULT_TOPICS_FOR_POLL.get();
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## KAFKA_CALL_BACK

Используется: [Публикация сообщений](FUNCTION.MD#Публикация-сообщений)

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

import static ru.tinkoff.qa.neptune.kafka.properties
        .KafkaCallbackProperty.KAFKA_CALL_BACK;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        CallBack myCallBack = KAFKA_CALL_BACK.get();
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## KAFKA_KEY_TRANSFORMER

Основная статья: [Сериализация и десериализация](./../../../core.api/doc/rus/SERIALIZATION_DESERIALIZATION.MD)

Используется: [Публикация сообщений](FUNCTION.MD#Публикация-сообщений)

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
        .DefaultDataTransformers.KAFKA_KEY_TRANSFORMER;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        DataTransformer transformer = KAFKA_KEY_TRANSFORMER.get();
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)