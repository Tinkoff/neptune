# Поиск элементов страницы

Данный модуль позволяет описывать взаимодействие с элементами страницы как посредством [WebElement](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/WebElement.html) так и [используя шаблон проектирования Page Object](https://www.seleniumhq.org/docs/06_test_design_considerations.jsp#page-object-design-pattern).
Поиск элементов является основным шагом перед выполнением различных интерактивных действий на странице.

## Инструменты поиска элементов страницы

См. [как построить цепочки шагов, возвращающие результат](/doc/rus/core/Steps.md#Построение-цепочек-шагов,-возвращающих-результат). 
Используются в связке с [Контекстом шагов для Selenium](/doc/rus/selenium/SeleniumStepContext.md).

## Поиск одного элемента

Для поиска одного элемента используется [SearchSupplier](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/SearchSupplier.html)

| Возможность использовать критерии 	| Объединение критериев 	| Возможность указывать индивидуальный таймаут 	| Если желаемое значение не получено                                   	| Игнорируемые исключения                            	| Данные, которые могут быть  приложены к отчетам (по умолчанию)  	|
|-----------------------------------	|-----------------------	|----------------------------------------------	|----------------------------------------------------------------------	|----------------------------------------------------	|-----------------------------------------------------------------	|
|            Да                     	| AND                   	|                Да                          	| По умолчанию выбрасывает  org.openqa.selenium.NoSuchElementException 	| org.openqa.selenium.StaleElementReferenceException 	| Скриншоты. Прочие файлы                                          	|

---

```java
//пример поиска элемента
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass")));
        //..
    }
}
```

В примере выше происходит обычный поиск элемента по классу (аттрибут html). 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

---

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
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"), "МуText"));
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html) и его полному тексту. 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

---

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
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"), compile("МуTextPattern")));
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html) и его тексту, соответствующему регулярному выражению. 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

---

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
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"))
                .criteria(shouldBeEnabled()));        
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html) и по условию `элемент должен быть доступен`. 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).

---

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
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"))
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100));      
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html) и условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ). 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

---

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
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"), "МуText")
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100));      
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html), условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ) и его полному тексту. 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

---

```java
//пример поиска элемента по нескольким критериям и регулярному выражению
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
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"), compile("МуTextPattern"))
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100));      
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html), условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ) и его тексту, соответствующему регулярному выражению. 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

---

```java
//пример поиска элемента, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"))
                .timeOut(ofSeconds(5)));
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html). Поиск занимает 5 секунд. Может производиться поиск любого(видимого) подходящего элемента. 
[см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

---

```java
//пример поиска элемента по тексту, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"), "МуText")
                .timeOut(ofSeconds(5)));
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html) и его полному тексту. Поиск занимает 5 секунд. 
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

---

```java
//пример поиска элемента по регулярному выражению, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static java.util.regex.Pattern.compile;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"), compile("МуTextPattern"))
                .timeOut(ofSeconds(5)));
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html)  и его тексту, соответствующему регулярному выражению. Поиск занимает 5 секунд. 
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

---

```java
//пример поиска элемента по критерию, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"))
                .criteria(shouldBeEnabled())
                .timeOut(ofSeconds(5)));                       
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html) и по условию `элемент должен быть доступен`.  Поиск занимает 5 секунд.
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).

---

```java
//пример поиска элемента по нескольким критериям, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"))
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100)
                .timeOut(ofSeconds(5)));                       
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html), условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ). 
Поиск занимает 5 секунд. 
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

---

