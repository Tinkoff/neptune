# RABBIT_AMQP_PROPERTY

Настройка предоставляет объект `AMQP.BasicProperties.Builder`, подготовленный нужным образом, и который используется
rabbitMq клиентом по умолчанию.

```java
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

//Создаем поставщика, который будет поставлять объекты класса AMQP.BasicProperties.Builder
public class MyAMQPSupplier implements ObjectPropertySupplier<AMQP.BasicProperties.Builder, Supplier<AMQP.BasicProperties.Builder>> {

    @Override
    public AMQP.BasicProperties.Builder get() {
        //Тут создается объект, который будет использован клиентом
    }
}
```

```properties
#Значение свойства указывается так
RABBIT_AMQP_PROPERTY=org.my.pack.MyAMQPSupplier
```

```java
import com.rabbitmq.client.AMQP.BasicProperties.Builder;

import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqAMQPProperty.RABBIT_AMQP_PROPERTY;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        AMQP.BasicProperties.Builder builder = RABBIT_AMQP_PROPERTY.get();
    }
}
```

Ниже пример, если модуль используется для тестирования Spring-приложения вместе с инструментами [Spring Test](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html)

```java
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import static ru.tinkoff.qa.neptune.spring.boot.starter.application
    .contexts.CurrentApplicationContextTestExecutionListener.getCurrentApplicationContext;

//Создаем поставщика, который будет поставлять объекты класса AMQP.BasicProperties.Builder
public class MyAMQPSupplier implements ObjectPropertySupplier<AMQP.BasicProperties.Builder, Supplier<AMQP.BasicProperties.Builder>> {

    @Override
    public AMQP.BasicProperties.Builder get() {
        //текущий контекст приложения
        var context = getCurrentApplicationContext();
        //Далее извлекаем необходимые бины,
        //при помощи которых можно получить свойства

        //....
        //алгоритм получения бинов от контекста приложения 

        //То, что нужно заполнить и вернуть
        var builder = new AMQP.BasicProperties.Builder();
        //Логика получения свойств и заполнения
        //результирующего объекта
        //....
        //какая-то дополнительная логика
        //....
        return builder;
    }
}
```

Полезная ссылка: [Neptune Spring boot starter](./../../../spring/spring.boot.sterter.md)