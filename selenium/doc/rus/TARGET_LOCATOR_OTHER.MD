# Переключение контента. Прочее.

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст). 
  Объект [контекста для браузера](https://tinkoff.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/SeleniumStepContext.html)
  в примерах ниже вызывается статическим методом `ru.tinkoff.qa.neptune.selenium.SeleniumStepContext#inBrowser`

- О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

- О принципах работы шагов, которые выполняют действие, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-выполняют-действие).

Здесь описаны случаи, выходящие за рамки того, что описано в следующих главах:

- [Работа с фреймами. Поиск и переключение](./FRAMES.MD)

- [Работа с окнами. Поиск и переключение](./WINDOWS.MD)

- [Работа с алертами](./ALERTS.MD)

## Оглавление
  - [Переключение в родительский фрейм](#Переключение-в-родительский-фрейм)
  - [Переключение в активный элемент](#Переключение-в-активный-элемент)
  - [Переключение в дефолтный контент](#Переключение-в-дефолтный-контент)

## Переключение в родительский фрейм

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame.parent.ParentFrameSupplier.parentFrame;

public class Tezzt {
    
    @Test
    public void test() {
        inBrowser().switchTo(parentFrame());
    }
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Переключение в активный элемент

```java
import static java.time.Duration.ofSeconds;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.active.element.GetActiveElementSupplier.activeElement;

public class Tezzt {
    
    @Test
    public void test() {
        inBrowser().switchTo(activeElement());
    }
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Переключение в дефолтный контент 

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.content.DefaultContentSupplier.defaultContent;

public class Tezzt {
    
    @Test
    public void test() {
        //переключение либо в первый фрейм на странице, либо в основной документ
        //Фокус остается в текущем окне/вкладке
        inBrowser().switchTo(defaultContent());
    }
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

