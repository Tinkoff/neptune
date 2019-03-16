# Поиск элементов страницы

Данный модуль позволяет описывать взаимодействие с элементами страницы как посредством [WebElement](https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/WebElement.html) так и [используя шаблон проектирования Page Object](https://www.seleniumhq.org/docs/06_test_design_considerations.jsp#page-object-design-pattern).
Поиск элементов является основным шагом перед выполнением различных интерактивных действий на странице.

## Инструменты поиска элементов страницы

См. [как построить цепочки шагов, возвращающие результат](/doc/rus/core/Steps.md#Построение-цепочек-шагов,-возвращающих-результат). 
Используются в связке с [Контекстом шагов для Selenium](/doc/rus/selenium/SeleniumStepContext.md).

### Поиск одного элемента

Для поиска одного элемента используется [SearchSupplier](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/SearchSupplier.html)

| Возможность использовать критерии 	| Объединение критериев 	| Возможность указывать индивидуальный таймаут 	| Если желаемое значение не получено                                   	| Игнорируемые исключения                            	| Данные, которые могут быть  приложены к отчетам (по умолчанию)  	|
|-----------------------------------	|-----------------------	|----------------------------------------------	|----------------------------------------------------------------------	|----------------------------------------------------	|-----------------------------------------------------------------	|
|            Да                     	| AND                   	|                Да                          	| По умолчанию выбрасывает  org.openqa.selenium.NoSuchElementException 	| org.openqa.selenium.StaleElementReferenceException 	| Скриншоты. Прочие файлы                                          	|

---

#### Простой поиск элемента 

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

#### Поиск элемента по тексту 

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

#### Поиск элемента по регулярному выражению 

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

#### Поиск элемента по критерию 

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

#### Поиск элемента по нескольким критериям

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

#### Поиск элемента по нескольким критериям и тексту элемента

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

#### Поиск элемента по нескольким критериям и регулярному выражению

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

#### Поиск элемента c указанием отведенного времени

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

#### Поиск элемента по тексту, c указанием отведенного времени

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

#### Поиск элемента по регулярному выражению, c указанием отведенного времени

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

#### Поиск элемента по критерию, c указанием отведенного времени

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

#### Поиск элемента по нескольким критериям, c указанием отведенного времени

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

#### Поиск элемента по нескольким критериям и тексту элемента, c указанием отведенного времени

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

#### Поиск элемента по нескольким критериям и регулярному выражению, c указанием отведенного времени

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

#### Возврат null в случае, если элемент не был найден

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

#### Цепочка поиска

```java
//пример построения цепочки поиска. Корневой элемент еще не найден
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
                .foundFrom(webElement(tagName("body"))
                    .criteria(shouldBeEnabled())
                    .timeOut(ofSeconds(5))));              
        //..
    }
}
```

```java
//пример построения цепочки поиска. Корневой элемент уже найден
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
        var rootElement = seleniumSteps.find(webElement(tagName("body"))
                .criteria(shouldBeEnabled())
                .timeOut(ofSeconds(5)));
        
        //..
        var webElement = seleniumSteps.find(webElement(className("MyClass"))
                .foundFrom(rootElement));              
        //..
    }
}
```

---

### Поиск множества элементов

Для поиска множества элементов используется [MultipleSearchSupplier](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/searching/MultipleSearchSupplier.html)

| Возможность использовать критерии 	| Объединение критериев 	| Возможность указывать индивидуальный таймаут 	| Если желаемое значение не получено 	| Игнорируемые исключения                            	| Данные, которые могут быть  приложены к отчетам (по умолчанию)  	|
|-----------------------------------	|-----------------------	|----------------------------------------------	|------------------------------------	|----------------------------------------------------	|-----------------------------------------------------------------	|
|     Да                            	| AND                   	|        Да                                    	| Возвращает пустой List             	| org.openqa.selenium.StaleElementReferenceException 	| Скриншоты Прочие файлы                                          	|

---

#### Поиска элементов

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

#### Поиск элементов по тексту

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

#### Поиск элементов по регулярному выражению

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

#### Поиск элементов по критерию

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

#### Поиск элементов по нескольким критериям

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

#### Поиск элементов по нескольким критериям и тексту элемента

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

#### Поиск элементов по нескольким критериям и регулярному выражению

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

#### Поиск элементов c указанием отведенного времени

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

#### Поиск элементов по тексту c указанием отведенного времени

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

#### Поиск элементов по регулярному выражению c указанием отведенного времени

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

#### Поиск элементов по критерию c указанием отведенного времени

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

#### Поиск элементов по нескольким критериям c указанием отведенного времени

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

#### Поиск элементов по нескольким критериям и тексту элемента c указанием отведенного времени

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

#### Поиск элементов по нескольким критериям и регулярному выражению c указанием отведенного времени

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

#### Цепочка поиска

```java
//пример построения цепочки поиска. Корневой элемент еще не найден
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"))
                .foundFrom(webElement(tagName("body"))
                    .criteria(shouldBeEnabled())
                    .timeOut(ofSeconds(5))));              
        //..
    }
}
```

```java
//пример построения цепочки поиска. Корневой элемент уже найден
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        var rootElement = seleniumSteps.find(webElement(tagName("body"))
                .criteria(shouldBeEnabled())
                .timeOut(ofSeconds(5)));
        
        //..
        var webElements = seleniumSteps.find(webElements(className("MyClass"))
                .foundFrom(rootElement));              
        //..
    }
}
```

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

```java
//пример поиска одного элемента-кнопки
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;
//...
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.widget;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var button = seleniumSteps.find(widget(Button.class)
                .criteria(shouldBeEnabled()) //условие, что кнопка должна быть доступной, если необходимо 
                .criteria("Кнопка расположена по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100) //условие, указанное пользователем, если необходимо 
                .timeOut(ofSeconds(5)) //Время, на поиск, если необходимо      
                .foundFrom(webElement(tagName("form")))); //описание того как найти элемент, от которого следует осуществлять поиск 
                //сюда можно передать уже найденный элемент     
        //или      
        //_______________________________________________________________________________
        var button2 = seleniumSteps.find(widget(Button.class)
                .criteria(shouldBeEnabled()) //условие, что кнопка должна быть доступной, если необходимо 
                .criteria("Кнопка расположена по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100) //условие, указанное пользователем, если необходимо 
                .timeOut(ofSeconds(5)) //Время, на поиск, если необходимо      
                .foundFrom(widget(Form.class))); //описание того как найти виджет, от которого следует осуществлять поиск
                //сюда можно передать уже найденный элемент
    }
}
```

```java
//пример поиска множества элементов-кнопок
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;
//...
import static java.time.Duration.ofSeconds;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.widget;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.widgets;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldBeEnabled;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var buttons = seleniumSteps.find(widgets(Button.class)
                .criteria(shouldBeEnabled()) //условие, что кнопка должна быть доступной, если необходимо 
                .criteria("Кнопка расположена по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100) //условие, указанное пользователем, если необходимо 
                .timeOut(ofSeconds(5)) //Время, на поиск, если необходимо      
                .foundFrom(webElement(tagName("form")))); //описание того как найти элемент, от которого следует осуществлять поиск 
                //сюда можно передать уже найденный элемент  
        //или      
        //_______________________________________________________________________________
        var buttons2 = seleniumSteps.find(widgets(Button.class)
                .criteria(shouldBeEnabled()) //условие, что кнопка должна быть доступной, если необходимо 
                .criteria("Кнопка расположена по оси Y выше чем 100", element -> 
                        element.getLocation().getY() < 100) //условие, указанное пользователем, если необходимо 
                .timeOut(ofSeconds(5)) //Время, на поиск, если необходимо      
                .foundFrom(widget(Form.class))); //описание того как найти виджет, от которого следует осуществлять поиск
                //сюда можно передать уже найденный элемент        
    }
}
```

При поиске не обязательно указывать конкретный (НЕ АБСТРАКТНЫЙ) класс виджета. Поиск виджетов выполнен таком образом, чтобы не завязываться
на особенности той или иной страницы приложения. Достаточно просто указать абстрактный класс, представляющий тот или иной класс элемента. 
И в зависимости от страницы, будут найдены те или иные элементы, описанные различными классами, расширяющими указанный абстрактный. 

```java
//простой пример
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.widget;


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

@FindBys({@FindBy(tagName = "form"), @FindBy(className = "MyButton")})
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

//_________________________________________________________________________________________________

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var button = seleniumSteps.find(widget(Button.class));//на одной странице могут быть найдены кнопки
        //описанные классом CommonButton
        //на другой странице - MyButton
    }
}
```

Кроме того, существует возможность поиска по меткам. Для этого класс виджета должен реализовывать интерфейс [Labeled](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/Labeled.html).

```java
//простой пример
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import ru.tinkoff.qa.neptune.selenium.api.widget.Labeled;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.widget;


@FindBy(tagName = "button")
public class CommonButton extends Button implements Labeled {
    public CommonButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    @Override
    public void click() {
        //реализация
    }
    
    @Override
    public List<String> labels() {
        //реализация
    }

    //....
}

@FindBys({@FindBy(tagName = "form"), @FindBy(className = "MyButton")})
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

//_________________________________________________________________________________________________

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var button = seleniumSteps.find(widget(Button.class, "Сохранить"));//Согласно примеру выше,
        //по меткам будут искаться только кнопки, описанные классом CommonButton
    }
}
```

Имеется ряд методов, которые помогают описать примеры (выше) поиска кнопки в таком виде

```java
//простой пример
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.buttons;




//_________________________________________________________________________________________________

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var button = seleniumSteps.find(button());
        var buttons = seleniumSteps.find(buttons());
        
        var button2 = seleniumSteps.find(button("Сохранить"));
        var buttons2 = seleniumSteps.find(buttons("Сохранить"));
    }
}
```

Если есть виджеты, описывающие специфические/характерные для приложения элементы, ничего не мешает сделать свою библиотеку критериев поиска.

```java

import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier;

import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.widget;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.widgets;

public class MySearches {
    
    public static SearchSupplier<MyWidget> myWidget() {
        return widget(MyWidget.class);
    }
    
    public static MultipleSearchSupplier<MyWidget> myWidgets() {
        return widgets(MyWidget.class);
    }
    
    //и т.д.
}
```

## Присутствие/отсутствие элементов/виджетов на странице

### Присутствие элементов/виджетов на странице 

См. [Шаги для определения присутствия](/doc/rus/core/Presence_Absence.md#Присутствие). 

Для определения присутствия элементов/виджетов на странице в данный момент/спустя время, отведенное на поиск, используется [ElementPresence](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/functions/searching/presence/ElementPresence.html).

```java
//пример: присутствие вэб-елемента
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.presence.ElementPresence.presenceOfAnElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //присутствие вэб-елемента в данный момент
        //или спустя время, отведенное на поиск
        seleniumSteps.get(presenceOfAnElement(webElement(tagName("MyTag"))));
    }
}
```


```java
//пример: присутствие кнопки "Push me"
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.presence.ElementPresence.presenceOfAnElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //присутствие кнопки "Push me" в данный момент
        //или спустя время, отведенное на поиск
        seleniumSteps.get(presenceOfAnElement(button("Push me")));
    }
}
```

```java
//пример: присутствие вэб-елементов
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.presence.ElementPresence.presenceOfElements;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //присутствие вэб-елементов (одного или более) в данный момент
        //или спустя время, отведенное на поиск
        seleniumSteps.get(presenceOfElements(webElements(tagName("MyTag"))));
    }
}
```

```java
//пример: присутствие кнопок
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.buttons;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.presence.ElementPresence.presenceOfElements;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //присутствие кнопок (одной или более) в данный момент
        //или спустя время, отведенное на поиск
        seleniumSteps.get(presenceOfElements(buttons()));
    }
}
```

Если необходимо просто определить, присутствует ли элемент на странице сейчас/спустя некоторое время

### Отсутствие элементов/виджетов на странице 

См. [Шаги для определения отсутствия](/doc/rus/core/Presence_Absence.md#Отсутствие).

В разработке. 

---
См. также [Шаги возвращающие результат](/doc/rus/core/Steps.md#Шаги-возвращающие-результат)

См. также [Построение цепочек шагов, возвращающих результат](/doc/rus/core/Steps.md#Построение-цепочек-шагов,-возвращающих-результат)