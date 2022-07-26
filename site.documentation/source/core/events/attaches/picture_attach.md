# Аттач картинки

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;

import java.awt.image.BufferedImage;

public class MyImageCaptor<T> extends ImageCaptor< //<- нужно унаследовать данный класс
    T //тип объекта, который может быть превращен в картинку
    > {

    @Override
    public T getCaptured(Object toBeCaptured) {
        return // Возвращает объект, с помощью которого формируется картинка
        // если такой объект нельзя получить, метод должен возвращать null
    }

    @Override
    public BufferedImage getData(T caught) {
        return // возвращает картинку,
        // если такой объект нельзя получить, метод должен возвращать null
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedImageInjector;

import java.awt.image.BufferedImage;

public class MyImageInjector implements CapturedImageInjector {

    @Override
    public void inject(BufferedImage toBeInjected, String message) {
        //логика прикрепления картинки к отчету / логу
    }
}
```

