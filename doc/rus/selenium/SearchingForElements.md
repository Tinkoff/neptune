# Поиск элементов страницы

Данный модуль позволяет описывать взаимодействие с элементами страницы как посредством [WebElement](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/WebElement.html) так и [используя шаблон проектирования Page Object](https://www.seleniumhq.org/docs/06_test_design_considerations.jsp#page-object-design-pattern).
Поиск элементов является основным шагом перед выполнением различных интерактивных действий на странице.

## Инструменты поиска элементов страницы

См. [как построить цепочки шагов, возвращающие результат](/doc/rus/core/Steps.md#Построение-цепочек-шагов,-возвращающих-результат). 
Используются в связке с [Контекстом шагов для Selenium](/doc/rus/selenium/SeleniumStepContext.md).

## Поиск одного элемента

Для поиска одного элемента используется [SearchSupplier](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/SearchSupplier.html)

| Возможность использовать критерии 	| Объединение критериев 	| Возможность указывать индивидуальный таймаут 	| Если желаемое значение не получено                                   	| Данные, которые могут быть  приложены к отчетам (по умолчанию)  	|
|-----------------------------------	|-----------------------	|----------------------------------------------	|----------------------------------------------------------------------	|-----------------------------------------------------------------	|
|           [x]                     	| *AND*                   	|                [x]                           	| По умолчанию выбрасывает  org.openqa.selenium.NoSuchElementException 	| Скриншоты Прочие файлы                                          	|

```java
//пример обычного поиска элемента
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //**
        var webElement = seleniumSteps.find(webElement(className("MyClass")));
        //**
    }
}
```

В примере выше происходит обычный поиск элемента по классу (аттрибут html). Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

```java
//пример поиска элемента по тексту
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //**
        var webElement = seleniumSteps.find(webElement(className("MyClass"), "МуText"));
        //**
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html) и его полному тексту. Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

```java
//пример поиска элемента по регулярному выражению
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.util.regex.Pattern.compile;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //**
        var webElement = seleniumSteps.find(webElement(className("MyClass"), compile("МуTextPattern")));
        //**
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html) и его тексту, соответствующему регулярному выражению. Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

```java
//пример поиска элемента по критерию
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //**
        var webElement = seleniumSteps.find(webElement(className("MyClass"))
                .criteria(shouldBeEnabled()));        
        //**
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html) и по условию `элемент должен быть доступен`. Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).

```java
//пример поиска элемента по нескольким критериям
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //**
        var webElement = seleniumSteps.find(webElement(className("MyClass"))
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100));      
        //**
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html) и условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ). Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

```java
//пример поиска элемента по нескольким критериям и тексту элемента
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //**
        var webElement = seleniumSteps.find(webElement(className("MyClass"), "МуText")
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100));      
        //**
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html), условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ) и его полному тексту. Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

```java
//пример поиска элемента по нескольким критериям и тексту элемента
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.util.regex.Pattern.compile;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //**
        var webElement = seleniumSteps.find(webElement(className("MyClass"), compile("МуTextPattern"))
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100));      
        //**
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html), условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ) и его тексту, соответствующему регулярному выражению. Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

## Поиск множества элементов

Для поиска множества элементов используется [MultipleSearchSupplier](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/MultipleSearchSupplier.html)

| Возможность использовать критерии 	| Объединение критериев 	| Возможность указывать индивидуальный таймаут 	| Если желаемое значение не получено    | Данные, которые могут быть  приложены к отчетам (по умолчанию)  	|
|-----------------------------------	|-----------------------	|----------------------------------------------	|---------------------------------------|-----------------------------------------------------------------	|
|           [x]                     	| *AND*                   	|                [x]                           	|  Возвращает пустой List           	| Скриншоты Прочие файлы                                          	|

## Предлагаемое использование шаблона проектирования Page Object.

Предлагается не описывать всю страницу целиком, а использовать типизированные элементы/структуры элементов. В рамках данного модуля введено такое понятие, как [виджет](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/Widget.html). 

### Как создать виджет 

```java

``` 

## Поиск элементов