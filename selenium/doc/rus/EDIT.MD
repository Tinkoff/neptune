# Редактирование элементов страницы

О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/IDEA.MD#Шаги,-которые-возвращают-результат).

О принципах работы шагов, которые выполняют действие, можно
прочитать [здесь](./../../../core.api/doc/rus/IDEA.MD#Шаги,-которые-выполняют-действие).

Полезные страницы:

- [Поиск элементов](ELEMENTS_SEARCHING.MD)
- [Виджеты](./WIDGET.MD)

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.Editable;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

public class EditableWidget extends Widget implements Editable<String> {
    //Реализация логики
}
```

```java
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        //Редактирование объекта, чей класс наследует ru.tinkoff.qa.neptune.selenium.api.widget.Widget и 
        //реализует интерфейс ru.tinkoff.qa.neptune.selenium.api.widget.Editable,
        inBrowser().edit(widget(EditableWidget.class), "Some text");
        //который будет найден во время выполнения действия

        //Либо объект, чей класс наследует ru.tinkoff.qa.neptune.selenium.api.widget.Widget и 
        //реализует интерфейс ru.tinkoff.qa.neptune.selenium.api.widget.Editable,
        //можно найти заранее
        var element = inBrowser().find(widget(EditableWidget.class));
        inBrowser().edit(element, "Some text"); //и выполнить редактирование в нужный момент
    }
}
```