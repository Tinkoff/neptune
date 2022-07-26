# Аннотации для шагов

В данном разделе описаны аннотации, связывающие [шаги](./../../../steps/steps/index.md) и классы, создающие аттачи

Для примера будем использовать следующие классы

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

//Предположим, что объект-контекст может содержать данные,
// которые можно использовать как текстовое приложение к отчету о тесте
public class ExampleCaptor extends Captor<T, String> {

    @Override
    public String getData(T caught) {
        return //возврат какой-то строки
    }

    @Override
    public T getCaptured(Object toBeCaptured) {
        return //убеждаемся, что объект типа T или
        //что от объекта можно получить значение типа T,
        // и приводим объект к этому типу
        // или создаем объект этого типа
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

public class ExampleCaptor2 extends Captor<M, String> {

    @Override
    public String getData(M caught) {
        return //возврат какой-то строки
    }

    @Override
    public M getCaptured(Object toBeCaptured) {
        return //убеждаемся, что объект типа M или
        //что от объекта можно получить значение типа M,
        // и приводим объект к этому типу
        // или создаем объект этого типа
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

public class ExampleCaptor3 extends Captor<R, String> {

    @Override
    public String getData(R caught) {
        return //возврат какой-то строки
    }

    @Override
    public R getCaptured(Object toBeCaptured) {
        return //убеждаемся, что объект типа R или
        //что от объекта можно получить значение типа R,
        // и приводим объект к этому типу
        // или создаем объект этого типа
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

public class ExampleCaptor4 extends Captor<V, String> {

    @Override
    public String getData(V caught) {
        return //возврат какой-то строки
    }

    @Override
    public V getCaptured(Object toBeCaptured) {
        return //убеждаемся, что объект типа V или
        //что от объекта можно получить значение типа V,
        // и приводим объект к этому типу
        // или создаем объект этого типа
    }
}
```

```{toctree}
:hidden:

@CaptureOnSuccess.md
@CaptureOnFailure.md
```
