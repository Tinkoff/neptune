# KAFKA_CALL_BACK

Данная настройка предоставляет возможность указывать дефолтный CallBack.

```java
import org.apache.kafka.clients.producer.Callback;

//Создаем поставщика, который будет поставлять объект класса CallBack
public class CallBackSupplier implements ObjectPropertySupplier<CallBack, Supplier<CallBack>> {

    @Override
    public CallBack get() {
        //алгоритм создания/получения объекта
    }
}
```

```properties
#Значение свойства указывается так
KAFKA_CALL_BACK=org.my.pack.CallBackSupplier
```

```java
import org.apache.kafka.clients.producer.Callback;

import static ru.tinkoff.qa.neptune.kafka.properties
        .KafkaCallbackProperty.KAFKA_CALL_BACK;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        CallBack myCallBack = KAFKA_CALL_BACK.get();
    }
}
```
