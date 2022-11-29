# KAFKA_DEFAULT_DATA_TRANSFORMER

Подробнее: [Сериализация и десериализация](./../../../core/serialize_deserialize.rst)

Данное свойство используется для сериализации и десериализации значения (`org.apache.kafka.clients.consumer.ConsumerRecord.value()`)
сообщения

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
        .DefaultDataTransformers.KAFKA_DEFAULT_DATA_TRANSFORMER;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        DataTransformer transformer = KAFKA_DEFAULT_DATA_TRANSFORMER.get();
    }
}
```