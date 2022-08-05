# Создание собственных настроек

Работа с настройками _Neptune_ подробно описана в [этом](../../quick_start/settings/index.md) разделе.

Данный раздел содержит документацию о том, как создать свой набор настроек.

## Как описывается свойство

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

//Название свойства. Рекомендуется давать названия как в примере, 
//чтобы можно было использовать в качестве переменной среды.
//Аннотация обязательная!!!!!
@PropertyName("MY_PROPERTY")
//Значение свойства по умолчанию.
// Будет возвращаться значение, прочитанное из этой строки и преобразованное в
// значение типа T при соблюдении следующих условий:
// - значение этого свойства не указано в neptune.properties и neptune.global.properties
// - Результаты System.getenv() и System.getProperties() не содержат свойства
// - окружение тестируемого приложения не содержит имя свойства в качестве ключа
//Аннотация необязательная.
@PropertyDefaultValue("Some default value")
//Аннотация ниже формирует описание свойства / комментарий к нему.
//Аннотация не обязательная (но рекомендуется).
@PropertyDescription(section = "Some logical section", //Название раздела/группы свойств
        description = {"Description string 1", //описание,
                "Description string 2", //построчно
                "Description string 3"})
public class MyProperty implements
        PropertySupplier< //нужно реализовать этот интерфейс
                T, //тип объекта, в который преобразуется строка, 
                // прочитанная из значения свойства
                R> { // Тип объекта, который может быть 
                    // преобразован в строку-значение. 
                    // Может совпадать с типом выше.

    // Не обязательно так делать. 
    // Просто для быстрого доступа к значению свойства
    public static final MyProperty PROPERTY = new MyProperty();

    @Override
    public String readValuesToSet(R value) {
        //тут можно описать алгоритм, к
        // ак преобразовать переданный объект в строку-значение свойства
        return PropertySupplier.super.readValuesToSet(value);
        //или своя реализация
    }

    @Override
    public T parse(String value) {
        //тут нужно описать алгоритм, 
        // как преобразовать строку-значение свойства
        return //алгоритм преобразования;
    }

    @Override 
    public T returnIfNull() {
        //Тут можно описать, какое значение вернуть вместо null.
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
        //так делать не рекомендуется, просто демонстрация возможностей
        PROPERTY.accept(newValue);

        //Обнуление свойства
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

```{toctree}
:hidden:

string.rst
boolean.rst
byte.rst
double.rst
float.rst
integer.rst
long.rst
short.rst
url.rst
enum_single_item.rst
enum_multiple_items.rst
duration.rst
any_objects.rst
property_sources.rst
```
