# Интерактивные действия

См. [Поиск элементов страницы](/doc/rus/selenium/SearchingForElements.md)

См. [Предлагаемое использование шаблона проектирования Page Object](/doc/rus/selenium/SearchingForElements.md#Предлагаемое-использование-шаблона-проектирования-Page-Object)

Для выполнения интерактивных действий используется [InteractiveAction](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/intreraction/InteractiveAction.html)

| [Данные, которые могут быть  приложены к отчетам (по умолчанию)](/doc/rus/core/Steps.md#Данные,-которые-могут-быть-приложены-к-отчетам-и-логу) 	|
|-----------------------------------------------------------------	|
| Скриншоты Прочие файлы                                          	|


## Нажатие клавиш-модификаторв:

```java
//пример: простое нажатие клафиш-модификаторв
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.Keys.ALT;
import static org.openqa.selenium.Keys.CONTROL;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.keyDown;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(keyDown(CONTROL))
                .perform(keyDown(ALT));
        //....        
    }
}
```
---
```java
//пример: нажатие клафиш-модификаторв с фокусировкой на элементе страницы (WebElement)
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static org.openqa.selenium.Keys.ALT;
import static org.openqa.selenium.Keys.CONTROL;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.keyDown;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var webElement = seleniumSteps.find(webElement(tagName("button")));
        seleniumSteps.perform(keyDown(webElement, CONTROL))
                .perform(keyDown(webElement, ALT));
        //....        
    }
}
```
---

```java
//пример: нажатие клафиш-модификаторв с фокусировкой на элементе страницы (Widget)
//На примере кнопки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.Keys.ALT;
import static org.openqa.selenium.Keys.CONTROL;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.keyDown;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var button = seleniumSteps.find(button("Push me"));
        seleniumSteps.perform(keyDown(button, CONTROL))
                .perform(keyDown(button, ALT));
        //....        
    }
}
```
---

```java
//пример: нажатие клафиш-модификаторв с фокусировкой на элементе страницы (WebElement)
//Указывается способ, как найти целевой элемент
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static org.openqa.selenium.Keys.ALT;
import static org.openqa.selenium.Keys.CONTROL;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.keyDown;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(keyDown(webElement(tagName("button")), CONTROL))
                .perform(keyDown(webElement(tagName("button")), ALT));
        //....        
    }
}
```
---

```java
//пример: нажатие клафиш-модификаторв с фокусировкой на элементе страницы (Widget)
//На примере кнопки
//Указывается способ, как найти целевую кнопку
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static org.openqa.selenium.Keys.ALT;
import static org.openqa.selenium.Keys.CONTROL;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.keyDown;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(keyDown(button("Push me"), CONTROL))
                .perform(keyDown(button("Push me"), ALT));
        //....        
    }
}
```
---


---
См. также [Шаги, выполняющие действие](/doc/rus/core/Steps.md#Шаги,-выполняющие-действие)

