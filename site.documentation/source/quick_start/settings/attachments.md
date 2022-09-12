# Автоматическое создание аттачей

О том где и как применяется читать [тут](../../core/events/index.md)

Свойство управляет автоматическим созданием аттачей для тест-репорта. Объекты перечисления
`ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents`. Доступные значения:

- `SUCCESS` - если шаг завершился успешно
- `FAILURE` - если выполнившийся шаг выбросил исключение
- `SUCCESS_AND_FAILURE` - во всех случаях

```properties
#Значение свойства указывается так
DO_CAPTURES_OF=SUCCESS_AND_FAILURE
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents;

import static ru.tinkoff.qa.neptune.core.api.properties.general
        .events.DoCapturesOf.DO_CAPTURES_OF_INSTANCE;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        CapturedEvents value = DO_CAPTURES_OF_INSTANCE.get();
    }
}
```