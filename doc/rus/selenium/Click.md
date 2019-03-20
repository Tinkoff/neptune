# Клик по элементу

См. [Поиск элементов страницы](/doc/rus/selenium/SearchingForElements.md)

Для выполнения кликов по элементам используется [ClickActionSupplier](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/click/ClickActionSupplier.html)

| [Данные, которые могут быть приложены к отчетам (по умолчанию)](/doc/rus/core/Steps.md#Данные,-которые-могут-быть-приложены-к-отчетам-и-логу) 	|
|-----------------------------------------------------------------	|
| Скриншоты Прочие файлы                                          	|

## Клик по WebElement

### Клик с поиском элемента

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        seleniumSteps.click(on(webElement(tagName("button"))));   
        //элемент еще не найден. Его следует найти перед выполнением клика
        //..
    }
}
```

### Клик по уже найденному элементу

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElement = seleniumSteps.find(webElement(tagName("button")));
        seleniumSteps.click(on(webElement));   
        //элемент уже найден перед выполнением клика
        //..
    }
}
```

## Клик по виджету

См. [Предлагаемое использование шаблона проектирования Page Object](/doc/rus/selenium/SearchingForElements.md#Предлагаемое-использование-шаблона-проектирования-Page-Object)

Для того, чтобы описать клик по элементу-виджету, нужно чтобы класс, который описывает виджет, реализовывал интерфейс [Clickable](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/Clickable.html):

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.Clickable;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;
import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.widget;

@FindBy(xpath = "/path/to/clickable/element")
public class MyClickableWidget extends Widget implements Clickable {

    public MyClickableWidget(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    @Override
    public void click() {
        //реализация
    }    
}

//и тогда
public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        seleniumSteps.click(on(widget(MyClickableWidget.class)));      
    }
}
```

### Клик с поиском виджета

```java
//на примере клика по ссылке
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        seleniumSteps.click(on(link("Click me")));   
        //ссылка еще не найдена. Ее следует найти перед выполнением клика
        //..
    }
}
```

### Клик по уже найденному виджету


```java
//на примере клика по ссылке
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var clickMeLink = seleniumSteps.find(link("Click me"));
        seleniumSteps.click(on(clickMeLink));   
        //ссылка уже найдена перед выполнением клика
        //..
    }
}
```

## Цепочки

Клики можно объединять в цепочки одним из следующих способов

```java
//простой последовательный вызов метода .click
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
       seleniumSteps.click(on(link("Click me")))
          .click(on(button("Click me then"))); 
    }
} 
```

```java
//вызов метода .click один раз
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
       seleniumSteps.click(on(link("Click me"))
          .andOn(button("Click me then"))); 
    }
} 
```

Второй способ позволяет записывать цепочку кликов в одну переменную/поле класса и т.п. Дает интересные возможности, например, 
для параметризации тестов.

```java
//вызов метода .click один раз
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
       var clickSequence = on(link("Click me"))
                 .andOn(button("Click me then"));
       seleniumSteps.click(clickSequence); 
    }
} 
```

---
См. также [Шаги, выполняющие действие](/doc/rus/core/Steps.md#Шаги,-выполняющие-действие)