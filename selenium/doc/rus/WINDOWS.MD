# Работа с окнами. Поиск и переключение

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст). 
  Объект [контекста для браузера](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/SeleniumStepContext.html)
  в примерах ниже вызывается статическим методом `ru.tinkoff.qa.neptune.selenium.SeleniumStepContext#inBrowser`

- О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги,-которые-возвращают-результат).

- О принципах работы шагов, которые выполняют действие, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги,-которые-выполняют-действие).

- О критериях можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Критерии).
  
- [Критерии для окон/вкладок браузера](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/functions/target/locator/window/WindowCriteria.html)

## Получение окна для дальнейшей работы

```java
import static java.time.Duration.ofSeconds;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.WindowCriteria.*;

public class Tezzt {

    @Test
    public void test() {
        var w1 = inBrowser().get(currentWindow()); //получение текущего окна/вкладки
        var w2 = inBrowser().get(window()); //будет взято первое попавшееся окно/вкладка
        //пример выше можно уточнить

        var w3 = inBrowser().get(window(1)); //получение окна по его индексу, начиная с 0
        var w4 = inBrowser().get(window() //будет взято окно/вкладка, удовлетворяющая некоторому критерию
                .criteria(titleMatches("^.*\\b(Github)\\b.*$")) //например то, что заголовок страницы соответствует 
                // регулярному выражению или содержит подстроку
                .criteria(urlMatches("^.*\\b(github)\\b.*$"))) //и загруженная ссылка соответствует
                // регулярному выражению или содержит подстроку
                ;

        //комбинация примеров w3 и w4. 
        //получить второе окно/вкладку при условии, что в нем загружено что-то похожее на 
        //github.com
        var w5 = inBrowser().get(window(1)
                .criteria(titleMatches("^.*\\b(Github)\\b.*$"))
                .criteria(urlMatches("^.*\\b(github)\\b.*$")));

        //тот же самый случай, что и w5, но дополненный временем ожидания.
        //Ситуация: нужно некоторое время, пока откроется второе окно и в нем загрузится что-то похожее на 
        //github.com
        var w6 = inBrowser().get(window(1)
                .criteria(titleMatches("^.*\\b(Github)\\b.*$"))
                .criteria(urlMatches("^.*\\b(github)\\b.*$"))
                .timeOut(ofSeconds(5)));

        //Во всех описанных выше случаях переключение в окно/вкладку не происходит,
        //WebDriver остается сфокусированным в том же окне, что и раньше
    }
}
```

