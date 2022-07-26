# Глубина вывода шагов в отчет / консоль

О том где и как применяется читать [тут](../../core/steps/annotations/@MaxDepthOfReporting.md)

Свойство включает/выключает ограничение глубины вывода в консоль/тест-репорт шагов, для которых оно предусмотрено.

```properties
#Значение свойства указывается так
TO_LIMIT_REPORT_DEPTH=true
```

```java
package org.my.pack;

import static ru.tinkoff.qa.neptune.core.api.properties.general.events.ToLimitReportDepth
        .TO_LIMIT_REPORT_DEPTH_PROPERTY;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        Boolean toLimit = TO_LIMIT_REPORT_DEPTH_PROPERTY.get();
    }
}
```

