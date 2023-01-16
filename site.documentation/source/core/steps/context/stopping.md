# Завершение / Остановка

Некоторые контексты могут занимать ресурсы, которые следует освобождать во время простоя (все тесты в наборе завершились,
какие-либо из тестовых потоков завершились и т.п.).

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

public class MyTestContext extends Context<MyTestContext>
    implements Stoppable { // <---Необходимо реализовать данный интерфейс

    @Override
    public void stop() {
        //логика освобождения ресурсов, занятых объектом контекста,
        //или уничтожения внутренних объектов, инкапсулированных объектом контекста
    }
}
```

Автоматическое завершение / остановка включается при указании [соответствующих настроек](../../../quick_start/settings/resorces.md)