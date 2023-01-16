# Свойства для аутентификации в RabbitMQ

⚠️ Рекомендация: в целях безопасности значения перечисленных ниже свойств рекомендуется не выкладывать в открытом виде,
а использовать Vault, Consul, переменные CI или локальные переменные окружения.

Полезная ссылка:

- [Свойства Neptune](./../../../quick_start/settings/index.md)
- [Сторонние свойства](./../../../core/settings/property_sources.rst)

## RABBIT_MQ_PASSWORD

Настройка, в которой указывается значение пароля

```properties
#Значение свойства указывается так
RABBIT_MQ_PASSWORD=${SOME_SECRET_PASSWORD_PROPERTY}
```

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

## RABBIT_MQ_USERNAME

Настройка, в которой указывается значение логина

```properties
#Значение свойства указывается так
RABBIT_MQ_USERNAME=${SOME_SECRET_USER_PROPERTY}
```

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