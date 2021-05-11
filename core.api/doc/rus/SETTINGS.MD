# Механизм настроек

Ниже описан способ настроек переменных среды / окружения / свойств, механизм их чтения, описания. Так же описаны базовые 
свойства / переменные окружения, которые можно задать независимо от того, какие модули Neptune подключены к проекту.

## `neptune.global.properties` и `neptune.properties`

Настройка среды / параметров (какой браузер используется для проверки front-end приложения, параметры подключения к базам, 
end-point'ы проверяемых http/rest сервисов, время для ожидания чего-либо) для запуска тестов происходит посредством 
добавления переменных среды и их значений.

Так же можно выполнять такие настройки посредством `*.properties`-файлов. 

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

Может быть так, что проект состоит из нескольких модулей, один из которых — кастомизация Neptune для нужд команды. Так же
может быть, что кастомизация Neptune для нужд команды используется как внешняя зависимость (dependency). Может быть так, 
что все зависимые модули используют примерно один и тот набор свойств / переменных окружения с одними и теми же 
значениями. В этом случае можно использовать файлы `neptune.global.properties`. Данный файл должен быть помещен в 
`resources` модуля (или зависимости), который используется тестами других модулей приложения / системы. В этом файле 
указывается общий набор свойств / переменных окружения и их значений. Это позволяет:

- настраивать свойства / переменные окружения в файлах `neptune.properties` зависимых проектов / модулей точечно, т.е
  добавлять то, что не было указано в `neptune.global.properties` или перекрывать некоторые значения свойств/переменных
  окружения, уже указанных в `neptune.global.properties` (если для тестов данного модуля должны быть использованы другие
  значения).
  
Значения свойств / переменных окружения, указанных в `neptune.properties`, имеют приоритет над значениями тех же самых 
переменных, указанных в `neptune.global.properties`. Т.е. если настройка указана в `neptune.global.properties` с одним 
значением, и та же самая настройка внесена в `neptune.properties` с другим значением, при запуске тестов будет 
использовано значение, указанное в `neptune.properties`.

Значения переменных среды имеют приоритет над значениями тех же свойств, указанных в `neptune.global.properties` и
`neptune.properties`.

## Описание свойства / переменной среды

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

//Перечисленные ниже аннотации используются для описания поведения свойства / переменной окружения
// и при генерации файлов  neptune.properties и neptune.global.properties (будет описано ниже).
@PropertyName("MY_PROPERTY") //Название свойства / переменной окружения. Рекомендуется давать названия
// как в примере, чтобы можно было использовать в качестве переменной среды.
//Аннотация обязательная!!!!!
@PropertyDefaultValue("Some default value") //значение свойства по умолчанию.
// Будет возвращаться значение, прочитанное из этой строки и преобразованное в
// значение типа T при соблюдении следующих условий:
// - значение этого свойства / переменной окружения не указано в neptune.properties
// - значение этого свойства / переменной окружения не указано в neptune.global.properties
// - Результаты System.getenv() и System.getProperties() не содержат ключа, соответствующего
// названию, указанному аннотацией @PropertyName (см. выше).
// Если аннотация присутствует и файлы neptune.properties / neptune.global.properties
// генерируются автоматически, то в файл данная переменная окружения / свойство вносится с уже заполненным
// значением (дефолтным). Аннотация не обязательная.
//_____________________________________________________
//Аннотация ниже формирует описание свойства / переменной окружения окружения.
//Она нужна для того, чтобы у свойства / переменной окружения был сопроводительный комментарий-описание
// после автоматической генерации файлов neptune.properties или neptune.global.properties.
// Аннотация не обязательная (но рекомендуется).
@PropertyDescription(section = "Some logical section", //Название раздела, к которому можно было бы отнести свойство
        // или переменную окружения. После автоматической генерации файлов neptune.properties или
        // neptune.global.properties все свойства будут сгруппированы по "разделам"
        description = {"Description string 1", //описание, что за свойство / переменная окружения, зачем нужно
                "Description string 2", //Так можно разбивать описание
                "Description string 3"}) //на строки, если оно длинное
public class MyProperty implements
        PropertySupplier< //нужно реализовать этот интерфейс
                T, //тип объекта, в который преобразуется строка, прочитанная из значения свойства
                // или переменной окружения
                R> { // тип объекта, который может быть преобразован в строку-значение свойства
                // или переменной окружения. Может совпадать с типом выше.

    // Не обязательно так делать. Просто для быстрого доступа к значению свойства
    // или переменной окружения
    public static final MyProperty PROPERTY = new MyProperty();

    @Override //тут можно описать алгоритм, как преобразовать переданный объект в строку-значение свойства
    // или переменной окружения.
    public String readValuesToSet(R value) {
        return PropertySupplier.super.readValuesToSet(value);
        //или своя реализация
    }

    @Override //тут нужно описать алгоритм, как преобразовать строку-значение свойства
    // или переменной окружения в объект
    public T parse(String value) {
        return //алгоритм преобразования;
    }

    @Override //Тут можно описать, какое значение вернуть вместо null.
    public T returnIfNull() {
        return PropertySupplier.super.returnIfNull();
        //или своя реализация
    }
}

```

Вариант использования

```java
package org.my.pack;

import static org.my.pack.MyProperty.PROPERTY;

public class MyUseCase {

    public void useCase() {
        //чтение
        T value = PROPERTY.get();

        R newValue = //инициализация
                
        //Так можно менять значение
        // так делать не рекомендуется, просто демонстрация возможностей               
        PROPERTY.accept(newValue);

        //Обнуление свойства / переменной окружения
        //так делать не рекомендуется, просто демонстрация возможностей
        PROPERTY.accept(null);
    }
}

```

Так же можно группировать похожие по смыслу или функциям описания свойств / переменных окружения в перечисления.

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

public enum MyPropertyEnum implements PropertySupplier<T, R> {
    @PropertyName("MY_PROPERTY_1")
    @PropertyDefaultValue("Some default value 1")
    @PropertyDescription(section = "Some logical section",
            description = {"Description string 1",
                    "Description string 2",
                    "Description string 3"}
    )        
    PROPERTY_1,

    @PropertyName("MY_PROPERTY_2")
    @PropertyDefaultValue("Some default value 2")
    @PropertyDescription(section = "Some logical section",
            description = {"Description string 4",
                    "Description string 5",
                    "Description string 6"}
    )
    PROPERTY_2;

    @Override //при необходимости
    public String readValuesToSet(R value) {
        return PropertySupplier.super.readValuesToSet(value);
    }

    @Override
    public T parse(String value) {
        return //алгоритм преобразования;
    }

    @Override //при необходимости
    public T returnIfNull() {
        return PropertySupplier.super.returnIfNull();
    }
}
```

Ниже примеры того, как можно быстро описать свойства / переменные окружения, значения которых могут быть преобразованы 
в объекты различных типов. 

### Свойства для возврата чисел
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

### Свойства для возврата Boolean
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

### Свойства для возврата величины продолжительности
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

### Свойства для возврата объектов перечислений
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

### Свойства для создания и возврата объектов URL
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

### Свойства для создания и возврата объектов
<details>
  <summary>Нажми, чтобы прочесть</summary>

</details>

## Автоматическая генерация `neptune.global.properties` и `neptune.properties`

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
