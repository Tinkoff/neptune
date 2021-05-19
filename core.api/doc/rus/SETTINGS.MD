# Механизм настроек

Ниже описан способ настроек переменных среды / окружения / свойств, механизм их чтения, описания. Так же описаны базовые 
свойства / переменные окружения, которые можно задать независимо от того, какие модули Neptune подключены к проекту.

## Оглавление
  - [`neptune.global.properties` и `neptune.properties`](#neptuneglobalproperties-и-neptuneproperties)
  - [Описание свойства / переменной среды](#Описание-свойства--переменной-среды)
    - [Свойства для возврата строки](#Свойства-для-возврата-строки)
    - [Свойства для возврата Boolean](#Свойства-для-возврата-Boolean)
    - [Свойства для возврата чисел](#Свойства-для-возврата-чисел)
      - [Byte](#byte)
      - [Double](#double)
      - [Float](#float)
      - [Integer](#integer)
      - [Long](#long)
      - [Short](#short)
    - [Свойства для создания и возврата объектов URL](#Свойства-для-создания-и-возврата-объектов-url)
    - [Свойства для возврата объектов перечислений](#Свойства-для-возврата-объектов-перечислений)
      - [Возврат одиночного объекта перечисления](#Возврат-одиночного-объекта-перечисления)
      - [Возврат множества объектов перечисления](#Возврат-множества-объектов-перечисления)
    - [Свойства для возврата величины продолжительности](#Свойства-для-возврата-величины-продолжительности)
    - [Свойства для создания и возврата объектов](#Свойства-для-создания-и-возврата-объектов)
      - [Создание и возврат одного объекта](#Создание-и-возврат-одного-объекта)
      - [Создание и возврат коллекции объектов](#Создание-и-возврат-коллекции-объектов)
  - [Автоматическая генерация `neptune.global.properties` и `neptune.properties`](#Автоматическая-генерация-neptuneglobalproperties-и-neptuneproperties)
    - [Maven](#maven)
    - [Gradle](#gradle)
    - [Результат](#Результат)
  - [Основной набор настроек/свойств](#Основной-набор-настроексвойств)
    - [Освобождение ресурсов](#Освобождение-ресурсов)
      - [TO_FREE_RESOURCES_ON_INACTIVITY](#to_free_resources_on_inactivity)
      - [Время](#Время)
    - [Локализация](#Локализация)
      - [DEFAULT_LOCALE](#default_locale)
      - [DEFAULT_LOCALIZATION_ENGINE](#default_localization_engine)
    - [Логирование шагов](#Логирование-шагов)
      - [DO_CAPTURES_OF](#do_captures_of)
      - [TO_LIMIT_REPORT_DEPTH](#to_limit_report_depth)

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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Свойства для возврата строки

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.string.StringValuePropertySupplier;


@PropertyName("MY_PROPERTY")
public class MyStringProperty implements StringValuePropertySupplier {

  public static final MyStringProperty PROPERTY = new MyStringProperty();

}
```

Варианты использования:

- переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
MY_PROPERTY=Some string
```

- java-код

```java
package org.my.pack;


import static org.my.pack.MyStringProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    String value = PROPERTY.get();
  }
}

```

</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Свойства для возврата Boolean

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.booleans.BooleanValuePropertySupplier;


@PropertyName("MY_PROPERTY")
public class MyBooleanProperty implements BooleanValuePropertySupplier {

    public static final MyBooleanProperty PROPERTY = new MyBooleanProperty();

}
```

Варианты использования:

- переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
MY_PROPERTY=true
```

- java-код

```java
package org.my.pack;


import static org.my.pack.MyBooleanProperty.PROPERTY;

public class MyUseCase {

    public void useCase() {
        Boolean value = PROPERTY.get();
    }
}
```
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Свойства для возврата чисел

#### Byte

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.bytes.ByteValuePropertySupplier;


@PropertyName("MY_PROPERTY")
public class MyByteProperty implements ByteValuePropertySupplier {

    public static final MyByteProperty PROPERTY = new MyByteProperty();

}
```

Варианты использования:

- переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
MY_PROPERTY=2
```

- java-код

```java
package org.my.pack;

import static org.my.pack.MyByteProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    Byte value = PROPERTY.get();
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

#### Double

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.doubles.DoubleValuePropertySupplier;


@PropertyName("MY_PROPERTY")
public class MyDoubleProperty implements DoubleValuePropertySupplier {

  public static final MyDoubleProperty PROPERTY = new MyDoubleProperty();

}
```

Варианты использования:

- переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
MY_PROPERTY=2.5
```

- java-код

```java
package org.my.pack;

import static org.my.pack.MyDoubleProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    Double value = PROPERTY.get();
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

#### Float

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.floats.FloatValuePropertySupplier;


@PropertyName("MY_PROPERTY")
public class MyFloatProperty implements FloatValuePropertySupplier {

  public static final MyFloatProperty PROPERTY = new MyFloatProperty();

}
```

Варианты использования:

- переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
MY_PROPERTY=2.5
```

- java-код

```java
package org.my.pack;


import static org.my.pack.MyFloatProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    Float value = PROPERTY.get();
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

#### Integer

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.integers.IntValuePropertySupplier;


@PropertyName("MY_PROPERTY")
public class MyIntegerProperty implements IntValuePropertySupplier {

  public static final MyIntegerProperty PROPERTY = new MyIntegerProperty();

}

```

Варианты использования:

- переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
MY_PROPERTY=2
```

- java-код

```java
package org.my.pack;


import static org.my.pack.MyIntegerProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    Integer value = PROPERTY.get();
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

#### Long

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;


@PropertyName("MY_PROPERTY")
public class MyLongProperty implements LongValuePropertySupplier {

  public static final MyLongProperty PROPERTY = new MyLongProperty();

}
```

Варианты использования:

- переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
MY_PROPERTY=2
```

- java-код

```java
package org.my.pack;


import static org.my.pack.MyLongProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    Long value = PROPERTY.get();
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

#### Short

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.shorts.ShortValuePropertySupplier;

@PropertyName("MY_PROPERTY")
public class MyShortProperty implements ShortValuePropertySupplier {

  public static final MyShortProperty PROPERTY = new MyShortProperty();

}
```

Варианты использования:

- переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
MY_PROPERTY=2
```

- java-код

```java
package org.my.pack;


import static org.my.pack.MyShortProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    Short value = PROPERTY.get();
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Свойства для создания и возврата объектов URL

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

@PropertyName("MY_PROPERTY")
public class MyURLProperty implements URLValuePropertySupplier {

  public static final MyURLProperty PROPERTY = new MyURLProperty();
}
```

Варианты использования:

- переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
MY_PROPERTY=https://www.google.com
```

- java-код

```java
package org.my.pack;

import java.net.URL;

import static org.my.pack.MyURLProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    URL value = PROPERTY.get();
  }
}
```
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Свойства для возврата объектов перечислений

Предположим, у нас есть перечисление

```java
package org.my.pack;

public enum MyEnum {
    
    ITEM1,
    ITEM2,
    ITEM3
}

```

#### Возврат одиночного объекта перечисления

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

@PropertyName("MY_PROPERTY")
public class MyEnumItemProperty implements
        EnumPropertySuppler<MyEnum> { //<-Указывается тип перечисления  

  public static final MyEnumItemProperty PROPERTY = new MyEnumItemProperty();
}
```

Варианты использования:

- переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
# Значение должно полностью соответствовать имени одного из элементов
# перечисления
MY_PROPERTY=ITEM1
```

- java-код

```java
package org.my.pack;

import static org.my.pack.MyEnumItemProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    MyEnum value = PROPERTY.get();
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

#### Возврат множества объектов перечисления

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.enums.MultipleEnumPropertySuppler;

@PropertyName("MY_PROPERTY")
public class MyEnumItemsProperty implements
        MultipleEnumPropertySuppler<MyEnum> { //<-Указывается тип перечисления

  public static final MyEnumItemsProperty PROPERTY = new MyEnumItemsProperty();
}
```

Варианты использования:

- переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
# Значение должно содержать одно или несколько имен элементов перечисления
MY_PROPERTY=ITEM1,ITEM3
```

- java-код

```java
package org.my.pack;

import java.util.List;

import static org.my.pack.MyEnumItemsProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    List<MyEnum> value = PROPERTY.get();
  }
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Свойства для возврата величины продолжительности

<details>
  <summary>Нажми, чтобы прочесть</summary>

- Нужно создать описание свойства / переменной окружения, которое конвертирует значение в элемент перечисления в
  элемент `java.time.temporal.ChronoUnit`. См [Возврат одиночного объекта перечисления](#Возврат-одиночного-объекта-перечисления).
  
```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.enums.EnumPropertySuppler;

import java.time.temporal.ChronoUnit;

@PropertyName("MY_PROPERTY_CHRONO_UNIT")
public class MyChronoUnitProperty implements EnumPropertySuppler<ChronoUnit> {

  public static final MyChronoUnitProperty MY_CHRONO_UNIT_PROPERTY = new MyChronoUnitProperty();
}
```

- Нужно создать описание свойства / переменной окружения, которое конвертирует значение в элемент перечисления в
  `Long`. См [Свойства для возврата чисел. Long](#Long).
  
```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.longs.LongValuePropertySupplier;

@PropertyName("MY_PROPERTY_CHRONO_VALUE")
public class MyChronoValueProperty implements LongValuePropertySupplier {

  public static final MyChronoValueProperty MY_CHRONO_VALUE_PROPERTY = new MyChronoValueProperty();
}
```

- далее, нужно создать класс, который превращает оба значения в `java.time.Duration`

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.duration.DurationSupplier;

import static org.my.pack.MyChronoUnitProperty.MY_CHRONO_UNIT_PROPERTY;
import static org.my.pack.MyChronoValueProperty.MY_CHRONO_VALUE_PROPERTY;

public class MyDurationSupplier extends 
        DurationSupplier { //<--наследоваться от этого класса
    
    //просто, для быстрого доступа
    public static DurationSupplier MY_DURATION = new MyDurationSupplier();

    protected MyDurationSupplier() {
        super(MY_CHRONO_UNIT_PROPERTY, MY_CHRONO_VALUE_PROPERTY);
    }
}
```

Варианты использования:

- переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
MY_PROPERTY_CHRONO_UNIT=SECONDS
MY_PROPERTY_CHRONO_VALUE=5
```

- java-код

```java
package org.my.pack;

import java.time.Duration;

import static org.my.pack.MyDurationSupplier.MY_DURATION;

public class MyUseCase {

  public void useCase() {
    //чтение
    Duration value = MY_DURATION.get(); //вернет 5 сек
  }
}
```

</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Свойства для создания и возврата объектов

Предположим, существует следующий класс объекта

```java
package org.my.pack;

public class MyObject {
    
    private Object a;

    private Object b;

    private Object c;

    public Object getA() {
        return a;
    }

    public MyObject setA(Object a) {
        this.a = a;
        return this;
    }

    public Object getB() {
        return b;
    }

    public MyObject setB(Object b) {
        this.b = b;
        return this;
    }

    public Object getC() {
        return c;
    }

    public MyObject setC(Object c) {
        this.c = c;
        return this;
    }
}
```

Предположи, что у `MyObject` есть наследник

```java
package org.my.pack;

public class MyExtendedObject extends MyObject{
    
    private Object d;

    public Object getD() {
        return d;
    }

    public MyExtendedObject setD(Object d) {
        this.d = d;
        return this;
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

#### Создание и возврат одного объекта

<details>
  <summary>Нажми, чтобы прочесть</summary>

Описание свойства / переменной окружения должно возвращать созданный объект заданного класса или наследников этого 
класса.

Возможны 2 ситуации:

- конечный пользователь имеет возможность указать класс объекта, базовый или расширяющий.
- конечный пользователь имеет возможность указать, каким должен быть объект (как его заполнить, какие параметры 
  указать)

Примеры:

_- Конечный пользователь имеет возможность указать класс объекта, базовый или расширяющий:_

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;

@PropertyName("MY_PROPERTY")
public class MyObjectProperty implements
        ObjectByClassPropertySupplier<MyObject> { // <- В алмазных скобках указывается базовый тип 
    // Это может быть и интерфейс, и абстрактный класс

    public static final MyObjectProperty PROPERTY = new MyObjectProperty();
}
```

Переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
# Указывается полное пакетное имя нужного класса
# ВАЖНО!!!!!: 
# - указанное значение НЕ должно быть интерфейсом, и НЕ должно быть абстрактным классом
# - У указанного класса не должно быть объявленных конструкторов, или среди объявленных конструкторов 
# должен присутствовать доступный конструктор без параметров
MY_PROPERTY=org.my.pack.MyExtendedObject
```

Java-код

```java
package org.my.pack;

import static org.my.pack.MyObjectProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    MyObject value = PROPERTY.get(); //вернется созданный
    //объект. Но его поля не будут заполнены.
    //Такой способ подойдет, если параметры объекта заполняются
    // неявно в момент создания этого объекта, либо параметры
    //вернувшегося объекта никакой роли не играют в дальнейшей
    //логике работы
  }
}
```

_- Конечный пользователь имеет возможность указать, каким должен быть объект (как его заполнить, какие параметры
  указать):_

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.function.Supplier;

@PropertyName("MY_PROPERTY")
public class MyObjectProperty implements
        ObjectPropertySupplier<MyObject, Supplier<MyObject>> { //в алмазных скобках 2 параметра:
  // - базовый тип объекта, который должно возвращать отображение свойства / переменной окружения. 
  // Это может быть и интерфейс, и абстрактный класс.
  //
  // - тип объекта, с помощью которого происходит создание и настройка результирующего объекта.
  // Это тип должен быть или расширять Supplier<MyObject>.

  public static final MyObjectProperty PROPERTY = new MyObjectProperty();
}
```

```java
package org.my.pack;

import java.util.function.Supplier;

public class MyObjectSupplier implements Supplier<MyObject> {

    @Override
    public MyObject get() {
        return new MyObject() //Если бы тут был непустой конструктор,
                // им можно быо бы воспользоваться.
                .setA(1) //так выполняется настройка / заполнение результирующего
                .setB("A") //объекта.
                .setC(true);
    }
}
```

Переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
# Указывается полное пакетное имя нужного класса
# ВАЖНО!!!!!: 
# - указанное значение НЕ должно быть интерфейсом, и НЕ должно быть абстрактным классом
# - У указанного класса не должно быть объявленных конструкторов, или среди объявленных конструкторов 
# должен присутствовать доступный конструктор без параметров
MY_PROPERTY=org.my.pack.MyObjectSupplier
```

Java-код

```java
package org.my.pack;

import static org.my.pack.MyObjectProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    MyObject value = PROPERTY.get(); //вернется созданный
    //объект, чьи поля будут заполнены.
    //Такой способ подойдет, если класс объекта не имеет доступного
    // конструктора без параметров, и если параметры результирующего
    // объекта имеют значение для дальнейшей работы
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

#### Создание и возврат коллекции объектов

<details>
  <summary>Нажми, чтобы прочесть</summary>

Можно воспользоваться способом, описанным в [Создание и возврат одного объекта](#Создание-и-возврат-одного-объекта), если
рассматривать результирующую коллекцию как объект.

Ниже альтернативный пример.

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.MultipleObjectPropertySupplier;

import java.util.function.Supplier;

@PropertyName("MY_PROPERTY")
public class MyObjectsProperty implements
        MultipleObjectPropertySupplier<MyObject, Supplier<MyObject>> { //в алмазных скобках 2 параметра:
  // - базовый тип объектов, из которых состоит List<>, который должно возвращать
  // отображение свойства / переменной окружения. Это может быть и интерфейс, и абстрактный класс.
  //
  // - тип объекта, с помощью которого происходит создание и настройка каждого элемента результирующего List<>.
  // Это тип должен быть или расширять Supplier<MyObject>.

  public static final MyObjectsProperty PROPERTY = new MyObjectsProperty();
}
```

```java
package org.my.pack;

import java.util.function.Supplier;

public class MyObjectSupplier implements Supplier<MyObject> {

  @Override
  public MyObject get() {
    return new MyObject() //Если бы тут был непустой конструктор,
            // им можно быо бы воспользоваться.
            .setA(1) //так выполняется настройка / заполнение результирующего
            .setB("A") //объекта.
            .setC(true);
  }
}
```

```java
package org.my.pack;

import java.util.Date;
import java.util.function.Supplier;

public class MyExtendedObjectSupplier implements Supplier<MyObject> {
  @Override
  public MyObject get() {
    return new MyExtendedObject() //Если бы тут был непустой конструктор,
            // им можно быо бы воспользоваться.
            .setD(new Date())
            .setA(2) //так выполняется настройка / заполнение результирующего
            .setB("B") //объекта.
            .setC(false);
  }
}

```

Переменная среды или свойство `neptune.properties` / `neptune.global.properties`

```properties
# Указываются полные пакетные имена нужных классов
# ВАЖНО!!!!!: 
# - указанные значения НЕ должны быть интерфейсами, и НЕ должны быть абстрактными классами
# - У указанных классов не должно быть объявленных конструкторов, или среди объявленных конструкторов 
# должны присутствовать доступные конструкторы без параметров
MY_PROPERTY=org.my.pack.MyObjectSupplier,org.my.pack.MyExtendedObjectSupplier
```

Java-код

```java
package org.my.pack;

import java.util.List;

import static org.my.pack.MyObjectsProperty.PROPERTY;

public class MyUseCase {

  public void useCase() {
    //чтение
    List<MyObject> value = PROPERTY.get(); //вернется созданный
    //List<> из 2-х элементов
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Автоматическая генерация `neptune.global.properties` и `neptune.properties`

Свойств / переменных окружения может быть много и заполнять файлы `neptune.global.properties` и `neptune.properties` вручную
может быть долго и трудоемко. На основе аннотаций (см. [Описание свойства / переменной среды](#описание-свойства--переменной-среды)) 
можно выполнить автоматическую генерацию файлов. 

### Maven

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>exec-maven-plugin</artifactId>
      <version>3.0.0</version>
      <executions>
        <execution>
          <id>generate properties</id>
          <phase>compile</phase>
          <goals>
            <goal>java</goal>
          </goals>
          <configuration>
            <mainClass>ru.tinkoff.qa.neptune.core.api.properties.NeptunePropertyGenerator</mainClass>
            <arguments>
              <!--генерировать neptune.properties (false), или neptune.global.properties (true)-->
              <!--neptune.global.properties рекомендуется, если создается общий модуль-кастомизация Neptune
              для тестов других модулей системы/приложения-->
              <argument>false</argument>
              <!--наследовать свойства/переменные окружения из neptune.global.properties (true) или нет (false)-->
              <!--Если выше указано `true` - значение параметра игнорируется. Иначе: 
              - false - создастся файл , повторяющий neptune.global.properties
              - true - создастся файл, в котором "унаследованные" свойства/переменные окружения будут закомментированы
              Если никакого общего модуля-кастомизации, в котором дефолтные значения заданы через neptune.global.properties,
              не используется, то создастся neptune.properties с дефолтными значениями (согласно наличию аннотации 
              @PropertyDefaultValue)-->
              <argument>false</argument>
              <!--Где расположить сгенерированный файл-->
              <argument>${basedir}/src/test/resources</argument>  
              <!--или ${basedir}/src/main/resources-->
              <!--или любая другая директория-->
            </arguments>
          </configuration>
        </execution>
      </executions>
    </plugin>    
  </plugins>
</build>
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Gradle

```groovy
task generateProperties(type: JavaExec) {
    classpath sourceSets.test.runtimeClasspath //или sourceSets.main.runtimeClasspath
    main = "ru.tinkoff.qa.neptune.core.api.properties.NeptunePropertyGenerator"
    //генерировать neptune.properties (false), или neptune.global.properties (true).
    //neptune.global.properties рекомендуется, если создается общий модуль-кастомизация Neptune
    //для тестов других модулей системы/приложения
    args false,
            //наследовать свойства/переменные окружения из neptune.global.properties (true) или нет (false).
            //Если выше указано `true` - значение параметра игнорируется. Иначе:
            //- false - создастся файл , повторяющий neptune.global.properties
            //- true - создастся файл, в котором "унаследованные" свойства/переменные окружения будут закомментированы
            //Если никакого общего модуля-кастомизации, в котором дефолтные значения заданы через neptune.global.properties,
            //не используется, то создастся neptune.properties с дефолтными значениями (согласно наличию аннотации
            //@PropertyDefaultValue)
            false,  
            //Где расположить сгенерированный файл.
            "$projectDir/src/test/resources"
            //или $projectDir/src/main/resources
            //или любая другая директория
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Результат 

Получится файл, как в примере ниже

```properties
#Neptune properties and some properties of JDK or 3rd party frameworks are defined there
#===========================General properties. Report
#To limit report/log output or not by depth of step performing
#It is about steps which are built by `ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier`
#and `ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier`
TO_LIMIT_REPORT_DEPTH=true
#===========================General properties. Report captures/Attachments
#Defines events to make log attachments
#Available values: SUCCESS, FAILURE, SUCCESS_AND_FAILURE
DO_CAPTURES_OF=
#===========================General properties. Resource management
#Is it necessary to free unused resources, e.g. opened browser, connections to databases etc.
TO_FREE_RESOURCES_ON_INACTIVITY=
#Time unit (see java.time.temporal.ChronoUnit) of time to wait for activity
#It have sense when value of the property 'TO_FREE_RESOURCES_ON_INACTIVITY' is 'true'
TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT=SECONDS
#Value of time to wait for activity
#It have sense when value of the property 'TO_FREE_RESOURCES_ON_INACTIVITY' is 'true'
TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE=30
#===========================Report localization
#Defines default locale
DEFAULT_LOCALE=
#Define localisation mechanism
DEFAULT_LOCALIZATION_ENGINE=
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Основной набор настроек/свойств

Эти свойства / переменные окружения могут быть настроены всегда, независимо от набора подключенных модулей Neptune.
Свойства / переменные окружения других модулей Neptune:

- [Neptune. Http](./../../../http.api/doc/rus/SETTINGS.MD)
- [Neptune. Selenium WebDriver](./../../../selenium/doc/rus/SETTINGS.MD)
- [Neptune. TestNG](./../../../testng.integration/doc/rus/README.MD#Настройки)
- JUnit5 (будут предоставлены после реализации*)

### Освобождение ресурсов

См. [Сброс/Остановка](./REFRESHING_STOPPING.MD#сброс--остановка)

##### TO_FREE_RESOURCES_ON_INACTIVITY

<details>
    <summary>Нажми, чтобы прочесть</summary>

Настройка указывает на необходимость освобождать ресурсы (подключения к базам данных, открытые браузеры и т.д) при простое. 
Доступные значения - `true` (рекомендуемое для прогона тестов в несколько потоков) или `false`.

Пример настройки:

```properties
#Значение свойства указывается так
TO_FREE_RESOURCES_ON_INACTIVITY=true
```

```java
import static ru.tinkoff.qa.neptune.core.api.properties
        .general.resorces.FreeResourcesOnInactivity.TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY;

public class SomeClass {
    
    public void someVoid() {
        //пример доступа до значения свойства
        boolean toFree = TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY.get();
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

##### Время 

<details>
    <summary>Нажми, чтобы прочесть</summary>

Свойства `TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT` и `TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE` определяют 
допустимое время простоя ресурсов (подключения к базам данных, открытые браузеры и т.д). Для свойства 
`TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT` следует указать значение, соответствующее одному из элементов
перечисления `java.time.temporal.ChronoUnit`, для свойства `TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE` следует 
указать значение, которое может быть прочитано как положительное число типа `java.lang.Long`.

```properties
#Укажем допустимое время простоя
#5 секунд
TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_UNIT=SECONDS
TO_FREE_RESOURCES_ON_INACTIVITY_AFTER_TIME_VALUE=5
```

```java
import java.time.Duration;

import static ru.tinkoff.qa.neptune.core.api.properties
        .general.resorces.FreeResourcesOnInactivityAfter.FREE_RESOURCES_ON_INACTIVITY_AFTER;

public class SomeClass {

    public void someVoid() {
        Duration downTimeDuration = FREE_RESOURCES_ON_INACTIVITY_AFTER.get();
    }
}
```

Время по умолчанию = 30 секунд. Указанное время применяется, когда `TO_FREE_RESOURCES_ON_INACTIVITY=true`.

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Логирование шагов

##### DO_CAPTURES_OF

[Основная статья](EVENTS.MD#Когда-следует-создавать-аттачи-в-отчете-о-прохождении)

<details>
    <summary>Нажми, чтобы прочесть</summary>

Настройка указывает, в каких случаях следует делать аттачи к отчету о тестах. Объекты перечисления 
`ru.tinkoff.qa.neptune.core.api.properties.general.events.CapturedEvents`. Доступные значения:

- `SUCCESS` - если шаг завершился успешно
- `FAILURE` - если выполняющийся шаг выбросил исключение
- `SUCCESS_AND_FAILURE` - во всех случаях

Пример настройки:

```properties
#Значение свойства указывается так
DO_CAPTURES_OF=SUCCESS_AND_FAILURE
```

```java
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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

##### TO_LIMIT_REPORT_DEPTH

[Основная статья](STEPS.MD#Вложенность--глубина-шагов)

<details>
    <summary>Нажми, чтобы прочесть</summary>

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

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
</details>

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
