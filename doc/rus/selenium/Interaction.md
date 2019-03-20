# Интерактивные действия

См. [Поиск элементов страницы](/doc/rus/selenium/SearchingForElements.md)

См. [Предлагаемое использование шаблона проектирования Page Object](/doc/rus/selenium/SearchingForElements.md#Предлагаемое-использование-шаблона-проектирования-Page-Object)

Для выполнения интерактивных действий используется [InteractiveAction](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/intreraction/InteractiveAction.html)

| [Данные, которые могут быть  приложены к отчетам (по умолчанию)](/doc/rus/core/Steps.md#Данные,-которые-могут-быть-приложены-к-отчетам-и-логу) 	|
|-----------------------------------------------------------------	|
| Скриншоты Прочие файлы                                          	|


## Нажатие клавиш-модификаторов:

```java
//пример: простое нажатие клафиш-модификаторов
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
//пример: нажатие клафиш-модификаторов, с фокусировкой на элементах страницы (WebElement)
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
//пример: нажатие клафиш-модификаторов, с фокусировкой на элементах страницы (Widget)
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
//пример: нажатие клафиш-модификаторов, с фокусировкой на элементах страницы (WebElement)
//Указывается способ, как найти целевые элементы
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
//пример: нажатие клафиш-модификаторов, с фокусировкой на элементах страницы (Widget)
//На примере кнопки
//Указывается способ, как найти целевые виджеты
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

## Освобождение клавиш-модификаторов:

```java
//пример: простое освобождение нажатых клафиш-модификаторов
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.Keys.ALT;
import static org.openqa.selenium.Keys.CONTROL;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.keyUp;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(keyUp(CONTROL))
                .perform(keyUp(ALT));
        //....        
    }
}
```
---
```java
//пример: освобождение нажатых клафиш-модификаторов, с фокусировкой на элементах страницы (WebElement)
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static org.openqa.selenium.Keys.ALT;
import static org.openqa.selenium.Keys.CONTROL;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.keyUp;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var webElement = seleniumSteps.find(webElement(tagName("button")));
        seleniumSteps.perform(keyUp(webElement, CONTROL))
                .perform(keyUp(webElement, ALT));
        //....        
    }
}
```
---

```java
//пример: освобождение нажатых клафиш-модификаторов, с фокусировкой на элементах страницы (Widget)
//На примере кнопки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.Keys.ALT;
import static org.openqa.selenium.Keys.CONTROL;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.keyUp;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var button = seleniumSteps.find(button("Push me"));
        seleniumSteps.perform(keyUp(button, CONTROL))
                .perform(keyUp(button, ALT));
        //....        
    }
}
```
---

```java
//пример: освобождение нажатых клафиш-модификаторов, с фокусировкой на элементах страницы (WebElement)
//Указывается способ, как найти целевые элементы
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static org.openqa.selenium.Keys.ALT;
import static org.openqa.selenium.Keys.CONTROL;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.keyUp;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(keyUp(webElement(tagName("button")), CONTROL))
                .perform(keyUp(webElement(tagName("button")), ALT));
        //....        
    }
}
```
---

```java
//пример: освобождение нажатых клафиш-модификаторов, с фокусировкой на элементах страницы (Widget)
//На примере кнопки
//Указывается способ, как найти целевые виджеты
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static org.openqa.selenium.Keys.ALT;
import static org.openqa.selenium.Keys.CONTROL;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.keyUp;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(keyUp(button("Push me"), CONTROL))
                .perform(keyUp(button("Push me"), ALT));
        //....        
    }
}
```
---

## Отправка символов/клавиш активному элементу:

```java
//пример: простая отправка символов/клавиш уже активному элементу
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.Keys.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.sendKeys;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(sendKeys(chord(CONTROL, "A", BACK_SPACE), "Some string"));
        //....        
    }
}
```
---
```java
//пример: отправка символов/клавиш, с фокусировкой на элементах страницы (WebElement)
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static org.openqa.selenium.Keys.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.sendKeys;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var webElement = seleniumSteps.find(webElement(tagName("textarea")));
        seleniumSteps.perform(sendKeys(webElement, chord(CONTROL, "A", BACK_SPACE), "Some string"));
        //....        
    }
}
```
---

```java
//пример: отправка символов/клавиш, с фокусировкой на элементах страницы (Widget)
//На примере текстового поля
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.Keys.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.sendKeys;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var textField = seleniumSteps.find(textField("Fill me"));
        seleniumSteps.perform(sendKeys(textField, chord(CONTROL, "A", BACK_SPACE), "Some string"));
        //....        
    }
}
```
---

```java
//пример: отправка символов/клавиш, с фокусировкой на элементах страницы (WebElement)
//Указывается способ, как найти целевые элементы
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static org.openqa.selenium.Keys.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.sendKeys;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(sendKeys(webElement(tagName("textarea")), chord(CONTROL, "A", BACK_SPACE), "Some string"));
        //....        
    }
}
```
---

```java
//пример: отправка символов/клавиш, с фокусировкой на элементах страницы (Widget)
//На примере текстового поля
//Указывается способ, как найти целевые виджеты
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.Keys.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.sendKeys;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(sendKeys(textField("Fill me"), chord(CONTROL, "A", BACK_SPACE), "Some string"));
        //....        
    }
}
```
---

## Клик мышью с задержкой:

```java
//пример: простой клик мышью с задержкой
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.clickAndHold;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(clickAndHold());
        //....        
    }
}
```
---
```java
//пример: простой клик мышью с задержкой, по элементам страницы (WebElement)
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.clickAndHold;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var webElement = seleniumSteps.find(webElement(tagName("a")));
        seleniumSteps.perform(clickAndHold(webElement));
        //....        
    }
}
```
---

```java
//пример: простой клик мышью с задержкой, по элементам страницы (Widget)
//На примере ссылки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.clickAndHold;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var link = seleniumSteps.find(link("Click me"));
        seleniumSteps.perform(clickAndHold(link));
        //....        
    }
}
```
---

```java
//пример: простой клик мышью с задержкой, по элементам страницы (WebElement)
//Указывается способ, как найти целевые элементы
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.clickAndHold;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(clickAndHold(webElement(tagName("a"))));
        //....        
    }
}
```
---

```java
//пример: простой клик мышью с задержкой, по элементам страницы (Widget)
//На примере ссылки
//Указывается способ, как найти целевые виджеты
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.clickAndHold;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(clickAndHold(link("Click me")));
        //....        
    }
}
```
---
























































































## Клик мышью с задержкой:

```java
//пример: простой клик мышью с задержкой
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.clickAndHold;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(clickAndHold());
        //....        
    }
}
```
---
```java
//пример: простой клик мышью с задержкой, по элементам страницы (WebElement)
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.clickAndHold;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var webElement = seleniumSteps.find(webElement(tagName("a")));
        seleniumSteps.perform(clickAndHold(webElement));
        //....        
    }
}
```
---

```java
//пример: простой клик мышью с задержкой, по элементам страницы (Widget)
//На примере ссылки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.clickAndHold;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var link = seleniumSteps.find(link("Click me"));
        seleniumSteps.perform(clickAndHold(link));
        //....        
    }
}
```
---

```java
//пример: простой клик мышью с задержкой, по элементам страницы (WebElement)
//Указывается способ, как найти целевые элементы
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.clickAndHold;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(clickAndHold(webElement(tagName("a"))));
        //....        
    }
}
```
---

```java
//пример: простой клик мышью с задержкой, по элементам страницы (Widget)
//На примере ссылки
//Указывается способ, как найти целевые виджеты
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.clickAndHold;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(clickAndHold(link("Click me")));
        //....        
    }
}
```
---


---
См. также [Шаги, выполняющие действие](/doc/rus/core/Steps.md#Шаги,-выполняющие-действие)

