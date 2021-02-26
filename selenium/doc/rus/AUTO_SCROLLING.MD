# Авто-скроллирование

Полезные страницы:

- [Поиск элементов](ELEMENTS_SEARCHING.MD)
- [Виджеты](./WIDGET.MD)
- [Клик по элементам](./CLICK.MD)
- [Редактирование элементов](./EDIT.MD)
- [Значение элементов](./ELEMENT_VALUE.MD)
- [Интерактивные действия](./INTERACTION.MD)
- [Настройки. WEB_DRIVER_DEFAULT_AUTO_SCROLLER](./SETTINGS.MD#web_driver_default_auto_scroller)

Довольно часто бывает, что та или иная страница приложения перегружена элементами, и браузер не может вывести 
на экран всю страницу. Часть элементов находится вне поля зрения, и для взаимодействия с этими элементами нужно выполнять 
прокрутку страницы, или как-то по-другому фокусироваться на скрытых элементах.

Ниже вариант решения, который предлагает Neptune.

## AutoScroller

Ниже пример реализации логики скроллирования страницы до нужного элемента/фокусировки на нужном элементе

```java
package org.my.pack;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.auto.scrolling.AutoScroller;

public class MyAutoScroller extends AutoScroller {

    public MyAutoScroller(WebDriver driver) {
        super(driver);
    }

    //Предположим мы хотим выполнить через java script.
    @Override
    protected void scrollIntoView(WebElement e) {
        executeScript("arguments[0].scrollIntoView()", e);
        //так же доступны методы:
        // - executeAsyncScript
        // - getWrappedDriver - если нужен WebDriber для выполнения
    }
}
```

Заполняем настройку [WEB_DRIVER_DEFAULT_AUTO_SCROLLER](./SETTINGS.MD#web_driver_default_auto_scroller). Подробнее о свойствах, 
которые возвращают объекты см. [статью](./../../../core.api/doc/rus/SETTINGS.MD#Свойства-для-создания-и-возврата-объектов)

```properties
WEB_DRIVER_DEFAULT_AUTO_SCROLLER=org.my.pack.MyAutoScroller
```

И теперь

```java
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        inBrowser().click(webElement(tagName("someTag"))); //перед тем как выполнить click
        //элемент будет выведен на экран, или н нем будет фокусировка
    }
}
```

или 

```java
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        inBrowser().click(button("Some button")); //перед тем как выполнить click
        //кнопка будет выведена на экран, или н ней будет фокусировка
    }
}
```

Для `WebElement` описанный механизм сработает при вызове следующих методов:

- `click`
- `submit`
- `sendKeys`
- `clear`
- `getLocation`
- `getSize`
- `getRect`
- `getCoordinates`

Для виджетов описанный механизм сработает при вызове следующих методов:

- `ru.tinkoff.qa.neptune.selenium.api.widget.Clickable.click`
- `ru.tinkoff.qa.neptune.selenium.api.widget.Editable.edit`
- `ru.tinkoff.qa.neptune.selenium.api.widget.HasLocation.getLocation`
- `ru.tinkoff.qa.neptune.selenium.api.widget.HasRectangle.getRect`
- `ru.tinkoff.qa.neptune.selenium.api.widget.HasSize.getSize`
- `ru.tinkoff.qa.neptune.selenium.api.widget.HasValue.getValue`

## Виджеты. Как реализовать индивидуальный механизм авто-скроллирования

Предположим, мы реализовали виджет со специфическим поведением

```java
import org.openqa.selenium.support.FindBy;

import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

@Name("Какой-то чудо-виджет")
@FindBy(xpath = ".//some/path")
public class SomeWidget
        extends Widget {

    protected SomeWidget(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    public void doSomeInteractiveAction() {
        //Описание дейстаия
    }

    public void doSomeInteractiveAction2() {
        //Описание дейстаия
    }
}
```

Допустим, описанный выше механизм не подходит для данного виджета. И допустим, что методы `doSomeInteractiveAction` и 
`doSomeInteractiveAction2` требуют вызов скроллинга/фокусировки на виджете. Тогда

```java
import org.openqa.selenium.support.FindBy;

import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.NeedToScrollIntoView;
import ru.tinkoff.qa.neptune.selenium.api.widget.ScrollsIntoView;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

@Name("Какой-то чудо-виджет")
@FindBy(xpath = ".//some/path")
public class SomeWidget
        extends Widget implements ScrollsIntoView { //Нужно использовать этот интерфейс
    //Данный интерфейс нужен тогда, когда виджет требует описание специфического скроллинга/фокуса

    protected SomeWidget(WebElement wrappedElement) {
        super(wrappedElement);
    }
    
    @NeedToScrollIntoView //Так отмечаются методы, которые требуют скроллинга/фокуса 
    public void doSomeInteractiveAction() {
        //Описание дейстаия
    }

    @NeedToScrollIntoView //Так отмечаются методы, которые требуют скроллинга/фокуса 
    public void doSomeInteractiveAction2() {
        //Описание дейстаия
    }

    @Override
    public void scrollIntoView() {
        //Тут реализуется специфическая логика скроллирования до виджета/фокуса на нем
    }
}
```