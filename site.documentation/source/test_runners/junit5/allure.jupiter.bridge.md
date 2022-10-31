# allure.jupiter.bridge

Модуль объединяет интеграции с [JUnit5](./index.md) и с [Allure](./../../test_reports/allure/index.md)
в одну работающую систему


```{eval-rst}
.. include:: junit5_allure_dependencies.rst
```

## allure.jupiter.bridge. Свойства

- [JUnit 5. Свойства](settings.md)
- [Allure. Свойства](./../../test_reports/allure/settings.md)

Рекомендуется включить свойство [junit.jupiter.extensions.autodetection.enabled](https://junit.org/junit5/docs/current/user-guide/#extensions-registration-automatic-enabling)

```properties
junit.jupiter.extensions.autodetection.enabled=true
```

## allure.jupiter.bridge. Аннотации

- [Allure. Аннотации](./../../test_reports/allure/annotations.md)

Если выключено свойство `junit.jupiter.extensions.autodetection.enabled`, тогда можно (пример)

```java
package org.my.pack;

import org.junit.jupiter.api.extension.ExtendWith;
import ru.tinkoff.qa.neptune.allure.ExcludeFromAllureReport;
import ru.tinkoff.qa.neptune.allure.jupiter.bridge.ExcludeFromAllureReportExtension;


@ExtendWith(ExcludeFromAllureReportExtension.class)
@ExcludeFromAllureReport
public class SomeTest {
    
}
```

