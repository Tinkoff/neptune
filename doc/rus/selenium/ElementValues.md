# Значения элементов

См. [Поиск элементов страницы](/doc/rus/selenium/SearchingForElements.md)

## Редактирование

См. [Предлагаемое использование шаблона проектирования Page Object](/doc/rus/selenium/SearchingForElements.md#Предлагаемое-использование-шаблона-проектирования-Page-Object)

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
        //чекбокс еще не найден. Его следует найти перед редактировием
        //..
    }
}
```

### Редактирование уже найденного виджета


```java
//на примере клика по ссылке
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
        //чекбокс уже найден перед редактировием
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

---
См. также [Шаги, выполняющие действие](/doc/rus/core/Steps.md#Шаги,-выполняющие-действие)

См. также [Шаги возвращающие результат](/doc/rus/core/Steps.md#Шаги-возвращающие-результат)

## Валидация значения