```java
//пример поиска элемента по нескольким критериям и тексту элемента, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"), "МуText")
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100)   
                .timeOut(ofSeconds(5)));            
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html), условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ) и его полному тексту. 
Поиск занимает 5 секунд. 
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

---

```java
//пример поиска элемента по нескольким критериям и регулярному выражению, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static java.util.regex.Pattern.compile;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"), compile("МуTextPattern"))
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100)
                .timeOut(ofSeconds(5)));              
        //..
    }
}
```

В примере выше происходит поиск элемента по классу (аттрибут html), условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ) и его тексту, соответствующему регулярному выражению. 
Поиск занимает 5 секунд. 
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

---

```java
//пример, как игнорировать NoSuchElementException. Вернется null
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import java.util.NoSuchElementException;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"))
                .addIgnored(NoSuchElementException.class));
        //..
    }
}
```

В примере выше происходит обычный поиск элемента по классу (аттрибут html). 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любого(видимого) подходящего элемента. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 
Если ничего не будет найдено, то вместо выбрасывания `NoSuchElementException` поиск возвращает `null`.

---

## Поиск множества элементов

Для поиска множества элементов используется [MultipleSearchSupplier](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/MultipleSearchSupplier.html)

| Возможность использовать критерии 	| Объединение критериев 	| Возможность указывать индивидуальный таймаут 	| Если желаемое значение не получено 	| Игнорируемые исключения                            	| Данные, которые могут быть  приложены к отчетам (по умолчанию)  	|
|-----------------------------------	|-----------------------	|----------------------------------------------	|------------------------------------	|----------------------------------------------------	|-----------------------------------------------------------------	|
|     Да                            	| AND                   	|        Да                                    	| Возвращает пустой List             	| org.openqa.selenium.StaleElementReferenceException 	| Скриншоты Прочие файлы                                          	|

---

```java
//пример поиска элементов
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass")));
        //..
    }
}
```

В примере выше происходит обычный поиск элементов по классу (аттрибут html). 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

---

```java
//пример поиска элементов по тексту
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"), "МуText"));
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html) и их полному тексту. 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

---

```java
//пример поиска элементов по регулярному выражению
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.util.regex.Pattern.compile;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"), compile("МуTextPattern")));
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html) и их тексту, соответствующему регулярному выражению. 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

---

```java
//пример поиска элементов по критерию
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"))
                .criteria(shouldBeEnabled()));        
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html) и по условию `элемент должен быть доступен`. 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).

---

```java
//пример поиска элементов по нескольким критериям
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"))
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100));      
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html) и условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ). 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

---

```java
//пример поиска элементов по нескольким критериям и тексту элемента
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"), "МуText")
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100));      
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html), условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ) и их полному тексту. 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

---

```java
//пример поиска элементов по нескольким критериям и регулярному выражению
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.util.regex.Pattern.compile;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"), compile("МуTextPattern"))
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100));      
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html), условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ) и их тексту, соответствующему регулярному выражению. 
Поиск занимает 1 минуту или время, указанное в [настройках](/doc/rus/selenium/Settings.md#Время-ожидания-элементов-на-странице-(не-обязательно)).
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

---

```java
//пример поиска элементов, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"))
                .timeOut(ofSeconds(5)));
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html). Поиск занимает 5 секунд. Может производиться поиск любых(видимых) подходящих элементов. 
[см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

---

```java
//пример поиска элементов по тексту, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"), "МуText")
                .timeOut(ofSeconds(5)));
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html) и их полному тексту. Поиск занимает 5 секунд. 
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

---

```java
//пример поиска элементов по регулярному выражению, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static java.util.regex.Pattern.compile;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"), compile("МуTextPattern"))
                .timeOut(ofSeconds(5)));
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html) и их тексту, соответствующему регулярному выражению. Поиск занимает 5 секунд. 
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)) 

---

```java
//пример поиска элементов по критерию, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"))
                .criteria(shouldBeEnabled())
                .timeOut(ofSeconds(5)));                       
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html) и по условию `элемент должен быть доступен`.  Поиск занимает 5 секунд.
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).

---

```java
//пример поиска элементов по нескольким критериям, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"))
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100)
                .timeOut(ofSeconds(5)));                       
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html), условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ). 
Поиск занимает 5 секунд. 
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

---

