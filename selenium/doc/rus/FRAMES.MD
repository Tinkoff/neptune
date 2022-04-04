# Работа с фреймами. Поиск и переключение

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст). 
  Объект [контекста для браузера](https://tinkoff.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/SeleniumStepContext.html)
  в примерах ниже вызывается статическим методом `ru.tinkoff.qa.neptune.selenium.SeleniumStepContext#inBrowser`

- О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

- О принципах работы шагов, которые выполняют действие, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-выполняют-действие).

## Оглавление
  - [Получение фрейма для дальнейшей работы](#Получение-фрейма-для-дальнейшей-работы)
  - [Наличие или отсутствие фрейма](#Наличие-или-отсутствие-фрейма)
  - [Переключение во фрейм](#Переключение-во-фрейм)

## Получение фрейма для дальнейшей работы

```java
import static java.time.Duration.ofSeconds;

import static org.openqa.selenium.By.*;
        
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier.frame;

public class Tezzt {
    
    @Test
    public void test() {
        var frame1 = inBrowser().get(frame(1) //поиск фрейма по индексу
                .timeOut(ofSeconds(5))); //время ожидания, пока фрейм появится 
        // и станет доступным для переключения

        var frame2 = inBrowser().get(frame("someNameOrId") //поиск фрейма по имени или id
                .timeOut(ofSeconds(5))); //время ожидания, пока фрейм появится 
        // и станет доступным для переключения

        var frame3 = inBrowser().get(frame(tagName("someFrameTag")) //поиск фрейма по локатору элемента, внутри которого 
                //фрейм должен находиться
                .timeOut(ofSeconds(5))); //время ожидания, пока фрейм появится 
        // и станет доступным для переключения
        
        //Во всех описанных выше случаях переключение во фрейм не происходит,
        //WebDriver остается сфокусированным на родительском фрейме/странице
    }
}
```

Время ожидания фрейма необязательно указывать при любой попытке найти фрейм. По умолчанию время на поиск фрейма 
равно 1 минуте. Если этого слишком много или мало, тогда это время можно изменить, поменяв [настройку времени ожидания фрейма](./SETTINGS.MD#Ожидание-фрейма). 
И тогда

```java
import static org.openqa.selenium.By.*;
        
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier.frame;

public class Tezzt {
    
    @Test
    public void test() {
        var frame1 = inBrowser().get(frame(1)); //поиск фрейма по индексу
        var frame2 = inBrowser().get(frame("someNameOrId")); //поиск фрейма по имени или id
        var frame3 = inBrowser().get(frame(tagName("someFrameTag"))); //поиск фрейма по локатору элемента, внутри которого 
        //а метод .timeOut вызывать при необходимости
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Наличие или отсутствие фрейма

Полезная информация: [Про наличие или отсутствие чего либо](./../../../core.api/doc/rus/STEPS.MD#Присутствие-и-отсутствие)


```java
import static java.time.Duration.ofSeconds;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier.frame;

public class Tezzt {
    
    @Test
    public void test() {
        inBrowser().presenceOf(frame(1)); //Ожидание наличия фрейма по индексу. 
        // Время ожидания == времени ожидания фрейма по умолчанию
        // вернется true если фрейм есть, и false - если нет
        inBrowser().presenceOf(frame(1).timeOut(ofSeconds(5))); //наличие фрейма по индексу.
        // Время ожидания == 5 сек
        // вернется true если фрейм есть, и false - если нет

        boolean isAlertPresent = inBrowser().presenceOfOrThrow(frame(1));
        // вернется true если фрейм есть, если нет - 
        // будет выброшено исключение

        inBrowser().absenceOf(frame(1), ofSeconds(5)); //Ожидание отсутствия фрейма. 
        //Значение времени ожидания, переданное через .timeOut, игнорируется.
        //Время ожидания, что фрейма нет, или он исчез - 5 сек
        //вернется true если фрейма нет или за 5 сек он пропал, и false - если он все еще здесь

        inBrowser().absenceOrThrow(frame(1), ofSeconds(5)); //Ожидание отсутствия фрейма. 
        //Значение времени ожидания, переданное через .timeOut, игнорируется.
        //Время ожидания, что фрейма нет, или он исчез - 5 сек
        //вернется true если фрейма нет или за 5 сек он пропал. 
        //Будет выброшено исключение, если фрейм никуда не пропал
        
        //Все выше описанное справедливо для ситуаций с поиском фреймов по имени/id или локатору элемента
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Переключение во фрейм

```java
        
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.GetFrameSupplier.frame;

public class Tezzt {
    
    @Test
    public void test() {
        inBrowser().switchTo(frame(1)); //Переключение во фрейм по его индексу
        //можно дополнить временем ожидания фрейма, см. примеры выше
        
        //Можно заранее найти фрейм
        var frame = inBrowser().get(frame(1));
        inBrowser().switchTo(frame); //и переключиться в него, когда это потребуется

        //Все выше описанное справедливо для ситуаций с поиском фреймов по имени/id или локатору элемента
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)