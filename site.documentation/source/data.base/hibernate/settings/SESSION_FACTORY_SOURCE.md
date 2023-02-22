# SESSION_FACTORY_SOURCE

Настройка предоставляет объект класса, расширяющего `ru.tinkoff.qa.neptune.hibernate.session.factory.SessionFactorySource`. 
С помощью этого объекта создаются или извлекаются уже созданные объекты `org.hibernate.SessionFactory`.

Значение по умолчанию `ru.tinkoff.qa.neptune.hibernate.session.factory.DefaultSessionFactorySource`. Если другое значение свойства не указано,
то следует указать значения следующих свойств:

- [USE_JPA_CONFIG.md](USE_JPA_CONFIG.md)
- [PERSISTENCE_UNITS.md](PERSISTENCE_UNITS.md)

либо

- [HIBERNATE_CONFIG_FILENAMES.md](HIBERNATE_CONFIG_FILENAMES.md)

Ниже пример того, как указывается значение свойства `SESSION_FACTORY_SOURCE`, отличное от дефолтного

```java
package org.my.pack;

import org.hibernate.SessionFactory;
import java.util.Set;
import ru.tinkoff.qa.neptune.hibernate.session.factory.SessionFactorySource;

public class CustomSessionFactorySource implements SessionFactorySource {

    @Override
    protected Set<SessionFactory> getSetOfSessionFactories() {
        //алгоритм создания 
        // или получения объектов SessionFactory
    }
}
```

```properties
#Значение свойства указывается так
SESSION_FACTORY_SOURCE=org.my.pack.CustomSessionFactorySource;
```

```java

import static ru.tinkoff.qa.neptune.hibernate.properties
    .SessionFactorySourceProperty.SESSION_FACTORY_SOURCE_PROPERTY;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        SessionFactorySource factorySource = SESSION_FACTORY_SOURCE_PROPERTY.get();
    }
}
```

Ниже пример, если модуль используется для тестирования Spring-приложения вместе с инструментами [Spring Test](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html)

```java
package org.my.pack;

import org.hibernate.SessionFactory;
import java.util.Set;
import ru.tinkoff.qa.neptune.hibernate.session.factory.SessionFactorySource;

import static ru.tinkoff.qa.neptune.spring.boot.starter
    .application.contexts.CurrentApplicationContextTestExecutionListener.getCurrentApplicationContext;

public class CustomSessionFactorySource implements SessionFactorySource {

    @Override
    protected Set<SessionFactory> getSetOfSessionFactories() {
        //простой пример, как можно получить нужный набор из проинициализированного 
        //Spring-контекста приложения
        return new HashSet<>(getCurrentApplicationContext()
            .getBeansOfType(SessionFactory.class)
            .values()
        );
    }
}
```

Полезная ссылка: [Neptune Spring boot starter](./../../../spring/spring.boot.sterter.md)