# Матчеры элементов.

Частично описано:
- [Проверка значения элемента](/doc/rus/selenium/ElementValues.md#Проверка-значения)
- [Проверка значения атрибута элемента](/doc/rus/selenium/ElementAttributesAndCSS.md#Проверка-значения-атрибута)
- [Проверка значения css элемента](/doc/rus/selenium/ElementAttributesAndCSS.md#Проверка-css)

Ниже описаны прочие матчеры, которые можно использовать для пароверок элементов. 

## Расположение элемента на странице

Модуль содержит матчер, который позволяет проверять расположение элементов на странице [HasLocationMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/HasLocationMatcher.html)

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasLocationMatcher.hasLocation;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(tagName("textArea"))),
                hasLocation(100, 100));
        //..
    }
}
```

```java
//на примере текстового поля 
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasLocationMatcher.hasLocation;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")),
                hasLocation(100, 100));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasLocationMatcher.hasLocation;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")),
                        hasLocation(greaterThanOrEqualTo(100), 100));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasLocationMatcher.hasLocation;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")),
                        hasLocation(100, greaterThanOrEqualTo(100)));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasLocationMatcher.hasLocation;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")),
                        hasLocation(greaterThanOrEqualTo(100), greaterThanOrEqualTo(100)));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasLocationMatcher.hasLocation;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var buttonElement = seleniumSteps.find(webElement(tagName("button")));
        assertThat(seleniumSteps.find(textField("Fill me")), //в данном примере проверяются относительные координаты
                hasLocation(greaterThanOrEqualTo(100), greaterThanOrEqualTo(100)).relativeTo(buttonElement));
        //относительно элемента buttonElement
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasLocationMatcher.hasLocation;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var button = seleniumSteps.find(button("Push me"));
        assertThat(seleniumSteps.find(textField("Fill me")), //в данном примере проверяются относительные координаты
                hasLocation(greaterThanOrEqualTo(100), 100).relativeTo(button));
        //относительно кнопки  "Push me"
        //..
    }
}
```

## Размер элемента

Модуль содержит матчер, который позволяет проверять размеры элементов [HasSizeMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/HasSizeMatcher.html)

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasSizeMatcher.hasDimensionalSize;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(tagName("textArea"))),
                hasDimensionalSize(100, 100));
        //..
    }
}
```

```java
//на примере текстового поля 
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasSizeMatcher.hasDimensionalSize;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")),
                hasDimensionalSize(100, 100));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasSizeMatcher.hasDimensionalSize;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")),
                        hasDimensionalSize(greaterThanOrEqualTo(100), 100));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasSizeMatcher.hasDimensionalSize;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")),
                        hasDimensionalSize(100, greaterThanOrEqualTo(100)));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasSizeMatcher.hasDimensionalSize;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")),
                        hasDimensionalSize(greaterThanOrEqualTo(100), greaterThanOrEqualTo(100)));
        //..
    }
}
```

## Текст элемента

Модуль содержит матчер, который позволяет проверять тексты элементов [HasTextMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/HasTextMatcher.html)

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasTextMatcher.hasText;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(tagName("textArea"))), hasText("Some text"));
        //..
    }
}
```

```java
//на примере текстового поля 
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasTextMatcher.hasText;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")), hasText("Some text"));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasTextMatcher.hasText;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(tagName("textArea"))), hasText(containsString("Some text")));
        //..
    }
}
```

```java
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasTextMatcher.hasText;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(tagName("textArea"))), hasText());
        //В данном примере проверяется, что элемент имеет вообще какой-либо непустой текст 
        //..
    }
}
```

## Видимость элемента

Модуль содержит матчер, который позволяет проверять видимость элементов [IsElementVisibleMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/IsElementVisibleMatcher.html)

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.IsElementVisibleMatcher.isVisible;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(tagName("textArea"))), isVisible());
        //..
    }
}
```

```java
//на примере текстового поля 
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.IsElementVisibleMatcher.isVisible;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")), isVisible());
        //..
    }
}
```


## Доступность элемента

