# DEFAULT_TOPICS_FOR_POLL

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