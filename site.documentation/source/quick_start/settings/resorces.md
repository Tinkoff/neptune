# Освобождение ресурсов

О том где и как применяется читать [тут](../../core/steps/context/stopping.md)

## TO_FREE_RESOURCES_ON_INACTIVITY

Свойство включает/выключает освобождение ресурсов/объектов, занятых [контекстом](../../core/steps/context/index.md), при
простое.

```properties
#Значение свойства указывается так
TO_FREE_RESOURCES_ON_INACTIVITY=true
```

```java
package org.my.pack;

import static ru.tinkoff.qa.neptune.core.api.properties.general
        .resorces.FreeResourcesOnInactivity.TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        boolean toFree = TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.get();
    }
}
```

## Время простоя

Свойства `TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT` и `TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE`
определяют допустимое время простоя ресурсов.

```properties
#Укажем допустимое время простоя
#5 секунд
TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT=SECONDS
TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE=5
```

```java
package org.my.pack;

import java.time.Duration;

import static ru.tinkoff.qa.neptune.core.api.properties.general
        .resorces.FreeResourcesOnInactivityAfter.FREE_RESOURCES_ON_INACTIVITY_AFTER;

public class SomeClass {

    public void someVoid() {
        Duration downTimeDuration = FREE_RESOURCES_ON_INACTIVITY_AFTER.get();
    }
}
```

Время по умолчанию = 30 секунд. Значения свойств применяются, если `TO_FREE_RESOURCES_ON_INACTIVITY=true`