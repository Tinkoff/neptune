# Язык / локализация

О том где и как применяется читать [тут](../../core/internationalization.md)

Перечисленные ниже свойства управляют тем, на каком языке выводятся в репорт/консоль шаги, и с помощью чего 
происходит локализация / интернационализация выводимой информации.

## DEFAULT_LOCALE

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

## DEFAULT_LOCALIZATION_ENGINE

Определяет какой механизм перевода будет использоваться

```properties
#В значение свойства указывается полное пакетное имя класса реализующее интерфейс
# ru.tinkoff.qa.neptune.core.api.localization.StepLocalization
#По дефолту, в примере показано рекомендуемое значение свойства
DEFAULT_LOCALIZATION_ENGINE=ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle
```

```java
import ru.tinkoff.qa.neptune.core.api.localization.StepLocalization;

import static ru.tinkoff.qa.neptune.core.api.properties.general.localization
        .DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;

public class SomeClass {

    public void someVoid() {
        //пример доступа до значения свойства
        StepLocalization localization = DEFAULT_LOCALIZATION_ENGINE.get();
    }
}
```

