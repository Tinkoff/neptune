# Значение элементов страницы

О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/IDEA.MD#Шаги,-которые-возвращают-результат).

Полезные страницы:

- [Поиск элементов](ELEMENTS_SEARCHING.MD)
- [Виджеты](./WIDGET.MD)

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

public class ValuableWidget extends Widget implements HasValue<String> {
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
        //Получение значения объекта, чей класс наследует ru.tinkoff.qa.neptune.selenium.api.widget.Widget и 
        //реализует интерфейс ru.tinkoff.qa.neptune.selenium.api.widget.HasValue,
        String value = inBrowser().valueOf(widget(ValuableWidget.class));
        //который будет найден во время выполнения действия

        //Либо объект, чей класс наследует ru.tinkoff.qa.neptune.selenium.api.widget.Widget и 
        //реализует интерфейс ru.tinkoff.qa.neptune.selenium.api.widget.HasValue,
        //можно найти заранее
        var element = inBrowser().find(widget(ValuableWidget.class));
        String value2 = inBrowser().valueOf(element); //и получить его значение
    }
}
```