# DEFAULT_TOPIC_FOR_SEND

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