Время ожидания окна необязательно указывать, т.к. по умолчанию время на поиск окна равно 1 минуте. Если этого слишком 
много или мало, тогда это время можно изменить, поменяв [настройку времени ожидания окна](./SETTINGS.MD#Ожидание-окна).

## Наличие или отсутствие окна

Полезная информация: [Про наличие или отсутствие чего либо](./../../../core.api/doc/rus/STEPS.MD#Присутствие-и-отсутствие)


```java
import static java.time.Duration.ofSeconds;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        inBrowser().presenceOf(window()); //Ожидание наличия окна/вкладки. 
        // Как и в примере выше, здесь можно указать индекс окна, критерии и время ожидания
        // вернется true если окно есть, и false - если нет

        boolean isAlertPresent = inBrowser().presenceOfOrThrow(window());
        // вернется true если окно есть, если нет - 
        // будет выброшено исключение

        inBrowser().absenceOf(window(), ofSeconds(5)); //Ожидание отсутствия окна. 
        //Можно указать индекс окна, критерии. Значение времени ожидания, переданное через .timeOut, игнорируется.
        //Время ожидания, что окна/вкладки нет, или оно исчезло - 5 сек
        //вернется true если окна нет или за 5 сек оно пропало, и false - если оно все еще здесь

        inBrowser().absenceOrThrow(window(), ofSeconds(5)); //Ожидание отсутствия окна. 
        //Можно указать индекс окна, критерии. Значение времени ожидания, переданное через .timeOut, игнорируется.
        //Время ожидания, что окна/вкладки нет, или оно исчезло - 5 сек
        //вернется true если окна нет или за 5 сек оно пропало.
        //Будет выброшено исключение, если окно/вкладка никуда не пропало
    }
}
```

## Переключение в окно

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        inBrowser().switchTo(window()); //Переключение в окно/вкладку.
        // Как и в примере выше, здесь можно указать индекс окна, критерии и время ожидания.
        
        //Можно заранее найти окно/вкладку
        var w = inBrowser().get(window());
        inBrowser().switchTo(w); //и переключиться в него, когда это потребуется
    }
}
```

## Положение окна

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        inBrowser().changeWindowPosition(1000, 1200); //смена положения на экране текущего окна
        //x=1000, y=1200

        inBrowser().changeWindowPosition(window(), 1000, 1200); //смена положения на экране окна, указанного в виде алгоритма поиска
        //x=1000, y=1200
        
        //Можно заранее найти окно/вкладку
        var w = inBrowser().get(window());
        inBrowser().changeWindowPosition(w, 1000, 1200); //и указать новое положение на экране
        //x=1000, y=1200
        
        var p = inBrowser().windowPosition(); //Позиция текущего окна на экране
        var p2 = inBrowser().positionOf(window()); //Позиция на экране окна, указанного в виде алгоритма поиска

        //Можно заранее найти окно/вкладку
        var w2 = inBrowser().get(window());
        var p3 = inBrowser().positionOf(w2); //и получить его позицию на экране
    }
}
```

## Размер окна

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        inBrowser().changeWindowSize(1000, 1200); //смена размера текущего окна
        //ширина 1000, высота 1200
        inBrowser().fullScreen(); //Разворот текущего окна на полный экран

        inBrowser().changeWindowSize(window(), 1000, 1200); //смена размера окна, указанного в виде алгоритма поиска
        //ширина 1000, высота 1200
        inBrowser().fullScreen(window()); //Разворот на полный экран окна, указанного в виде алгоритма поиска
        
        //Можно заранее найти окно/вкладку
        var w = inBrowser().get(window());
        inBrowser().changeWindowSize(w, 1000, 1200); //и указать новый размер
        //ширина 1000, высота 1200
        inBrowser().fullScreen(w); //И развернуть на полный экран
        
        var d = inBrowser().windowSize(); //Размер текущего окна
        var d2 = inBrowser().sizeOf(window()); //Размер окна, указанного в виде алгоритма поиска

        //Можно заранее найти окно/вкладку
        var w2 = inBrowser().get(window());
        var d3 = inBrowser().sizeOf(w2); //и получить его размер
    }
}
```

## Заголовок окна

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        var t = inBrowser().windowTitle(); //Заголовок текущего окна
        var t2 = inBrowser().windowTitle(window()); //Заголовок окна, указанного в виде алгоритма поиска

        //Можно заранее найти окно/вкладку
        var w = inBrowser().get(window());
        var t3 = inBrowser().windowTitle(w); //и получить его заголовок
    }
}
```

## Ссылка загруженной страницы

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        var u = inBrowser().getCurrentUrl(); //ссылка на страницу, загруженную в текущем окне/вкладке
        var u2 = inBrowser().getCurrentUrl(window()); //ссылка на страницу, загруженную в окне/вкладке, 
        //указанном в виде алгоритма поиска

        //Можно заранее найти окно/вкладку
        var w = inBrowser().get(window());
        var t3 = inBrowser().getCurrentUrl(w); //и получить ссылку на загруженную страницу
    }
}
```

## Закрытие окна

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        inBrowser().closeWindow(); //закрытие текущего окна
        inBrowser().closeWindow(window()); //закрытие окна, указанного в виде алгоритма поиска     
        
        //Можно заранее найти окно/вкладку
        var w = inBrowser().get(window());
        inBrowser().closeWindow(w); //и закрыть его
    }
}
```