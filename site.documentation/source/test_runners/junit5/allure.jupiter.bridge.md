# allure.jupiter.bridge

Модуль объединяет интеграции с [JUnit5](./index.md) и с [Allure](./../../test_reports/allure/index.md)
в одну работающую систему


```{eval-rst}
.. include:: junit5_allure_dependencies.rst
```

[API](https://tinkoff.github.io/neptune/allure.jupiter.bridge/index.html)

## allure.jupiter.bridge. Свойства

- [JUnit 5. Свойства](settings.md)
- [Allure. Свойства](./../../test_reports/allure/settings.md)

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

## Зависимости от JUnit 5

См. [Таблицу соответствий версий Neptune и JUnit5](./supported_versions.md)

