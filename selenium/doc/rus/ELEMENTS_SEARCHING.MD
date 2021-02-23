#Поиск элементов страницы

Полезные страницы:
- [Виджеты](./WIDGET.MD)
- [О принципах работы шагов, которые возвращают результат](./../../../core.api/doc/rus/IDEA.MD#Шаги,-которые-возвращают-результат).

## Пример поиска

Поиск нетипизированного элемента (`org.openqa.selenium.WebElement`) на странице.

```java
import org.openqa.selenium.WebElement;

import static java.time.temporal.ChronoUnit.*;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        WebElement e = inBrowser().find(webElement(xpath("./some/path")) //обязательно указать локатор
                .criteria(isEnabled()) //можно указать один критерий, 
                // или несколько критериев посредством вызова `.criteria` 
                // или с использованием конкатенации AND, OR, NOT, XOR
                .timeOut(ofSeconds(40))); //Так же можно указать время, за которое должен быть найден элемент,
        //соответствующий перечисленным критерием
    }
}
```

Пример поиска виджета

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

import static java.time.temporal.ChronoUnit.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        Button button = inBrowser().find(button()
                .criteria(isEnabled()) //можно указать один критерий, 
                // или несколько критериев посредством вызова `.criteria` 
                // или с использованием конкатенации AND, OR, NOT, XOR
                .timeOut(ofSeconds(40))); //Так же можно указать время, за которое должна быть найдена кнопка,
        //соответствующая перечисленным критерием
    }
}
```

Поиск множества нетипизированных элементов (`org.openqa.selenium.WebElement`) на странице.

```java
import java.util.List;
import org.openqa.selenium.WebElement;

import static java.time.temporal.ChronoUnit.*;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        List<WebElement> elements = inBrowser().find(webElements(xpath("./some/path")) //обязательно указать локатор
                .criteria(isEnabled()) //можно указать один критерий, 
                // или несколько критериев посредством вызова `.criteria` 
                // или с использованием конкатенации AND, OR, NOT, XOR
                //Перечисленным критериям должен соответствовать каждый элемент
                //из результирующего списка
                .timeOut(ofSeconds(40))); //Так же можно указать время, за которое должен быть найден 
        //хотя бы один элемент,
        //соответствующий перечисленным критерием
    }
}
```

Пример поиска множества виджетов

```java
import java.util.List;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

import static java.time.temporal.ChronoUnit.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        List<Button> buttons = inBrowser().find(buttons()
                .criteria(isEnabled()) //можно указать один критерий, 
                // или несколько критериев посредством вызова `.criteria` 
                // или с использованием конкатенации AND, OR, NOT, XOR
                //Перечисленным критериям должна соответствовать каждая кнопка
                //из результирующего списка
                .timeOut(ofSeconds(40))); //Так же можно указать время, за которое должна быть найдена
        //хотя бы одна кнопка,
        //соответствующая перечисленным критерием
    }
}
```

Можно минимизировать использование метода `timeOut`. Статья [Настройка ожидания элемента(ов)](./SETTINGS.MD#Ожидание-элемента(ов)).
Предположим, мы указали 40 секунд как время ожидания элемента(ов) по умолчания. Тогда

- поиск одиночного элемента/виджета

```java
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        WebElement e = inBrowser().find(webElement(xpath("./some/path")) //обязательно указать локатор
                .criteria(isEnabled()) //можно указать один критерий, 
                // или несколько критериев посредством вызова `.criteria` 
                // или с использованием конкатенации AND, OR, NOT, XOR
                );
    }
}
```

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        Button button = inBrowser().find(button()
                .criteria(isEnabled()) //можно указать один критерий, 
                // или несколько критериев посредством вызова `.criteria` 
                // или с использованием конкатенации AND, OR, NOT, XOR
                );
    }
}
```

- поиск множества элементов/виджетов

```java
import java.util.List;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        List<WebElement> elements = inBrowser().find(webElements(xpath("./some/path")) //обязательно указать локатор
                .criteria(isEnabled()) //можно указать один критерий, 
                // или несколько критериев посредством вызова `.criteria` 
                // или с использованием конкатенации AND, OR, NOT, XOR
                //Перечисленным критериям должен соответствовать каждый элемент
                //из результирующего списка
                );
    }
}
```

```java
import java.util.List;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        List<Button> buttons = inBrowser().find(buttons()
                .criteria(isEnabled()) //можно указать один критерий, 
                // или несколько критериев посредством вызова `.criteria` 
                // или с использованием конкатенации AND, OR, NOT, XOR
                //Перечисленным критериям должна соответствовать каждая кнопка
                //из результирующего списка
                );
    }
}
```

Так же рекомендуется выполнить настройки [поиска только видимых элементов](SETTINGS.MD#Поиск-видимых-элементов). Если 
этого не сделать, есть вероятность того, что поиск будет находить спрятанные/невидимые элементы. Если это не желаемая 
ситуация, то придется отсекать невидимые элементы следующим образом

```java

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        inBrowser().find(button()
                .criteria(isEnabled())
                .criteria(visible())); //<--!
    }
}
```

Объекты, которые возвращаются статическими методами класса `ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier`,
могут быть использованы не только для выполнения поиска элементов на странице, но и для перечисленных действий

- [Клик по элементам](./CLICK.MD)
- [Редактирование элементов](./EDIT.MD)
- [Свертывание и развертывание](./EXPAND_COLLAPSE.MD)
- [Получение значений элементов](./ELEMENT_VALUE.MD)
- [Получение значений атрибутов элементов](./ELEMENT_ATTR.MD)
- [Получение значений CSS элементов](./ELEMENT_CSS.MD)

## Поиск по тексту и меткам

## Поиск вложенных элементов

## Поиск виджета

## Поиск кнопок

## Поиск флагов

### Поиск чекбоксов

### Поиск радио-баттонов

### Поиск тогглов

## Поиск ссылок

## Поиск селектов

## Поиск мульти-селектов

## Поиск вкладок

## Поиск текстовых полей

## Календари

### Поиск календарей

### Поиск элементов ввода календарных интервалов

## Помск элементов таблиц

### Поиск таблиц

### Поиск ячеек таблиц

### Поиск шапок таблиц

### Поиск подвалов таблиц

### Поиск ячеек таблиц

## Поиск элементов, выводящих текст

## Поиск форм

## Поиск группирующих элементов

## Поиск картинок
