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

Ниже пример реализации логики скролирования страницы до нужного элемента/фокусировки на нужном элементе

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

## Виджеты. Как реализовать индивидуальный механизм авто-скроллирования

