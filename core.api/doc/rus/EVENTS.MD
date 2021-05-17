# События

Полезные страницы:

- [Шаги](STEPS.MD)

Описанный ниже механизм используется:

- [Интеграция с Allure](./../../../allure.integration/README.md)
- [Локализация](./LOCALIZATION.MD)

## Логгер событий

Для того чтобы выполняющиеся шаги могли отображаться в отчете о тестах / в консоли / в любом другом формате необходимо

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;

import java.util.Map;

public class MyEventLogger implements EventLogger { // реализовать данный интерфейс

    @Override
    public void fireTheEventStarting(String message, // заголовок шага
                                     Map<String, String> parameters //набор параметров шага
    ) {
        //Логика описания начала выполнения шага
    }

    @Override
    public void fireThrownException(Throwable throwable) {
        //логика, срабатывающая если выполнение шага завершилось выбросом исключения
    }

    @Override
    public void fireReturnedValue(String resultDescription, // пояснение к результату
                                  Object returned) { //вернувшийся результат
        //логика, срабатывающая если выполнение шага завершилось возвратом результата
    }

    @Override
    public void fireEventFinishing() {
        // логика, срабатывающая при завершении выполнения шага 
    }
}
```

Ничего больше делать не надо. Данный класс будет использован автоматически. ВАЖНО!!! У класса, реализующего интерфейс
`EventLogger`, не должно быть объявленных конструкторов, или должен быть объявлен доступный конструктор без параметров.

## Аттачи

Для дополнительной детализации отчета о выполнении тестов, к выполненным шагам можно прикладывать различные файлы (
скриншоты, снэпшоты записей из баз данных, снэпшоты отправленных http-запросов/полученных http ответов и т.д.).

Ниже пример того, как описать создание аттачей.

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.CapturedDataInjector;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.UseInjectors;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;

@Description("My attachment") // Пояснение / название аттача
@UseInjectors(MyInjector.class) //Тип объекта, который добавляет аттач непосредственно к отчету / логу.
// Здесь можно указать несколько классов.
// Можно указать абстрактные классы, реализующие интерфейс CapturedDataInjector<S>, или интерфейсы,
// расширяющие CapturedDataInjector<S>. Если в classpath есть их неабстрактные наследники / имплементоры,
// тогда они будут использованы автоматически.
// ВАЖНО: При автоматическом использовании имплементоров CapturedDataInjector<S>, эти имплементоры не
// должны иметь объявленных конструкторов, или у них должны быть объявлены доступные конструкторы без параметров.
public class MyCaptor<T, S> extends Captor< // <- нужно унаследовать данный класс
        T, //тип объекта, который может быть превращен в аттач
        S> { //тип объекта-аттача

    //Добавлено для наглядности!!! Объявленные публичные конструкторы должны быть без параметров
    // Можно перекрыть данный конструктор.
    protected MyCaptor(String message, //название аттача
                       List<? extends CapturedDataInjector<S>> injectors) //коллекция объектов, которые добавляют
                       // аттачи непосредственно к отчету / логу.
    {
        super(message, injectors);
    }

    //Добавлено для наглядности!!! Объявленные публичные конструкторы должны быть без параметров
    // Можно перекрыть данный конструктор.
    public MyCaptor(List<? extends CapturedDataInjector<S>> injectors) //коллекция объектов, которые добавляют
    // аттачи непосредственно к отчету / логу.
    // Название аттача будет создано при помощи аннотации @Description
    {
        super(injectors);
    }

    //Добавлено для наглядности!!! Объявленные публичные конструкторы должны быть без параметров
    // Можно перекрыть данный конструктор.
    public MyCaptor(String message) //название аттача
    // коллекция объектов, которые добавляют аттачи непосредственно к отчету / логу, 
    // будет подготовлена с использованием аннотации @UseInjectors
    {
        super(message);
    }

    // Название аттача будет создано при помощи аннотации @Description
    // Коллекция объектов, которые добавляют аттачи непосредственно к отчету / логу, 
    // будет подготовлена с использованием аннотации @UseInjectors
    public MyCaptor() {
        super();
    }

    @Override
    public S getData(T caught) {
        return // возвращает объект-аттач, 
        // если такой объект нельзя получить, метод должен возвращать null
    }

    @Override
    public T getCaptured(Object toBeCaptured) {
        return // Возвращает объект, с помощью которого формируется аттач
        // если такой объект нельзя получить, метод должен возвращать null
    }
}
```

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.CapturedDataInjector;

//Класс объектов, которые добавляют аттач в лог / отчет о тесте.
public class MyInjector<S> implements CapturedDataInjector< // <- нужно реализовать этот интерфейс
        S // Тип объекта, добавляемого как аттач
        > {

    @Override
    public void inject(S toBeInjected, String message) {
        //логика добавления приложения к отчету / логу
    }
}
```

Подробнее о том как связать классы, формирующие аттачи и классы, описывающие и формирующие шаги,
см. [здесь](STEPS.MD#Аттачи). Ничего более делать не нужно.

Можно воспользоваться одним из следующих вариантов расширения `Captor` / реализации `CapturedDataInjector`:

**- аттач файла**

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

Класс из примера ниже можно не реализовывать, если подключены:

- [Интеграция с Allure](./../../../allure.integration/README.md)

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedFileInjector;

import java.io.File;

//Данная реализация интерфейса будет использована автоматически.
public class MyFileInjector implements CapturedFileInjector {

    @Override
    public void inject(File toBeInjected, String message) {
        //логика прикрепления файла к отчету / логу
    }
}

```

**- аттач картинки**

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

Класс из примера ниже можно не реализовывать, если подключены:

- [Интеграция с Allure](./../../../allure.integration/README.md)

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedImageInjector;

import java.awt.image.BufferedImage;

//Данная реализация интерфейса будет использована автоматически
public class MyImageInjector implements CapturedImageInjector {

    @Override
    public void inject(BufferedImage toBeInjected, String message) {
        //логика прикрепления картинки к отчету / логу
    }
}
```

**- аттач текста**

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

Класс из примера ниже можно не реализовывать, если подключены:

- [Интеграция с Allure](./../../../allure.integration/README.md)

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedImageInjector;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;

import java.awt.image.BufferedImage;

//Данная реализация интерфейса будет использована автоматически
public class MyTextInjector implements CapturedStringInjector {

    @Override
    public void inject(StringBuilder toBeInjected, String message) {
        //логика прикрепления картинки к отчету / логу
    }
}
```

Либо можно воспользоваться следующими готовыми классами:

- `ru.tinkoff.qa.neptune.core.api.event.firing.collections.ArrayCaptor` - добавляет текстовый аттач, сформированный из
  массива
- `ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor` - добавляет текстовый аттач, сформированный
  из коллекции
- `ru.tinkoff.qa.neptune.core.api.event.firing.collections.MapCaptor` - добавляет текстовый аттач, сформированный
  из `java.util.Map`

### Когда следует создавать аттачи в отчете о прохождении

Аттач делается:

- при успешном выполнении шагов теста
- при неудачном завершении шагов теста
- как при успешном завершении шагов, так и при неудачном.

По умолчанию функция выключена. Включается с помощью [настроек](SETTINGS.MD#DO_CAPTURES_OF)