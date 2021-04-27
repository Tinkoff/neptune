# CSS элементов

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст). 
  Объект [контекста для браузера](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/SeleniumStepContext.html)
  в примерах ниже вызывается статическим методом `ru.tinkoff.qa.neptune.selenium.SeleniumStepContext#inBrowser`

- О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги,-которые-возвращают-результат).

Полезные страницы:

- [Поиск элементов](ELEMENTS_SEARCHING.MD)
- [Виджеты](./WIDGET.MD)

```java
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        //Получение css объекта org.openqa.selenium.WebElement,
        var css = inBrowser().cssValueOf(webElement(tagName("someTag")), "css_property_name");
        //который будет найден во время выполнения действия
        
        //Либо org.openqa.selenium.WebElement можно найти заранее
        var element = inBrowser().find(webElement(tagName("someTag")));
        var css2 = inBrowser().cssValueOf(element, "css_property_name"); //и получить css в нужный момент

        //Получение css объекта, чей класс наследует ru.tinkoff.qa.neptune.selenium.api.widget.Widget,
        var css3 = inBrowser().cssValueOf(widget(SomeeWidget.class), "css_property_name");
        //который будет найден во время выполнения действия

        //Либо объект, чей класс наследует ru.tinkoff.qa.neptune.selenium.api.widget.Widget,
        //можно найти заранее
        var element2 = inBrowser().find(widget(SomeeWidget.class));
        var css4 = inBrowser().cssValueOf(element2, "css_property_name"); //и получить css в нужный момент
    }
}
```