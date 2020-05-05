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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.keyDown;

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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.keyDown;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.keyDown;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.keyDown;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.keyDown;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.keyUp;

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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.keyUp;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.keyUp;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.keyUp;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.keyUp;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.sendKeys;

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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.sendKeys;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.sendKeys;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.sendKeys;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.sendKeys;
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

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.clickAndHold;

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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.clickAndHold;
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

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.clickAndHold;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.clickAndHold;
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

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.clickAndHold;
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

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.clickAndHold;

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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.clickAndHold;
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

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.clickAndHold;
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
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.clickAndHold;
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

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.clickAndHold;
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

## Освобождение мыши:

```java
//пример: освобождение левой кнопки мыши. Текущая позиция мыши.
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.release;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(release());
        //....        
    }
}
```
---
```java
//пример: освобождение левой кнопки мыши. Элемент страницы (WebElement)
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.release;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var webElement = seleniumSteps.find(webElement(tagName("a")));
        seleniumSteps.perform(release(webElement));
        //....        
    }
}
```
---

```java
//пример: освобождение левой кнопки мыши. Элемент страницы (Widget)
//На примере ссылки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.release;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var link = seleniumSteps.find(link("Click me"));
        seleniumSteps.perform(release(link));
        //....        
    }
}
```
---

```java
//пример: освобождение левой кнопки мыши. Элемент страницы (WebElement)
//Указывается способ, как найти целевые элементы
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.release;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(release(webElement(tagName("a"))));
        //....        
    }
}
```
---

```java
//пример: освобождение левой кнопки мыши. Элемент страницы (Widget)
//На примере ссылки
//Указывается способ, как найти целевые виджеты
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.release;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(release(link("Click me")));
        //....        
    }
}
```
---

## Клик левой кнопкой мыши:

```java
//пример: Клик левой кнопкой мыши. Текущая позиция мыши.
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.click;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(click());
        //....        
    }
}
```
---
```java
//пример: Клик левой кнопкой мыши. Элемент страницы (WebElement)
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.click;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var webElement = seleniumSteps.find(webElement(tagName("a")));
        seleniumSteps.perform(click(webElement));
        //....        
    }
}
```
---

```java
//пример: Клик левой кнопкой мыши. Элемент страницы (Widget)
//На примере ссылки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.click;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var link = seleniumSteps.find(link("Click me"));
        seleniumSteps.perform(click(link));
        //....        
    }
}
```
---

```java
//пример: Клик левой кнопкой мыши. Элемент страницы (WebElement)
//Указывается способ, как найти целевые элементы
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.click;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(click(webElement(tagName("a"))));
        //....        
    }
}
```
---

```java
//пример: Клик левой кнопкой мыши. Элемент страницы (Widget)
//На примере ссылки
//Указывается способ, как найти целевые виджеты
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.click;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(click(link("Click me")));
        //....        
    }
}
```
---

## Движение мыши:

```java
//пример: Движение мыши к элементу (верхнему левому углу).
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.moveToElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var webElement = seleniumSteps.find(webElement(tagName("a")));
        seleniumSteps.perform(moveToElement(webElement));
        //....        
    }
}
```
---

```java
//пример: Движение мыши к элементу (относительно его верхнего левого угла).
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.moveToElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var webElement = seleniumSteps.find(webElement(tagName("a")));
        seleniumSteps.perform(moveToElement(webElement, 
                10, //отклонение по оси X
                10)); //отклонение по оси Y
        //....        
    }
}
```
---

```java
//пример: Движение мыши к элементу (верхнему левому углу).
//Указывается способ, как найти целевые элементы
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.moveToElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(moveToElement(webElement(tagName("a"))));
        //....        
    }
}
```
---

```java
//пример: Движение мыши к элементу (относительно его верхнего левого угла).
//Указывается способ, как найти целевые элементы
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.moveToElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(moveToElement(webElement(tagName("a")), 
                10, //отклонение по оси X
                10)); //отклонение по оси Y
        //....        
    }
}
```
---

```java
//пример: Движение мыши к виджету (его верхнему левому углу или верхнему левому углу элемента, 
// относительно которого строится описание виджета).
//Указывается способ, как найти целевые элементы
//На примере ссылки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.moveToElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(moveToElement(link("Click me")));
        //....        
    }
}
```
---

```java
//пример: Движение мыши к виджету (относительно его верхнего левого угла или верхнего левого угла элемента, 
// относительно которого строится описание виджета).
//Указывается способ, как найти целевые элементы
//На примере ссылки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.moveToElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(moveToElement(link("Click me"),
                10, //отклонение по оси X
                10)); //отклонение по оси Y
        //....        
    }
}
```
---

```java
//пример: Движение мыши относительно текущей позиции/левого верхнего угла страницы
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.moveByOffset;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(moveByOffset(10, //отклонение по оси X
                10)); //отклонение по оси Y
        //....        
    }
}
```
---

## Клик правой кнопкой мыши (контекстный клик):

```java
//пример: Клик правой кнопкой мыши (контекстный клик). Текущая позиция мыши.
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.contextClick;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(contextClick());
        //....        
    }
}
```
---
```java
//пример: Клик правой кнопкой мыши (контекстный клик). Элемент страницы (WebElement)
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.contextClick;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var webElement = seleniumSteps.find(webElement(tagName("a")));
        seleniumSteps.perform(contextClick(webElement));
        //....        
    }
}
```
---

```java
//пример: Клик правой кнопкой мыши (контекстный клик). Элемент страницы (Widget)
//На примере ссылки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.contextClick;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var link = seleniumSteps.find(link("Click me"));
        seleniumSteps.perform(contextClick(link));
        //....        
    }
}
```
---

```java
//пример: Клик правой кнопкой мыши (контекстный клик). Элемент страницы (WebElement)
//Указывается способ, как найти целевые элементы
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.contextClick;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(contextClick(webElement(tagName("a"))));
        //....        
    }
}
```
---

```java
//пример: Клик правой кнопкой мыши (контекстный клик). Элемент страницы (Widget)
//На примере ссылки
//Указывается способ, как найти целевые виджеты
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.contextClick;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(contextClick(link("Click me")));
        //....        
    }
}
```
---







































## Клик правой кнопкой мыши (контекстный клик):

```java
//пример: Клик правой кнопкой мыши (контекстный клик). Текущая позиция мыши.
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.contextClick;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(contextClick());
        //....        
    }
}
```
---
```java
//пример: Клик правой кнопкой мыши (контекстный клик). Элемент страницы (WebElement)
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.contextClick;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var webElement = seleniumSteps.find(webElement(tagName("a")));
        seleniumSteps.perform(contextClick(webElement));
        //....        
    }
}
```
---

```java
//пример: Клик правой кнопкой мыши (контекстный клик). Элемент страницы (Widget)
//На примере ссылки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.contextClick;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        var link = seleniumSteps.find(link("Click me"));
        seleniumSteps.perform(contextClick(link));
        //....        
    }
}
```
---

```java
//пример: Клик правой кнопкой мыши (контекстный клик). Элемент страницы (WebElement)
//Указывается способ, как найти целевые элементы
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.contextClick;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(contextClick(webElement(tagName("a"))));
        //....        
    }
}
```
---

```java
//пример: Клик правой кнопкой мыши (контекстный клик). Элемент страницы (Widget)
//На примере ссылки
//Указывается способ, как найти целевые виджеты
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction_Deprecated.contextClick;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //....
        seleniumSteps.perform(contextClick(link("Click me")));
        //....        
    }
}
```
---


См. также [Шаги, выполняющие действие](/doc/rus/core/Steps.md#Шаги,-выполняющие-действие)

