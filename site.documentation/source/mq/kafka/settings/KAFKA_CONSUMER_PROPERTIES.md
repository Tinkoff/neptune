# KAFKA_CONSUMER_PROPERTIES

Настройка предоставляет свойства, которые используются kafka клиентом для
создания инстанса `org.apache.kafka.clients.consumer.KafkaConsumer`.

Примеры properties можно посмотреть [тут](https://kafka.apache.org/documentation/#consumerconfigs)

```java
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

//Создаем поставщика, который будет поставлять объект класса Properties
public class MyConsumerPropertiesSupplier 
    implements ObjectPropertySupplier<Properties, Supplier<Properties>> {

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

import static ru.tinkoff.qa.neptune.kafka.properties
        .DefaultKafkaProperties.KAFKA_CONSUMER_PROPERTIES;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        Properties consumerProperties = KAFKA_CONSUMER_PROPERTIES.get();
    }
}
```

Ниже пример, если модуль используется для тестирования Spring-приложения вместе с
инструментами [Spring Test](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html)

```java
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import static ru.tinkoff.qa.neptune.spring.boot.starter.application
    .contexts.CurrentApplicationContextTestExecutionListener.getCurrentApplicationContext;


//Создаем поставщика, который будет поставлять объект класса Properties
public class MySpringConsumerPropertiesSupplier 
    implements ObjectPropertySupplier<Properties, Supplier<Properties>> {

    @Override
    public Properties get() {
        //текущий контекст приложения
        var context = getCurrentApplicationContext();
        //Далее извлекаем необходимые бины,
        //при помощи которых можно получить свойства для консъюмера.

        //....
        //алгоритм получения бинов от контекста приложения 
        
        //То, что нужно заполнить и вернуть
        var properties = new Properties();
        
        //Логика получения свойств консъюмера и заполнения
        //результирующего объекта properties
        //....
        //какая-то дополнительная логика
        //....
        return properties;
    }
}
```

Полезная ссылка: [Neptune Spring boot starter](./../../../spring/spring.boot.sterter.md)