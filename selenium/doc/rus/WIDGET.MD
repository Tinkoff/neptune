# Виджеты

Сначала полезно прочитать про
дизайн-паттерн [Page Object](https://www.selenium.dev/documentation/en/guidelines_and_recommendations/page_object_models/)
.

Neptune предлагает свой вариант реализации этого шаблона проектирования, который сводится к типизации элементов на
странице, описанию их специфического поведения.

## Задача

Предположим, нужно написать тесты для следующей страницы

![Страница](Page.PNG "Title")

Ниже ее DOM модель

```html
<!DOCTYPE html>
<html>
<body>

<h1>The input element</h1>

<form action="/action_page.php">
    <label for="fname">First name:</label>
    <input type="text" id="fname" name="fname"><br><br>
    <label for="lname">Last name:</label>
    <input type="text" id="lname" name="lname"><br><br>
    <input type="submit" value="Submit">
</form>

</body>
</html>
```

Представим, что front end нашего приложения состоит из таких же элементов. Попробуем описать их

## Создание виджета

- Заголовок

```java
import org.openqa.selenium.support.FindBy;

import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

@Name("Заголовок") //Данная аннотация влияет но то, как будет представлено строковое 
//значение данного объекта при вызове метода toString()
@Priority(priority = 2) //С помощью данной аннотации можно управлять очередью
//поиска однотипных элементов. 
//Если у данного класса выше приоритет,
//то механизм поиска первыми будет искать элементы данного класса.
//Если такие элементы найти не удалось, далее будут произведены попытки найти элементы
//классов с меньшим приоритетом.
//Максимальный приоритет = 1. Чем выше цифра, тем ниже приоритет.
@FindBy(tagNname = "h1") //Локатор, по которому можно найти элементы данного класса
public class Header
        extends Widget //Нужно унаследовать от этого класса
        implements HasValue<String> //Какое значение может предоставить элемент данного типа
{

    //Унаследованный конструктор
    protected Header(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    //Назначение таких элементов - простой вывод текста
    //Предположим, что этот текст - значение элемента, 
    //которое предоставляется виджетом
    @Override
    public String getValue() {
        return getWrappedElement().getText();
    }
}
```

- Текстовые поля

```java
import org.openqa.selenium.support.FindBy;

import ru.tinkoff.qa.neptune.selenium.api.widget.Editable;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

@Name("Текстовое поле ввода")
@FindBy(xpath = "./input[@type='text']")
public class TextInput
        extends Widget 
        implements HasValue<String>, 
        Editable<String> //Означает, что эти элементы можно редактировать, 
        // в данном случае вводом строчки
{
    
    protected TextInput(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override //Описываем механизм редактирования
    public void edit(String valueToSet) {
        var e = getWrappedElement();
        e.clear();
        e.sendKeys(charSequence);
    }

    @Override //Если поле заполнено, то можем считать,
    //что это значение, которое предоставляет виджет
    public String getValue() {
        return getWrappedElement().getAttribute("value");
    }
}
```

- Кнопки

```java

```