```java
//пример поиска элементов по нескольким критериям и тексту элемента, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"), "МуText")
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100)   
                .timeOut(ofSeconds(5)));            
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html), условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ) и их полному тексту. 
Поиск занимает 5 секунд. 
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

---

```java
//пример поиска элементов по нескольким критериям и регулярному выражению, c указанием отведенного времени
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static java.util.regex.Pattern.compile;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"), compile("МуTextPattern"))
                .criteria(shouldBeEnabled())
                .criteria("Элеменент по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100)
                .timeOut(ofSeconds(5)));              
        //..
    }
}
```

В примере выше происходит поиск элементов по классу (аттрибут html), условию (`элемент должен быть доступен` **И** `Элеменент по оси Y выше чем 100` ) и их тексту, соответствующему регулярному выражению. 
Поиск занимает 5 секунд. 
Может производиться поиск любых(видимых) подходящих элементов. [см. поиск только видимых элементов](/doc/rus/selenium/Settings.md#Поиск-только-видимых-элементов-(не-обязательно)). 
В данном примере используется один из критериев, перечисленных в [CommonConditions](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/CommonConditions.html).
Второй критерий определен пользователем.

---


## Предлагаемое использование шаблона проектирования Page Object

Предлагается не описывать всю страницу целиком, а использовать типизированные элементы/структуры элементов. 
В рамках данного модуля введено такое понятие, как [виджет](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/Widget.html). 

### Как создать виджет 

Виджет типизирует элемент/группу элементов и описывает его поведение с точки зрения интерактивного взаимодействия с ним. 
По умолчанию, модуль содержит интерфейсы, кототорые позволяют описать наиболее частые варианты интерактивного поведения элемента страницы:
  - [Clickable](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/Clickable.html) - виджеты, по котором можно выполнять клик.
  Кнопки, ссылки и т.д.
  - [Editable](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/Editable.html) - виджеты, значения которых можно редактировать.
  Текстовые поля, флаги (чекбоксы, радиобаттоны), селекторы и т.д.
  - [HasValue](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/HasValue.html) - Виджеты, которое могут возвращать значения.
  Текст в текстовом поле, включенность флага, записи в таблице и т.д.
  - [Labeled](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/Labeled.html) - для того, чтобы виджет можно было найти по меткам - 
  тексту/значению актрибута самого элемента или теккстам/значениям атрибутов связанных элементов.   
  
[Модуль содержит наиболее часто используемые варианты типизированных элементов/виджетов. Абстрактные классы](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/drafts/package-summary.html)

```java
//простой пример
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

@Priority(1) //приоритет поиска. указывать не обязательно
@Name("Мой виджет") //отображаемое имя. указывать не обязательно
@FindBy(xpath = ".//path/to/element") //локатор, по которому ищется элемент. Путь к элементу, 
//от которого ищутся под-элементы виджета/относительно которого ищутся связанные элементы
public class MyCustomWidget extends Widget  {
    public MyCustomWidget(WebElement wrappedElement) {
        super(wrappedElement);
    }

    //....
}
``` 

#### Аннотация [@Name](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/Name.html) 
Нужна для названия виджета/текстового пояснения виджета. 

Указанное значение может наследоваться

```java
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

@Name("Мой виджет") //вызов toString у объекта MyCustomWidget дасть результат
//`Мой виджет` + пояснение, по какому критерию найден
@FindBy(xpath = ".//path/to/element")
public class MyCustomWidget extends Widget  {
    public MyCustomWidget(WebElement wrappedElement) {
        super(wrappedElement);
    }

    //....
}
``` 

```java
//вызов toString у объекта MyCustomWidget дасть результат
//`Мой виджет` + пояснение, по какому критерию найден
@FindBy(xpath = ".//path/to/element2")
public class MyCustomWidget2 extends MyCustomWidget  {
    public MyCustomWidget2(WebElement wrappedElement) {
        super(wrappedElement);
    }

    //....
}
```

Указанное значение может перекрываться

```java
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;

@Name("Мой новый виджет") //вызов toString у объекта MyCustomWidget дасть результат
//`Мой новый виджет` + пояснение, по какому критерию найден
@FindBy(xpath = ".//path/to/element3")
public class MyCustomWidget3 extends MyCustomWidget  {
    public MyCustomWidget3(WebElement wrappedElement) {
        super(wrappedElement);
    }

    //....
}
```

#### Аннотация [@Priority](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/Priority.html) 
Указывает приоритет поиска виджета определенного типа среди прочих однотипных. Чем ниже значение, тем выше приоритет. Указанные значения
должны быть >= 1.

Например 

```java
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