Модуль содержит матчер, который позволяет проверять доступность элементов [IsElementEnabledMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/IsElementEnabledMatcher.html)

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.IsElementEnabledMatcher.isEnabled;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(tagName("textArea"))), isEnabled());
        //..
    }
}
```

```java
//на примере текстового поля 
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.IsElementEnabledMatcher.isEnabled;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.textField;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(textField("Fill me")), isEnabled());
        //..
    }
}
```

## Наличие множества вложенных элементов

Модуль содержит матчер, который позволяет проверять наличие множества элементов внутри/относительно проверяемого [HasNestedElementsMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/HasNestedElementsMatcher.html). 
Если необходимо проверить наличие хотя бы одного вложенного элемента, то больше подходит [матчер, проверяющий наличие вложенного элемента](#Наличие-вложенного-элемента), 
но использование данного матчера для тех же целей не запрещается.

```java
//
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasNestedElementsMatcher.hasNestedElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.buttons;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(className("MyClass"))),
                hasNestedElements(buttons())); //проверка наличия кнопок внутри элемента
        //..
    }
}
```

```java
//Или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasNestedElementsMatcher.hasNestedElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(className("MyClass"))),
                hasNestedElements(webElements(tagName("span")))); //проверка вложенных элементов <span>
        //..
    }
}
```

```java
//Или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasNestedElementsMatcher.hasNestedElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(className("MyClass"))),
                hasNestedElements(webElements(tagName("span"))).withCount(2)); //проверка вложенных элементов <span>
        // Ожидается что их ровно 2
        //..
    }
}
```

```java
//Или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasNestedElementsMatcher.hasNestedElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(className("MyClass"))),
                hasNestedElements(webElements(tagName("span")))
                        .withCount(greaterThanOrEqualTo(2))); //проверка вложенных элементов <span>
        // Ожидается что их 2 или более
        //..
    }
}
```

## Наличие вложенного элемента

Модуль содержит матчер, который позволяет проверять наличие хотя бы одного элемента внутри/относительно проверяемого [HasNestedElementMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/HasNestedElementMatcher.html)

```java
//
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasNestedElementMatcher.hasNestedElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(className("MyClass"))),
                hasNestedElement(button())); //проверка наличия кнопоки внутри элемента
        //..
    }
}
```

```java
//Или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.openqa.selenium.By.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasNestedElementMatcher.hasNestedElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(webElement(className("MyClass"))),
                hasNestedElement(webElement(tagName("span")))); //проверка вложенных элементов <span>
        //..
    }
}
```

## Специфические матчеры

### [Select](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/api/widget/drafts/Select.html). Проверка опций

Модуль содержит матчер, который позволяет проверять опции селектов [HasOptionsMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/HasOptionsMatcher.html)

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasOptionsMatcher.hasOptions;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.select;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(select("Select something")), //Проверка селекта
                        hasOptions("Select me1", "Select me2", "Select me3"));
        //Ожидается что селект строго имеет перечисленные опции в указанном порядке
        //..
    }
}
```

```java
//или 
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasOptionsMatcher.hasOptions;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.select;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var options = List.of("Select me1", "Select me2", "Select me3");

        assertThat(seleniumSteps.find(select("Select something")), //Проверка селекта
                hasOptions(options));
        //Ожидается что селект строго имеет перечисленные опции в порядке, как в переданной коллекции
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
import static org.hamcrest.Matchers.everyItem;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasOptionsMatcher.hasOptions;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.select;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(select("Select something")), //Проверка селекта
                        hasOptions(everyItem(containsString("Select"))));
        //..
    }
}
```

### [Table](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/api/widget/drafts/Table.html). Проверка колонок.

Модуль содержит матчер, который позволяет проверять колонки таблиц [HasColumnMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/HasColumnMatcher.html)

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasColumnMatcher.hasAColumn;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.table;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(table()), //Проверка таблицы
                hasAColumn("Some column", "Value 1", "Value 1", "Value 1"));
        //Ожидается что таблица имеет колонку с названием "Some column" 
        // и строго перечисленными значениями в указанном порядке
        //..
    }
}
```

```java
//или 
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasColumnMatcher.hasAColumn;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.table;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        var values = List.of("Value 1", "Value 1", "Value 1");
        
        assertThat(seleniumSteps.find(table()), //Проверка таблицы
                hasAColumn("Some column", values));
        //Ожидается что таблица имеет колонку с названием "Some column"
        // и строго перечисленными значениями в порядке, как в переданной коллекции
        //Ожидается что селект строго имеет перечисленные опции в порядке, как в переданной коллекции
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
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasColumnMatcher.hasAColumn;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.table;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(table()),
                        hasAColumn(containsString("column"), "Value 1", "Value 1", "Value 1"));
        //..
    }
}
```

```java
//или 
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.startsWith;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasColumnMatcher.hasAColumn;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.table;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(table()),
                hasAColumn("Some column", everyItem(startsWith("Value"))));
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
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.startsWith;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasColumnMatcher.hasAColumn;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.table;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(table()),
                hasAColumn(containsString("Some column"), everyItem(startsWith("Value"))));
        //..
    }
}
```


### [Table](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/api/widget/drafts/Table.html). Проверка конкретных строк.

Модуль содержит матчер, который позволяет проверять определенные строки таблиц [HasTableRowMatcher](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/HasTableRowMatcher.html). 

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasTableRowMatcher.hasARow;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasValueMatcher.hasValue;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.table;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        //Проверка второй строки таблицы
        assertThat(seleniumSteps.find(table()),
                hasARow(1,  hasValue(contains("Value 1", "Value 1", "Value 1"))));
        //Ожидается, что вторая строка таблицы имеет перечисленные значения в указанном порядке
        //..
    }
}
```

### [Link](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/api/widget/drafts/Link.html). Проверка ссылок.

Модуль содержит матчер, который позволяет проверять ссылки [HasReference](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/hamcrest/matchers/elements/HasReference.html). 

```java
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasReference.hasReference;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(link("Click me")),
                        hasReference("https://www.google.com"));
        //Ожидается, что ссылка равна "https://www.google.com"
        //..
    }
}
```

```java
import java.net.URL;
//или
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
//...
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasReference.hasReference;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() throws Exception {
        //..
        var google = new URL("https://www.google.com");
        assertThat(seleniumSteps.find(link()), hasReference(google)); 
        //Ожидается, что ссылка равна "https://www.google.com"
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
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasReference.hasReference;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.link;

public class MyTests /*...*/ {
    private SeleniumStepContext seleniumSteps;
    
    @Test
    public void myTest() {
        //..
        assertThat(seleniumSteps.find(link()),
                        hasReference(containsString("google")));
        //Ожидается, что ссылка содержит "google"
        //..
    }
}
```

---
См. также [check](/doc/rus/check/Main.md)

См. также [hamcrest.org](http://hamcrest.org/JavaHamcrest/)