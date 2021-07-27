# Связывание классов

Данный механизм может быть полезен в следующих случаях:

- когда классы, которые написаны для тестирования (и поддерживаемые Neptune) не должны быть жестко связаны/влиять на 
  работу компонентов кода, написанного для продукта. Но тестовые классы зависят от продуктового кода и могут его повторно 
  использовать.
  
- когда классы, которые написаны для тестирования (и поддерживаемые Neptune) связаны с кодом, получены в результате 
  кодогенерации. 
  
Описанный ниже механизм реализует не-ссылочную связь один ко многим. Один класс, написанный для тестирования, может
быть привязан ко многим сгенерированным/продуктовым классам и использовать их.
  
## Оглавление

- [Примеры](#Примеры)
- [Реализованные механизмы привязки](#Реализованные-механизмы-привязки)

## Примеры

```java
package org.mypack;

public class A {
    //реализация и логика
}
```

```java
package org.mypack;

public class B {
    //реализация и логика
}
```

Тогда

```java
package org.mypack;

import ru.tinkoff.qa.neptune.core.api.binding.Bind;

@Bind(to = A.class)
@Bind(to = B.class)
public class C { //Данный класс как-то надо привязать к А и B
}
```

```java
package org.mypack;

import ru.tinkoff.qa.neptune.core.api.binding.Bind;

public class D {

  @Bind(to = A.class)
  @Bind(to = B.class)
  private Object f; //Данное поле как-то надо привязать к А и B
}
```

Усложним задачу

```java
package org.mypack;

public class E extends A {
  //реализация и логика
}

```

```java
package org.mypack;

public class F extends A {
  //реализация и логика
}
```

Тогда

```java
package org.mypack;

import ru.tinkoff.qa.neptune.core.api.binding.Bind;

@Bind(to = A.class,
        withSubclasses = true, //привязка к классам-наследникам
        exclude = {E.class}) //можно перечислить, какие наследники класса A должны быть исключены
//точно так же можно аннотировать поля
public class G {
}
```

Ниже пример того как получить связи класса A (классы и поля)

```java
package org.mypack;

import java.lang.reflect.AnnotatedElement;
import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.binding.Bind.DefaultBindReader.getBoundTo;

public class SomeCode {

  public void someMethod() {
    //получает список связанных классов и полей
    List<AnnotatedElement> annotatedElements = getBoundTo(A.class);
    
    //Дальнейшая обработка связей осуществляется индивидуально для каждого модуля Neptune
  }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Реализованные механизмы привязки

- [Интеграция с Http-клиентом Java v>=11](./../../../http.api/README.md)
  Описано [тут](./../../../http.api/doc/rus/MAPPING.MD#Инициализация-объектов-моделей-сервисов-в-клиентском-коде-и-их-использование) 
  и [тут](./../../../http.api/doc/rus/MAPPING.MD#Недефолтные-корневые-URL)

- [Интеграция с Retrofit2](./../../../retrofit2/README.md)
  Описано [тут](./../../../retrofit2/doc/rus/SETTINGS.MD#Недефолтные-корневые-URL)
  и [тут](./../../../retrofit2/doc/rus/SETTINGS.MD#Недефолтные-настройки-Retrofit)
  


[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

