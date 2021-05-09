# Механизм настроек

## neptune.global.properties и neptune.properties

## Автоматическая генерация neptune.global.properties и neptune.properties

## Свойства для возврата чисел
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Свойства для возврата величины продолжительности
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Свойства для возврата объектов перечислений
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Свойства для создания и возврата объектов URL
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Свойства для создания и возврата объектов
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Свойства для возврата Boolean
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Основной набор настроек/свойств

### Освобождение ресурсов

### Локализация

[Основная статья](./LOCALIZATION.MD)

##### DEFAULT_LOCALE

<details>
    <summary>Нажми, чтобы прочесть</summary>

Определяет языковой стандарт по умолчанию

```properties
#Значение свойства указывается так
DEFAULT_LOCALE=ru_RU
```

```java
import java.util.Locale;

import static ru.tinkoff.qa.neptune.core.api.properties.general.localization
        .DefaultLocaleProperty.DEFAULT_LOCALE_PROPERTY;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        Locale locale = DEFAULT_LOCALE_PROPERTY.get();
    }
}
```

</details>

##### DEFAULT_LOCALIZATION_ENGINE

<details>
    <summary>Нажми, чтобы прочесть</summary>

Определяет какой механизм перевода будет использоваться

```properties
#В значение свойства указывается полное пакетное имя класса реализующее интерфейс
# ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization
DEFAULT_LOCALIZATION_ENGINE=org.my.project.MyLocalizationEngine
```

```java
import ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization;

import static ru.tinkoff.qa.neptune.core.api.properties.general.localization
        .DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        StepLocalization localization = DEFAULT_LOCALIZATION_ENGINE.get();
    }
}
```

</details>

### Логирование шагов

##### TO_LIMIT_REPORT_DEPTH

[Основная статья](STEPS.MD#Вложенность/глубина-шагов)

Определяет, нужно ли ограничить логирование/вывод в отчет о тестировании шагов в зависимости от их глубины. Значение по
умолчанию (рекомендуемое) `true`.

```properties
#Значение свойства указывается так
TO_LIMIT_REPORT_DEPTH=true
```

```java
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.ToLimitReportDepth
        .TO_LIMIT_REPORT_DEPTH_PROPERTY;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        Boolean toLimit = TO_LIMIT_REPORT_DEPTH_PROPERTY.get();
    }
}
```