@FindBy(tagName = "button")
public class CommonButton extends Button {
    public CommonButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    @Override
    public void click() {
        //реализация
    }

    //....
}

@Priority(1) //означает, что элементы этого класса
//более приоритетны для поиска, чем элементы класса CommonButton
@FindBy(className = "MyButton")
public class MyButton extends Button {
    public MyButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    @Override
    public void click() {
        //реализация
    }

    //....
}
```

Указанное значение может наследоваться

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

@Priority(1)
@FindBy(tagName = "button")
public class CommonButton extends Button {
    public CommonButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    @Override
    public void click() {
        //реализация
    }

    //....
}

@FindBy(className = "MyButton") /*при поиске 
кнопок, те кнопки, что описаны классами 
CommonButton и MyButton имеют одинаковый приоритет для механизма 
поиска
*/
public class MyButton extends CommonButton {
    public MyButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
    //....
}
```

Указанное значение может перекрываться

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

@Priority(1)
@FindBy(tagName = "button")
public class CommonButton extends Button {
    public CommonButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    @Override
    public void click() {
        //реализация
    }

    //....
}

@Priority(2)
@FindBy(className = "MyButton") /*при поиске 
кнопок, те кнопки, что описаны классом
MyButton имеют меньший приоритет для механизма 
поиска чем кнопки, описанные CommonButton
*/
public class MyButton extends CommonButton {
    public MyButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
    //....
}
```

См. [Поиск виджетов](#Поиск-виджетов)

#### Аннотации [@FindBy](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/support/FindBy.html), [@FindBys](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/support/FindBys.html), [@FindAll](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/support/FindAll.html). 

[Стандартные пример использования](https://github.com/SeleniumHQ/selenium/wiki/PageFactory#making-the-example-work-using-annotations).

```java
//простой пример
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

@FindBy(xpath = ".//path/to/element") //Путь к элементу, от которого строится виджет
public class MyCustomWidget extends Widget  {
    
    //Путь к элементу, вложенному в элемент, от которого строится виджет,
    //или располагающемуся отностельно элемента, от которого строится виджет
    @FindBy(xpath = ".//path/to/inner/or/relative/element")
    private WebElement innerOrRelativeElement;
    
    public MyCustomWidget(WebElement wrappedElement) {
        super(wrappedElement);
    }

    //....
}
```

Никаких дополнительных действий выполнять не нужно.

Указанное значение может перекрываться

```java
//простой пример
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

@FindBy(tagName = "button")
public class CommonButton extends Button {
    public CommonButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    @Override
    public void click() {
        //реализация
    }

    //....
}

//Указанное ниже значение перекрывает @FindBy(tagName = "button")
@FindBys({@FindBy(tagName = "form"), @FindBy(className = "MyButton")})
public class MyButton extends CommonButton {
    public MyButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
```

Наследовать поисковый локатор элемента, от которого строится виджет, **ЗАПРЕШЕНО!**.

```java
//простой пример
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

@FindBy(tagName = "button")
public class CommonButton extends Button {
    public CommonButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    @Override
    public void click() {
        //реализация
    }

    //....
}

//Так делать некорректно.
public class MyButton extends CommonButton {
    public MyButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
```

Но можно наследовать поведение виджета. Например, если все кнопки на страницах приложения ведут себя одинаково и разница 
только в поисковых локаторах, то см. пример ниже

```java
//простой пример
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

public abstract class AbstractButton extends Button {
    public AbstractButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    @Override
    public void click() {
        //реализация
    }

    //....
}

@FindBy(tagName = "button")
public class CommonButton extends AbstractButton {
    public CommonButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
}

@FindBys({@FindBy(tagName = "form"), @FindBy(className = "MyButton")})
public class MyButton extends AbstractButton {
    public MyButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
```

Строго рекомендуется обеспечить уникальность поисковых локаторов элементов, от которых строятся виджеты.

### Поиск виджетов

Аналогичен [поиску одного элемента страницы](#Поиск-одного-элемента) и [поиску множества элементов страницы](#Поиск-множества-элементов).