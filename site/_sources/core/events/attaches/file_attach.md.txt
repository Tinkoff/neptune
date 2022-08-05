# Аттач файла

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;

import java.io.File;

public class MyFileCaptor<T> extends FileCaptor< //<- нужно унаследовать данный класс
        T //тип объекта, который может быть превращен в файл
        > {

    @Override
    public T getCaptured(Object toBeCaptured) {
        return // Возвращает объект, с помощью которого формируется файл
        // если такой объект нельзя получить, метод должен возвращать null
    }

    @Override
    public File getData(T caught) {
        return // возвращает файл,
        // если такой объект нельзя получить, метод должен возвращать null
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedFileInjector;

import java.io.File;

public class MyFileInjector implements CapturedFileInjector {

    @Override
    public void inject(File toBeInjected, String message) {
        //логика прикрепления файла к отчету / логу
    }
}

```

