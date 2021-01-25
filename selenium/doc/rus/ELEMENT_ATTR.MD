# Значение атрибутов элементов

О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/IDEA.MD#Шаги,-которые-возвращают-результат).

Полезные страницы:

- [Поиск элементов](ELEMENTS_SEARCHING.MD)
- [Виджеты](./WIDGET.MD)

```java
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        //Получение атрибута объекта org.openqa.selenium.WebElement,
        var attr = inBrowser().attrValueOf(webElement(tagName("someTag")), "attr_name");
        //который будет найден во время выполнения действия
        
        //Либо org.openqa.selenium.WebElement можно найти заранее
        var element = inBrowser().find(webElement(tagName("someTag")));
        var attr2 = inBrowser().attrValueOf(element, "attr_name"); //и получить значение атрибута в нужный момент

        //Получение атрибута объекта, чей класс наследует ru.tinkoff.qa.neptune.selenium.api.widget.Widget,
        var attr3 = inBrowser().attrValueOf(widget(SomeeWidget.class), "attr_name");
        //который будет найден во время выполнения действия

        //Либо объект, чей класс наследует ru.tinkoff.qa.neptune.selenium.api.widget.Widget,
        //можно найти заранее
        var element2 = inBrowser().find(widget(SomeeWidget.class));
        var attr4 = inBrowser().attrValueOf(element2, "attr_name"); //и получить значение атрибута в нужный момент
    }
}
```