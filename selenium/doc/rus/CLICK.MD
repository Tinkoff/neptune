# Клик по элементам

О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/IDEA.MD#Шаги,-которые-возвращают-результат).

О принципах работы шагов, которые выполняют действие, можно
прочитать [здесь](./../../../core.api/doc/rus/IDEA.MD#Шаги,-которые-выполняют-действие).

Полезные страницы:

- [Поиск элементов](ELEMENTS_SEARCHING.MD)
- [Виджеты](./WIDGET.MD)

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.Clickable;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

public class ClickableWidget extends Widget implements Clickable {
    //Реализация логики
}
```

```java
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        inBrowser().click(webElement(tagName("someTag"))); //Клик по объекту org.openqa.selenium.WebElement,
        //который будет найден во время выполнения действия
        
        //Либо org.openqa.selenium.WebElement можно найти заранее
        var element = inBrowser().find(webElement(tagName("someTag")));
        inBrowser().click(element); //и выполнить клик в нужный момент
        
        //Клик по объекту, чей класс наследует ru.tinkoff.qa.neptune.selenium.api.widget.Widget и 
        //реализует интерфейс ru.tinkoff.qa.neptune.selenium.api.widget.Clickable,
        inBrowser().click(widget(ClickableWidget.class));
        //который будет найден во время выполнения действия

        //Либо объект, чей класс наследует ru.tinkoff.qa.neptune.selenium.api.widget.Widget и 
        //реализует интерфейс ru.tinkoff.qa.neptune.selenium.api.widget.Clickable,
        //можно найти заранее
        var element2 = inBrowser().find(widget(ClickableWidget.class));
        inBrowser().click(element2); //и выполнить клик в нужный момент
    }
}
```