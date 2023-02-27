# USE_JPA_CONFIG

Значение `true` данного свойства означает, что подключения к базам данных конфигурируются с помощью `META-INF/persistence.xml` (спецификация Java Persistence API).
Данное свойство применяется, если значение свойства [SESSION_FACTORY_SOURCE](SESSION_FACTORY_SOURCE.md) равно дефолтному.

```properties
#Значение свойства указывается так
USE_JPA_CONFIG=true
```

```java
package org.my.pack;

import static ru.tinkoff.qa.neptune.hibernate.properties.UseJpaConfig.USE_JPA_CONFIG;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        Boolean useJPA = USE_JPA_CONFIG.get();
    }
}
```