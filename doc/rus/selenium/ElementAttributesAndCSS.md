# Атрибуты и CSS

См. [Поиск элементов страницы](/doc/rus/selenium/SearchingForElements.md)

См. [Предлагаемое использование шаблона проектирования Page Object](/doc/rus/selenium/SearchingForElements.md#Предлагаемое-использование-шаблона-проектирования-Page-Object)

## Атрибуты

Для получения значений атрибутов элементов используется [SequentialGetAttributeValueSupplier](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/value/SequentialGetAttributeValueSupplier.html).

| [Возможность использовать критерии](/doc/rus/core/Steps.md#Критерии)| [Объединение критериев](/doc/rus/core/Steps.md#Объединение-критериев)| [Возможность указывать индивидуальный таймаут](/doc/rus/core/Steps.md#Время-на-получение-значимого-результата)| Если желаемое значение не получено | [Игнорируемые исключения](/doc/rus/core/Steps.md#Игнорирование-выбрасываемых-исключений)| [Данные, которые могут быть  приложены к отчетам (по умолчанию)](/doc/rus/core/Steps.md#Данные,-которые-могут-быть-приложены-к-отчетам/логу)|
|-----------------------------------	|-----------------------	|----------------------------------------------	|----------------------------------------------------------------------	|----------------------------------------------------	|-----------------------------------------------------------------	|
|            НЕТ                     	| -             	|                НЕТ                         	| Возвращает null                                                      	| НЕТ 	                                                | Любые                                       	        |


### Получение атрибута объекта WebElement

#### C поиском элемента

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetAttributeValueSupplier.attributeValue;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var attrValue = seleniumSteps.get(attributeValue("attr_name").of(webElement(tagName("button"))));   
        //элемент еще не найден. Его следует найти перед получением значения аттрибута
        //..
    }
}
```

#### От уже найденного элемента

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetAttributeValueSupplier.attributeValue;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var buttonElement = seleniumSteps.find(webElement(tagName("button")));
        var attrValue = seleniumSteps.get(attributeValue("attr_name").of(buttonElement));   
        //элемент уже найден.
        //..
    }
}
```

### Получение атрибута виджета

#### C поиском виджета

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetAttributeValueSupplier.attributeValue;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var attrValue = seleniumSteps.get(attributeValue("attr_name").of(button("Click me")));   
        //виджет-кнопка еще не найден. Его следует найти перед получением значения аттрибута
        //..
    }
}
```

#### От уже найденного виджета

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetAttributeValueSupplier.attributeValue;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var button = seleniumSteps.find(button("Click me"));
        var attrValue = seleniumSteps.get(attributeValue("attr_name").of(button));   
        //виджет-кнопка уже найден
        //..
    }
}
```

---
См. также [Шаги возвращающие результат](/doc/rus/core/Steps.md#Шаги-возвращающие-результат)

См. также [Построение цепочек шагов, возвращающих результат](/doc/rus/core/Steps.md#Построение-цепочек-шагов,-возвращающих-результат)

### Проверка значения атрибута

Модуль содержит матчер, который позволяет проверять значения атрибутов элементов [HasAttributeMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/HasAttributeMatcher.html).

```java
//на примере WebElement
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasAttributeMatcher.hasAttribute;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(tagName("button"))), 
                  hasAttribute("attr_name", "Attribute value"));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasAttributeMatcher.hasAttribute;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(tagName("button"))), 
                  hasAttribute("attr_name", containsString("value")));
        //..
    }
}
```

```java
//на примере виджета-кнопки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasAttributeMatcher.hasAttribute;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(button("Click me")), 
                  hasAttribute("attr_name", "Attribute value"));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasAttributeMatcher.hasAttribute;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(button("Click me")),
                  hasAttribute("attr_name", containsString("value")));
        //..
    }
}
```

---
См. также [check](/doc/rus/check/Main.md)

См. также [hamcrest.org](http://hamcrest.org/JavaHamcrest/)

## CSS


Для получения значений CSS элементов используется [SequentialGetCSSValueSupplier](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/value/SequentialGetCSSValueSupplier.html).

| [Возможность использовать критерии](/doc/rus/core/Steps.md#Критерии)| [Объединение критериев](/doc/rus/core/Steps.md#Объединение-критериев)| [Возможность указывать индивидуальный таймаут](/doc/rus/core/Steps.md#Время-на-получение-значимого-результата)| Если желаемое значение не получено | [Игнорируемые исключения](/doc/rus/core/Steps.md#Игнорирование-выбрасываемых-исключений)| [Данные, которые могут быть  приложены к отчетам (по умолчанию)](/doc/rus/core/Steps.md#Данные,-которые-могут-быть-приложены-к-отчетам/логу)|
|-----------------------------------	|-----------------------	|----------------------------------------------	|----------------------------------------------------------------------	|----------------------------------------------------	|-----------------------------------------------------------------	|
|            НЕТ                     	| -             	|                НЕТ                         	| Возвращает null                                                      	| НЕТ 	                                                | Любые                                       	        |


### Получение css объекта WebElement

#### C поиском элемента

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetCSSValueSupplier.cssValue;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var cssValue = seleniumSteps.get(cssValue("css_name").of(webElement(tagName("button"))));   
        //элемент еще не найден. Его следует найти перед получением значения css
        //..
    }
}
```

#### От уже найденного элемента

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetCSSValueSupplier.cssValue;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var buttonElement = seleniumSteps.find(webElement(tagName("button")));
        var cssValue = seleniumSteps.get(cssValue("css_name").of(buttonElement));   
        //элемент уже найден.
        //..
    }
}
```

### Получение css виджета

#### C поиском виджета

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetCSSValueSupplier.cssValue;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var cssValue = seleniumSteps.get(cssValue("css_name").of(button("Click me")));   
        //виджет-кнопка еще не найден. Его следует найти перед получением значения css
        //..
    }
}
```

#### От уже найденного виджета

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetCSSValueSupplier.cssValue;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var button = seleniumSteps.find(button("Click me"));
        var cssValue = seleniumSteps.get(cssValue("css_name").of(button));   
        //виджет-кнопка уже найден.
        //..
    }
}
```

---
См. также [Шаги возвращающие результат](/doc/rus/core/Steps.md#Шаги-возвращающие-результат)

См. также [Построение цепочек шагов, возвращающих результат](/doc/rus/core/Steps.md#Построение-цепочек-шагов,-возвращающих-результат)

### Проверка css

Модуль содержит матчер, который позволяет проверять значения css элементов [HasCssValueMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/HasCssValueMatcher.html).

```java
//на примере WebElement
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasCssValueMatcher.hasCss;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(tagName("button"))), 
                  hasCss("css_name", "CSS value"));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasCssValueMatcher.hasCss;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(tagName("button"))), 
                  hasCss("css_name", containsString("value")));
        //..
    }
}
```

```java
//на примере виджета-кнопки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasCssValueMatcher.hasCss;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(button("Click me")), 
                  hasCss("css_name", "CSS value"));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasCssValueMatcher.hasCss;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(button("Click me")),
                  hasCss("css_name", containsString("value")));
        //..
    }
}
```

---
См. также [check](/doc/rus/check/Main.md)

См. также [hamcrest.org](http://hamcrest.org/JavaHamcrest/)









