# Значения элементов

См. [Поиск элементов страницы](/doc/rus/selenium/SearchingForElements.md)

См. [Предлагаемое использование шаблона проектирования Page Object](/doc/rus/selenium/SearchingForElements.md#Предлагаемое-использование-шаблона-проектирования-Page-Object)


Для выполнения редактирования используется [EditActionSupplier](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/edit/EditActionSupplier.html)

| [Данные, которые могут быть  приложены к отчетам (по умолчанию)](/doc/rus/core/Steps.md#Данные,-которые-могут-быть-приложены-к-отчетам-и-логу) 	|
|-----------------------------------------------------------------	|
| Скриншоты Прочие файлы                                          	|


## Редактирование

Для того, чтобы описать редактирование значения элемента-виджета, нужно чтобы класс, который описывает виджет, реализовывал интерфейс [Editable](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/Editable.html):

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.Editable;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;
import static ru.tinkoff.qa.neptune.selenium.functions.edit.EditActionSupplier.valueOfThe;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.widget;

@FindBy(xpath = "/path/to/editable/element")
public class MyEditableWidget extends Widget implements Editable<String> {

    public MyEditableWidget(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    @Override
    public void edit(String valueToSet) {
        //реализация
    }    
}

//и тогда
public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        seleniumSteps.edit(valueOfThe(widget(MyEditableWidget.class), "String to change value"));      
    }
}
```

### Редактирование с поиском виджета

```java
//на примере чекбокса
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.edit.EditActionSupplier.valueOfThe;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.checkBox;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        seleniumSteps.edit(valueOfThe(checkBox("Flag me"), true));   
        //чекбокс еще не найден. Его следует найти перед редактированием
        //..
    }
}
```

### Редактирование уже найденного виджета


```java
//на примере чекбокса
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.edit.EditActionSupplier.valueOfThe;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.checkBox;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var checkBox = seleniumSteps.find(checkBox("Flag me"));
        seleniumSteps.edit(valueOfThe(checkBox, true));    
        //чекбокс уже найден перед редактированием
        //..
    }
}
```

### Цепочки

Операции редактирования можно объединять в цепочки одним из следующих способов

```java
//простой последовательный вызов метода .edit
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.edit.EditActionSupplier.valueOfThe;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.checkBox;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.select;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
       seleniumSteps.edit(valueOfThe(checkBox("Flag me"), true)) 
          .edit(valueOfThe(select("Select me then"), "Value to select")); 
    }
} 
```

```java
//вызов метода .edit один раз
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.edit.EditActionSupplier.valueOfThe;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.checkBox;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.select;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
       seleniumSteps.edit(valueOfThe(checkBox("Flag me"), true) 
          .andValueOfThe(select("Select me then"), "Value to select")); 
    }
} 
```

Второй способ позволяет записывать цепочку операций редактирования в одну переменную/поле класса и т.п. Дает интересные возможности, например, 
для параметризации тестов.

```java
//вызов метода .click один раз
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.edit.EditActionSupplier.valueOfThe;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.checkBox;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.select;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
       var editSequence = valueOfThe(checkBox("Flag me"), true) 
          .andValueOfThe(select("Select me then"), "Value to select");
       seleniumSteps.edit(editSequence); 
    }
} 
```

## Очистка значения

В разработке

## Получение значения

Для того, чтобы описать получение значения элемента-виджета, нужно чтобы класс, который описывает виджет, реализовывал интерфейс [HasValue](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/api/widget/HasValue.html):

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.widget;

@FindBy(xpath = "/path/to/valuable/element")
public class MyValuableWidget extends Widget implements HasValue<String> {

    public MyValuableWidget(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    @Override
    public String getValue() {
        //реализация
    }    
}

//и тогда
public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var value = seleniumSteps.getValue(ofThe(widget(MyEditableWidget.class)));      
    }
}
```

### Получение значения с поиском виджета

Для получения значений элементов используется [SequentialGetValueSupplier](https://tinkoffcreditsystems.github.io/neptune/ru/tinkoff/qa/neptune/selenium/functions/value/SequentialGetValueSupplier.html).

| [Возможность использовать критерии](/doc/rus/core/Steps.md#Критерии)| [Объединение критериев](/doc/rus/core/Steps.md#Объединение-критериев)| [Возможность указывать индивидуальный таймаут](/doc/rus/core/Steps.md#Время-на-получение-значимого-результата)| Если желаемое значение не получено | [Игнорируемые исключения](/doc/rus/core/Steps.md#Игнорирование-выбрасываемых-исключений)| [Данные, которые могут быть  приложены к отчетам (по умолчанию)](/doc/rus/core/Steps.md#Данные,-которые-могут-быть-приложены-к-отчетам/логу)|
|-----------------------------------	|-----------------------	|----------------------------------------------	|----------------------------------------------------------------------	|----------------------------------------------------	|-----------------------------------------------------------------	|
|            НЕТ                     	| -             	|                НЕТ                         	| Возвращает null                                                      	| НЕТ 	                                                | Любые                                       	        |


```java
//на примере текстового поля 
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetValueSupplier.ofThe;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var value = seleniumSteps.getValue(ofThe(textField("Fill me")));   
        //поле еще не найдено. Его следует найти перед получением значения
        //..
    }
}
```

### Получение значения уже найденного виджета


```java
//на примере текстового поля 
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static ru.tinkoff.qa.neptune.selenium.functions.value.SequentialGetValueSupplier.ofThe;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var fillMeField = seleniumSteps.find(textField("Fill me"));
        var value = seleniumSteps.getValue(ofThe(fillMeField));      
        //поле уже найдено перед получением значения
        //..
    }
}
```

---
См. также [Шаги, выполняющие действие](/doc/rus/core/Steps.md#Шаги,-выполняющие-действие)

См. также [Шаги возвращающие результат](/doc/rus/core/Steps.md#Шаги-возвращающие-результат)

См. также [Построение цепочек шагов, возвращающих результат](/doc/rus/core/Steps.md#Построение-цепочек-шагов,-возвращающих-результат)

## Проверка значения

Модуль содержит матчер, который позволяет проверять значения элементов [HasValueMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/HasValueMatcher.html).

```java
//на примере текстового поля 
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasValueMatcher.hasValue;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")), 
                  hasValue("Filled value"));
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
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasValueMatcher.hasValue;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")), 
                  hasValue(containsString("Filled")));
        //..
    }
}
```

---
См. также [check](/doc/rus/check/Main.md)

См. также [hamcrest.org](http://hamcrest.org/JavaHamcrest/)

