# Аттач текста

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

public class MyTextCaptor<T> extends StringCaptor< //<- нужно унаследовать данный класс
    T //тип объекта, который может быть превращен в текст
    > {

    @Override
    public T getCaptured(Object toBeCaptured) {
        return // Возвращает объект, с помощью которого формируется текст
        // если такой объект нельзя получить, метод должен возвращать null
    }

    @Override
    public StringBuilder getData(T caught) {
        return // возвращает текст,
        // если такой объект нельзя получить, метод должен возвращать null
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;

//Данная реализация интерфейса будет использована автоматически
public class MyTextInjector implements CapturedStringInjector {

    @Override
    public void inject(StringBuilder toBeInjected, String message) {
        //логика прикрепления картинки к отчету / логу
    }
}
```

Либо можно воспользоваться следующими готовыми классами:

- [ArrayCaptor](https://tinkoff.github.io/neptune/core.api/ru/tinkoff/qa/neptune/core/api/event/firing/collections/ArrayCaptor.html) - добавляет текстовый аттач, сформированный из массива
- [CollectionCaptor](https://tinkoff.github.io/neptune/core.api/ru/tinkoff/qa/neptune/core/api/event/firing/collections/CollectionCaptor.html) - добавляет текстовый аттач, сформированный из коллекции
- [MapCaptor](https://tinkoff.github.io/neptune/core.api/ru/tinkoff/qa/neptune/core/api/event/firing/collections/MapCaptor.html) - добавляет текстовый аттач, сформированный
  из `java.util.Map`

