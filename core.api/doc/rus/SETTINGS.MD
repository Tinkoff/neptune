# Механизм настроек

Ниже описан способ настроек переменных окружения/свойств, механизм их чтения, описания. Так же описаны базовые 
свойства/переменные окружения, которые можно задать независимо от того, какие модули Neptune подключены к проекту.

## neptune.global.properties и neptune.properties

Настройка среды / параметров (какой браузер используется для проверки front-end приложения, параметры подключения к базам, 
end-point'ы проверяемых http/rest сервисов, время для ожидания чего-либо) для запуска тестов происходит посредством 
добавления переменных среды и их значений.

Так же возможно выполнять такие настройки посредством `*.properties`-файлов. 

Neptune использует файлы `neptune.properties`, расположенные в директории `main/resources` или `test/resources` 
проекта/модуля для:

- указания значений свойств/переменных окружения Neptune в рамках модуля/проекта
- обеспечения возможности версионирования свойств/переменных окружения Neptune в рамках модуля/проекта
- обеспечения возможности переноса настроек свойств/переменных окружения Neptune в рамках модуля/проекта вместе
  с исходным кодом тестов
- обеспечения возможности указывать значения свойств/переменных окружения для прочих сторонних библиотек, которые
  используются для написания тестов
- обеспечения возможности настраивать переменные среды точечно (об этом подробнее ниже).

Значения переменных среды имеют приоритет над значениями тех же самых переменных, указанных в `neptune.properties`. 
Т.е. если настройка указана как переменная среды с одним значением, и та же самая настройка внесена в
`neptune.properties` с другим значением, при запуске тестов в этой среде будет использовано значение переменной этой 
среды.

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
