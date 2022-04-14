# Навигация по страницам

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст). 
  Объект [контекста для браузера](https://tinkoff.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/SeleniumStepContext.html)
  в примерах ниже вызывается статическим методом `ru.tinkoff.qa.neptune.selenium.SeleniumStepContext#inBrowser`

- О принципах работы шагов, которые выполняют действие, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-выполняют-действие).

Важная информация:

- [Работа с окнами/вкладками браузера](./WINDOWS.MD)
- [Настройки. BASE_WEB_DRIVER_URL](./SETTINGS.MD#base_web_driver_url)

## Оглавление
  - [Переход по ссылкам](#Переход-по-ссылкам)
  - [Переход на следующую ссылку/возврат на предыдущую ссылку](#Переход-на-следующую-ссылкувозврат-на-предыдущую-ссылку)
  - [Вызов обновления страницы](#Вызов-обновления-страницы)

## Переход по ссылкам

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public class Tezzt {

    @Test
    public void test() {
        URL url = new URL("https://www.google.com/search?q=test+automation");
        //так же можно использовать строку (String)

        inBrowser().navigateTo(url); //Навигация в текущем браузерном окне/вкладке
        inBrowser().navigateTo(url, window()); //Навигация в браузерном окне/вкладке,
        //Указанном в виде алгоритма поиска

        var w = inBrowser().get(window());
        inBrowser().navigateTo(url, w); //Навигация в заранее найденном браузерном окне/вкладке
    }

}
```

Если для проекта указано значение свойства/переменной окружения `BASE_WEB_DRIVER_URL`, тогда можно использовать фрагмент 
относительно этого значения.

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

//Предположим что BASE_WEB_DRIVER_URL=https://www.google.com
public class Tezzt {

    @Test
    public void test() {
        inBrowser().navigateTo("/search?q=test+automation"); //Навигация в текущем браузерном окне/вкладке
        inBrowser().navigateTo("/search?q=test+automation", window()); //Навигация в браузерном окне/вкладке,
        //Указанном в виде алгоритма поиска

        var w = inBrowser().get(window());
        inBrowser().navigateTo("/search?q=test+automation", w); //Навигация в заранее найденном браузерном окне/вкладке
    }

}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Переход на следующую ссылку/возврат на предыдущую ссылку

```java

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public class Tezzt {
    @Test
    public void test() {
        inBrowser().navigateForward(); //Переход на следующую ссылку в текущем браузерном окне/вкладке
        inBrowser().navigateBack(); //Возврат на предыдущую ссылку в текущем браузерном окне/вкладке

        inBrowser().navigateForward(window()); //Переход на следующую ссылку в браузерном окне/вкладке,
        //Указанном в виде алгоритма поиска
        inBrowser().navigateBack(window()); //Возврат на предыдущую ссылку в браузерном окне/вкладке,
        //Указанном в виде алгоритма поиска

        var w = inBrowser().get(window());
        inBrowser().navigateForward(w); //Переход на следующую ссылку в заранее найденном браузерном окне/вкладке
        inBrowser().navigateForward(w); //Возврат на предыдущую ссылку в заранее найденном браузерном окне/вкладке
    }
}

```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Вызов обновления страницы

```java

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;

public class Tezzt {
    @Test
    public void test() {
        inBrowser().refresh(); //Вызов обновления страницы в текущем браузерном окне/вкладке
        inBrowser().refresh(window()); //Вызов обновления страницы в браузерном окне/вкладке,
        //Указанном в виде алгоритма поиска

        var w = inBrowser().get(window());
        inBrowser().refresh(w); //Вызов обновления страницы в заранее найденном браузерном окне/вкладке
    }
}

```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)