# Настройки RabbitMq модуля для дальнейшей работы

[Подробнее о механизме настроек](./../../../core.api/doc/rus/SETTINGS.MD).

Описанный ниже набор настроек/свойств
дополняет [базовый набор настроек/свойств](./../../../core.api/doc/rus/SETTINGS.MD#Основной-набор-настроексвойств),
которые могут быть включены в `neptune.global.properties` или в `neptune.properties`, или использованы в качестве
свойств/переменных окружения/среды в той среде, в которой запускаются тесты.

## Оглавление

- [RABBIT_AMQP_PROPERTY](#RABBIT_AMQP_PROPERTY)
- [RABBIT_MQ_PASSWORD](#RABBIT_MQ_PASSWORD)
- [RABBIT_MQ_USERNAME](#RABBIT_MQ_USERNAME)
- [RABBIT_MQ_DEFAULT_READ_MAPPER](#RABBIT_MQ_DEFAULT_READ_MAPPER)
- [RABBIT_MQ_CLUSTER](#RABBIT_MQ_CLUSTER)

## RABBIT_AMQP_PROPERTY

Настройка предоставляет объект `AMQP.BasicProperties.Builder`, подготовленный нужным образом, и который используется
rabbitMq клиентом по умолчанию.

```java
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
import java.net.*;

import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RABBIT_AMQP_PROPERTY;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        AMQP.BasicProperties.Builder builder = RABBIT_AMQP_PROPERTY.get();
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## RABBIT_MQ_PASSWORD

Настройка, в которой указывается значение пароля для rabbitMq

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqAuthorizationProperties.RABBIT_MQ_PASSWORD;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        String password = RABBIT_MQ_PASSWORD.get();
    }
}
```

```properties
#Значение свойства указывается так
RABBIT_MQ_PASSWORD=guest
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## RABBIT_MQ_USERNAME

Настройка, в которой указывается значение логина для rabbitMq

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqAuthorizationProperties.RABBIT_MQ_USERNAME;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        String username = RABBIT_MQ_USERNAME.get();
    }
}
```

```properties
#Значение свойства указывается так
RABBIT_MQ_USERNAME=guest
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## RABBIT_MQ_DEFAULT_READ_MAPPER

```java
//Создаем реализацию механизма сериализации и десериализации
public class MyMapper implements RabbitMqMapper {


    @Override
    public <T> T deserialize(String message, Class<T> cls) {
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
RABBIT_MQ_DEFAULT_MAPPER=org.my.pack.MyMapper
```

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqDefaultMapper.RABBIT_MQ_DEFAULT_MAPPER;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        RabbitMqMapper mapper = RABBIT_MQ_DEFAULT_MAPPER.get();
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## RABBIT_MQ_CLUSTER

Настройка позволяет указать список конечных точек, которые будут использоваться при подключении. Будет использоваться
первая достижимая конечная точка. В случае сбоев подключения использование списка конечных точек позволяет приложению
подключиться к другому узлу, если исходный узел не работает.

```java
import java.net.*;

import static ru.tinkoff.qa.neptune.rabbit.mq.properties.RabbitMqClusterProperty.RABBIT_MQ_CLUSTER_PROPERTY;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        Addreses[] addresses = RABBIT_MQ_CLUSTER_PROPERTY.get();
    }
}
```

```properties
#Значение свойства указывается так
RABBIT_MQ_CLUSTER_PROPERTY=host:port,host:port
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
