# Поиск элементов страницы

Полезные страницы:

- [Виджеты](./WIDGET.MD)
- [О принципах работы шагов, которые возвращают результат](./../../../core.api/doc/rus/IDEA.MD#Шаги,-которые-возвращают-результат)
  .

## Пример поиска

- Поиск нетипизированного элемента (`org.openqa.selenium.WebElement`) на странице:

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
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                .timeOut(ofSeconds(40))); //Так же можно указать время, за которое должен быть найден элемент,
        //соответствующий перечисленным критерием
    }
}
```

- Пример поиска виджета:

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
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                .timeOut(ofSeconds(40))); //Так же можно указать время, за которое должна быть найдена кнопка,
        //соответствующая перечисленным критерием
    }
}
```

- Поиск множества нетипизированных элементов (`org.openqa.selenium.WebElement`) на странице:

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
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                //Перечисленным критериям должен соответствовать каждый элемент
                //из результирующего списка
                .timeOut(ofSeconds(40))); //Так же можно указать время, за которое должен быть найден 
        //хотя бы один элемент,
        //соответствующий перечисленным критерием
    }
}
```

- Пример поиска множества виджетов:

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
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                //Перечисленным критериям должна соответствовать каждая кнопка
                //из результирующего списка
                .timeOut(ofSeconds(40))); //Так же можно указать время, за которое должна быть найдена
        //хотя бы одна кнопка,
        //соответствующая перечисленным критерием
    }
}
```

Можно минимизировать использование метода `timeOut`.
Статья [Настройка ожидания элемента(ов)](./SETTINGS.MD#Ожидание-элемента(ов)). Предположим, мы указали 40 секунд как
время ожидания элемента(ов) по умолчания. Тогда

- поиск одиночного элемента/виджета:

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
                // или несколько критериев посредством вызова .criteria 
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
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
        );
    }
}
```

- поиск множества элементов/виджетов:

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
                // или несколько критериев посредством вызова .criteria 
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
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                //Перечисленным критериям должна соответствовать каждая кнопка
                //из результирующего списка
        );
    }
}
```

- проверка факта наличия/появления элемента/виджета:

Полезная информация: [Про наличие или отсутствие чего либо](./../../../core.api/doc/rus/IDEA.MD#Присутствие-и-отсутствие)

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
        boolean isElementPresent = inBrowser().presenceOf(webElement(xpath("./some/path")) //обязательно указать локатор
                .criteria(isEnabled()) //можно указать один критерий, 
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                .timeOut(ofSeconds(40))); //Так же можно указать время, за которое должен быть найден элемент,
        //соответствующий перечисленным критерием
        // вернется true если элемент есть, если нет - false

        boolean isElementPresent = inBrowser().presenceOf(webElement(xpath("./some/path")),
                "Элемент не появился"); //Текст ошибки, которая должна быть выброшена
        // вернется true если элемент есть, если нет - 
        // будет выброшено исключение с текстом 'Элемент не появился'

        boolean isButtonPresent = inBrowser().presenceOf(button()
                .criteria(isEnabled()) //можно указать один критерий, 
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                .timeOut(ofSeconds(40))); //Так же можно указать время, за которое должна быть найдена кнопка,
        //соответствующая перечисленным критерием
        // вернется true если кнопка есть, если нет - false

        boolean isButtonPresent = inBrowser().presenceOf(button(),
                "Кнопка не появилась"); //Текст ошибки, которая должна быть выброшена
        // вернется true если кнопка есть, если нет - 
        // будет выброшено исключение с текстом 'Кнопка не появилась'
    }
}
```

- проверка факта наличия/появления элементов/виджетов (хотя бы одного):

Полезная информация: [Про наличие или отсутствие чего либо](./../../../core.api/doc/rus/IDEA.MD#Присутствие-и-отсутствие)

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
        boolean areElementsPresent = inBrowser().presenceOf(webElements(xpath("./some/path")) //обязательно указать локатор
                .criteria(isEnabled()) //можно указать один критерий, 
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                //Перечисленным критериям должен соответствовать каждый элемент
                //из результирующего списка
                .timeOut(ofSeconds(40))); //Так же можно указать время, за которое должен быть найден 
        //хотя бы один элемент,
        //соответствующий перечисленным критерием
        // вернется true если хотя бы один элемент есть, если нет - false

        boolean areElementsPresent = inBrowser().presenceOf(webElements(xpath("./some/path")), 
                "Элементы не появились"); //Текст ошибки, которая должна быть выброшена
        //  вернется true если хотя бы один элемент есть, если нет - 
        // будет выброшено исключение с текстом 'Элемент не появился'

        boolean areButtonsPresent = inBrowser().presenceOf(buttons()
                .criteria(isEnabled()) //можно указать один критерий, 
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                //Перечисленным критериям должна соответствовать каждая кнопка
                //из результирующего списка
                .timeOut(ofSeconds(40))); //Так же можно указать время, за которое должна быть найдена
        //хотя бы одна кнопка,
        //соответствующая перечисленным критерием
        // вернется true если хотя бы одна кнопка есть, если нет - false

        boolean areButtonsPresent = inBrowser().presenceOf(buttons(),
                "Кнопки не появились"); //Текст ошибки, которая должна быть выброшена
        // вернется true если хотя бы одна кнопка есть, если нет -  
        // будет выброшено исключение с текстом 'Кнопки не появились'
    }
}
```

- проверка факта отсутствия/исчезновения элемента/виджета:

Полезная информация: [Про наличие или отсутствие чего либо](./../../../core.api/doc/rus/IDEA.MD#Присутствие-и-отсутствие)

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
        boolean isElementPresent = inBrowser().absenceOf(webElement(xpath("./some/path")) //обязательно указать локатор
                .criteria(isEnabled()), //можно указать один критерий, 
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                ofSeconds(40)); //Время, за которое элемент,
        //соответствующий перечисленным критерием, должен исчезнуть, если он есть 
        // вернется true если элемента нет или за указанное время он исчез, иначе - false

        boolean isElementPresent = inBrowser().absenceOf(webElement(xpath("./some/path")),
                ofSeconds(40),
                "Элемент все еще тут"); //Текст ошибки, которая должна быть выброшена
        // вернется true если элемента нет или за указанное время он исчез, иначе - 
        // будет выброшено исключение с текстом 'Элемент все еще тут'

        boolean isButtonPresent = inBrowser().absenceOf(button()
                .criteria(isEnabled()), //можно указать один критерий, 
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                ofSeconds(40)); //Время, за которое кнопка,
        //соответствующая перечисленным критерием, должна исчезнуть, если она есть 
        // вернется true если кнопки нет или за указанное время она исчезла, иначе - false

        boolean isButtonPresent = inBrowser().absenceOf(button(),
                ofSeconds(40),
                "Кнопка все еще тут"); //Текст ошибки, которая должна быть выброшена
        // вернется true если кнопки нет или за указанное время она исчезла, иначе -  
        // будет выброшено исключение с текстом 'Кнопка все еще тут'
    }
}
```

- проверка факта отсутствия/исчезновения элементов/виджетов (всех):

Полезная информация: [Про наличие или отсутствие чего либо](./../../../core.api/doc/rus/IDEA.MD#Присутствие-и-отсутствие)

```java
import org.openqa.selenium.WebElement;

import static java.time.temporal.ChronoUnit.*;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        boolean isElementPresent = inBrowser().absenceOf(webElements(xpath("./some/path")) //обязательно указать локатор
                .criteria(isEnabled()), //можно указать один критерий, 
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                ofSeconds(40)); //Время, за которое элементы,
        //соответствующие перечисленным критерием, должны исчезнуть, если они есть 
        // вернется true если элементов нет или за указанное время они исчезли, иначе - false

        boolean isElementPresent = inBrowser().absenceOf(webElements(xpath("./some/path")),
                ofSeconds(40),
                "Элементы все еще тут"); //Текст ошибки, которая должна быть выброшена
        // вернется true если элементов нет или за указанное время они исчезли, иначе -  
        // будет выброшено исключение с текстом 'Элементы все еще тут'

        boolean isButtonPresent = inBrowser().absenceOf(buttons()
                .criteria(isEnabled()), //можно указать один критерий, 
                // или несколько критериев посредством вызова .criteria 
                // или с использованием конкатенации AND, OR, NOT, XOR
                ofSeconds(40)); //Время, за которое кнопки,
        //соответствующие перечисленным критерием, должны исчезнуть, если они есть 
        // вернется true если кнопок нет или за указанное время они исчезли, иначе - false

        boolean isButtonPresent = inBrowser().absenceOf(buttons(),
                ofSeconds(40),
                "Кнопки все еще тут"); //Текст ошибки, которая должна быть выброшена
        // вернется true если кнопок нет или за указанное время они исчезли, иначе - 
        // будет выброшено исключение с текстом 'Кнопки все еще тут'
    }
}
```

Так же рекомендуется выполнить настройки [поиска только видимых элементов](./SETTINGS.MD#FIND_ONLY_VISIBLE_ELEMENTS).
Если этого не сделать, есть вероятность того, что поиск будет находить спрятанные/невидимые элементы. Если это не
желаемая ситуация, то придется отсекать невидимые элементы следующим образом

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

Объекты, которые возвращаются статическими методами
класса `ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier`, могут быть использованы не только для
выполнения поиска элементов на странице, но и для перечисленных действий

- [Клик по элементам](./CLICK.MD)
- [Редактирование элементов](./EDIT.MD)
- [Свертывание и развертывание](./EXPAND_COLLAPSE.MD)
- [Получение значений элементов](./ELEMENT_VALUE.MD)
- [Получение значений атрибутов элементов](./ELEMENT_ATTR.MD)
- [Получение значений CSS элементов](./ELEMENT_CSS.MD)
- [Интерактивные действия](./INTERACTION.MD)

## Поиск по тексту и меткам

- Поиск нетипизированного элемента (`org.openqa.selenium.WebElement`) на странице:

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
        WebElement e = inBrowser().find(webElement(xpath("./some/path"), "Some text")
                //данный пример означает, что искомый элемент должен иметь текст "Some text"
                //т.е. вызов метода WebElement#getText должен возвращать строку, равную "Some text"
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));
    }
}
```

- Поиск множества нетипизированных элементов (`org.openqa.selenium.WebElement`) на странице:

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
        List<WebElement> elements = inBrowser().find(webElements(xpath("./some/path"), "Some text")
                //данный пример означает, что каждый искомый элемент должен иметь текст "Some text"
                //т.е. вызов метода WebElement#getText должен возвращать строку, равную "Some text"
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));
    }
}
```

- Пример поиска виджета:

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

import static java.time.temporal.ChronoUnit.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        Button button = inBrowser().find(button("Some button")
                //данный пример означает, что искомая кнопка должна иметь текст "Some button"
                //т.е. вызов метода Widget#getText должен возвращать строку, равную "Some button"
                //Так же это означает, что если какой-либо класс, 
                // расширяющий ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button,
                // использует метки, то одна из меток должна возвращать строку, равную "Some button"
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));
    }
}
```

- Пример поиска множества виджетов:

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
        List<Button> buttons = inBrowser().find(buttons("Some button")
                //данный пример означает, что каждая искомая кнопка должна иметь текст "Some button"
                //т.е. вызов метода Widget#getText должен возвращать строку, равную "Some button"
                //Так же это означает, что если какой-либо класс, 
                // расширяющий ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button,
                // использует метки, то одна из меток должна возвращать строку, равную "Some button"
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));
    }
}
```

Про метки описано в главе о [создании виджетов](./WIDGET.MD#Создание-виджета)

## Поиск вложенных элементов

Допустим, необходимо найти некоторые элементы, расположенные внутри некоторого родительского элемента. Пусть это будет
элемент, который можно найти по тэгу `form`.

### Поиск вложенных элементов с одновременным поиском родительского элемента.

Ситуация, когда сам родительский элемент на момент начала поиска еще не найден.

- Поиск нетипизированного элемента (`org.openqa.selenium.WebElement`) на странице:

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
        //пример поиска от другого WebElement'а
        WebElement e = inBrowser().find(webElement(xpath("./some/path"))
                .foundFrom(webElement(tagName("form"))) //ищем от другого WebElement'а, который еще не найден,
                //но будет найден в процессе выполнения поиска.
                //от этого промежуточного элемента будет получен тот, который необходим
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));

        //пример поиска от виджета
        WebElement e2 = inBrowser().find(webElement(xpath("./some/path"))
                .foundFrom(form()) //ищем от элемента, описанного одним из классов,
                //которые расширяют ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Form
                //Этот элемент на момент начала поиска еще не найден,
                //но будет найден в процессе выполнения поиска.
                //от этого промежуточного элемента будет получен тот, который необходим
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));

    }
}
```

- Поиск множества нетипизированных элементов (`org.openqa.selenium.WebElement`) на странице:

```java
import java.util.List;

import org.openqa.selenium.WebElement;

import static java.time.temporal.ChronoUnit.*;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        //пример поиска от другого WebElement'а
        List<WebElement> elements = inBrowser().find(webElements(xpath("./some/path"))
                .foundFrom(webElement(tagName("form"))) //ищем от другого WebElement'а, который еще не найден,
                //но будет найден в процессе выполнения поиска.
                //от этого промежуточного элемента будет получен 
                // результирующий лист элементов
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));

        //пример поиска от виджета
        List<WebElement> elements2 = inBrowser().find(webElements(xpath("./some/path"))
                .foundFrom(form()) //ищем от элемента, описанного одним из классов,
                //которые расширяют ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Form
                //Этот элемент на момент начала поиска еще не найден,
                //но будет найден в процессе выполнения поиска.
                //от этого промежуточного элемента будет получен 
                // результирующий лист элементов
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));
    }
}
```

- Пример поиска виджета:

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

import static java.time.temporal.ChronoUnit.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        //пример поиска от другого WebElement'а
        Button button = inBrowser().find(button()
                .foundFrom(webElement(tagName("form"))) //ищем кнопку от другого WebElement'а, 
                // который еще не найден,
                //но будет найден в процессе выполнения поиска.
                //от этого промежуточного элемента будет получена искомая кнопка
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));

        //пример поиска от виджета
        Button button2 = inBrowser().find(button()
                .foundFrom(form()) //ищем от элемента, описанного одним из классов,
                //которые расширяют ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Form
                //Этот элемент на момент начала поиска еще не найден,
                //но будет найден в процессе выполнения поиска.
                //от этого промежуточного элемента будет получена искомая кнопка
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));
    }
}
```

- Пример поиска множества виджетов:

```java
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

import java.util.List;

import static java.time.temporal.ChronoUnit.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        //пример поиска от другого WebElement'а
        List<Button> buttons = inBrowser().find(buttons()
                .foundFrom(webElement(tagName("form"))) //ищем кнопки от другого WebElement'а, 
                // который еще не найден,
                //но будет найден в процессе выполнения поиска.
                //от этого промежуточного элемента будет получен 
                // результирующий лист кнопок
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));

        //пример поиска от виджета
        List<Button> buttons2 = inBrowser().find(buttons()
                .foundFrom(form()) //ищем от элемента, описанного одним из классов,
                //которые расширяют ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Form
                //Этот элемент на момент начала поиска еще не найден,
                //но будет найден в процессе выполнения поиска.
                //от этого промежуточного элемента будет получен 
                // результирующий лист кнопок
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));
    }
}
```

### Поиск вложенных элементов от родительского элемента, который уже найден

- Поиск нетипизированного элемента (`org.openqa.selenium.WebElement`) на странице:

```java
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Form;

import static java.time.temporal.ChronoUnit.*;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        WebElement formElement = inBrowser().find(webElement(tagName("form")));
        Form form = inBrowser().find(form());

        //пример поиска от другого WebElement'а
        WebElement e = inBrowser().find(webElement(xpath("./some/path"))
                .foundFrom(formElement)
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));

        //пример поиска от виджета
        WebElement e2 = inBrowser().find(webElement(xpath("./some/path"))
                .foundFrom(form)
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));

    }
}
```

- Поиск множества нетипизированных элементов (`org.openqa.selenium.WebElement`) на странице:

```java
import java.util.List;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Form;

import static java.time.temporal.ChronoUnit.*;
import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        WebElement formElement = inBrowser().find(webElement(tagName("form")));
        Form form = inBrowser().find(form());

        //пример поиска от другого WebElement'а
        List<WebElement> elements = inBrowser().find(webElements(xpath("./some/path"))
                .foundFrom(formElement)
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));

        //пример поиска от виджета
        List<WebElement> elements2 = inBrowser().find(webElements(xpath("./some/path"))
                .foundFrom(form)
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));
    }
}
```

- Пример поиска виджета:

```java
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Form;

import static java.time.temporal.ChronoUnit.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        WebElement formElement = inBrowser().find(webElement(tagName("form")));
        Form form = inBrowser().find(form());

        //пример поиска от другого WebElement'а
        Button button = inBrowser().find(button()
                .foundFrom(formElement)
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));

        //пример поиска от виджета
        Button button2 = inBrowser().find(button()
                .foundFrom(form)
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));
    }
}
```

- Пример поиска множества виджетов:

```java
import java.util.List;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Form;

import static java.time.temporal.ChronoUnit.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        WebElement formElement = inBrowser().find(webElement(tagName("form")));
        Form form = inBrowser().find(form());

        //пример поиска от другого WebElement'а
        List<Button> buttons = inBrowser().find(buttons()
                .foundFrom(formElement)
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));

        //пример поиска от виджета
        List<Button> buttons2 = inBrowser().find(buttons()
                .foundFrom(form)
                .criteria(isEnabled())
                .timeOut(ofSeconds(40)));
    }
}
```

## Поиск виджета

Ниже пример, показывающий самый базовый вариант, которым можно воспользоваться:

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        //- Пример поиска виджета:
        SomeWidget widget = inBrowser().find(widget(SomeWidget.class));

        //- Пример поиска множества виджетов:
        List<SomeWidget> widget = inBrowser().find(widgets(SomeWidget.class));
    }
}
```

Данный пример нагляден, но рекомендуется только в том случае, если существует некий компонент UI страницы с уникальным
поведением, и у этого компонента нет никаких "разновидностей" и модификаций.

Если использование данного виджета встречается часто, то этот вариант можно упростить следующим образом:

<details>
 <summary>Пример кастомной библиотеки поисковых функций</summary>

```java
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier;

import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public final class MyWidgetLib {

    private MyWidgetLib() {
        super();
    }

    //Для поиска одиночного объекта
    public static SearchSupplier<SomeWidget> someWidget() {
        return widget(SomeWidget.class);
    }

    //Для поиска одиночного объекта c учетом его текстового содержимого или текста одной из его меток
    public static SearchSupplier<SomeWidget> someWidget(String textOrLabel) {
        return widget(SomeWidget.class);
    }

    //Для поиска множества объектов
    public static MultipleSearchSupplier<SomeWidget> someWidgets() {
        return widgets(SomeWidget.class);
    }

    //Для поиска множества объектов c учетом их текстового содержимого или текста одной из их меток
    public static MultipleSearchSupplier<SomeWidget> someWidgets(String textOrLabel) {
        return widgets(SomeWidget.class);
    }
}
```

</details>

И тогда

<details>
 <summary>Пример использования кастомной библиотеки поисковых функций</summary>

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        //- Пример поиска виджета:
        SomeWidget widget = inBrowser().find(someWidget());

        //- Пример поиска множества виджетов:
        List<SomeWidget> widget = inBrowser().find(someWidgets());
    }
}
```

</details>

Что если существует несколько разновидностей виджета, каждая из которых может быть найдена на странице с помощью разных
локаторов? Например, у кнопки может быть много разных вариантов, и у каждого из них может быть свой уникальный локатор.
Клик по каждой из этих кнопок может содержать свои нюансы.

Тогда следует:

- описать некий абстрактный класс, описывающий поведение виджета в общем. Разновидности этого виджета описать в
  классах-наследниках:

    <details>
     <summary>Абстрактный виджет</summary>

    ```java
    import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
      
    public abstract class SomeAbstractWidget extends Widget {
        //описание общей логики и той, что будет реализована в классах-наследниках.
    }
    ```

    </details>

    <details>
     <summary>Описание разновидностей виджета</summary>

    ```java
    @FindBy(xpath="./some/path1")
    public class SomeWidget1 extends SomeAbstractWidget {
        //реализация логики
    }
    ```

    ```java
    @FindBy(xpath="./some/path2")
    public class SomeWidget2 extends SomeAbstractWidget {
        //реализация логики
    }
    ```
    </details>


- для поиска таких виджетов рекомендуется использовать абстрактный класс в качестве аргумента

    <details>
     <summary>Пример кастомной библиотеки поисковых функций, адаптированной под описанную ситуацию</summary>

    ```java
    import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
    import ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier;
      
    import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
    import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;
      
    public final class MyWidgetLib {
      
        private MyWidgetLib() {
            super();
        }
      
        //Для поиска одиночного объекта
        public static SearchSupplier<SomeAbstractWidget> someWidget() {
            return widget(SomeAbstractWidget.class);
        }
      
        //Для поиска одиночного объекта c учетом его текстового содержимого или текста одной из его меток
        public static SearchSupplier<SomeAbstractWidget> someWidget(String textOrLabel) {
            return widget(SomeAbstractWidget.class);
        }
      
        //Для поиска множества объектов
        public static MultipleSearchSupplier<SomeAbstractWidget> someWidgets() {
            return widgets(SomeAbstractWidget.class);
        }
      
        //Для поиска множества объектов c учетом их текстового содержимого или текста одной из их меток
        public static MultipleSearchSupplier<SomeAbstractWidget> someWidgets(String textOrLabel) {
            return widgets(SomeAbstractWidget.class);
        }
    }
    ```
    </details>

В зависимости от контента загруженной страницы и точности заданных критериев поиска, может быть найден и возвращен
объект
`SomeWidget1` или `SomeWidget2`. Данный механизм позволяет абстрагироваться от деталей каждого из под-типов виджетов.

### Список абстрактных классов для описания виджетов, которые предоставляет Neptune

- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button` - для описания кнопок
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Flag` - для элементов, которые как бы имеют 2 состояния`включен`
  и`выключен`. От него наследуются `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Flag.CheckBox` - для описания
  чек-боксов, `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Flag.RadioButton` - для описания элементов
  типа `radioButton`, `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Flag.Toggle` - для описания переключателей.
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Image` - для описания элементов-картинок
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Link`  - для описания ссылок
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Select` - для описания элементов выбора
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.MultiSelect` - для описания элементов выбора множественного значения
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Tab` - для описания элементов-вкладок
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TextField` - для описания полей ввода текста
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Table` - для описания таблиц
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableCell` - для описания ячеек таблицы
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableFooter` - для описания подвала таблицы
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableHeader` - для описания заголовка таблицы
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableRow` - для описания строки таблицы
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Form` - для описания форм
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.ElementGroup` - для элементов, визуально группирующих другие
  элементы
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TextElement` - для элементов, чьим назначением является вывод текста
  на экран
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.CalendarWidget` - для календарей
- `ru.tinkoff.qa.neptune.selenium.api.widget.drafts.CalendarRangeWidget` - для диапазонов календарных дат

**Внимание!!!** Neptune не предоставляет наборы классов, расширяющих перечисленные абстрактные классы. Они должны быть
описаны для каждого тестируемого приложения или группы тестируемых приложений. Процесс описания виджетов
раскрыт [тут](./WIDGET.MD). Ниже примеры того, как вызвать функцию-шаг поиска каждого из перечисленных выше виджетов.

### Поиск кнопок

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        //пример простого поиска одной кнопки
        Button buttons = inBrowser().find(button());
        //пример простого поиска множества кнопок 
        List<Button> buttons = inBrowser().find(buttons());
    }
}
```

### Поиск флагов

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Flag;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        //пример простого поиска флага
        Flag flag = inBrowser().find(flags());
        //пример простого поиска множества флагов 
        List<Flag> flags = inBrowser().find(flags());
    }
}
```

Можно использовать, когда не имеет значение, что это - чекбокс, радио-баттон или тоггл. Иначе, см. примеры ниже.

#### Поиск чекбоксов

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Flag.CheckBox;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        Flag.CheckBox checkBox = inBrowser().find(checkbox());
        List<Flag.CheckBox> checkBoxes = inBrowser().find(checkBoxes());
    }
}
```

#### Поиск радио-баттонов

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Flag.RadioButton;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        Flag.RadioButton radioButton = inBrowser().find(radioButton());
        List<Flag.RadioButton> radioButtons = inBrowser().find(radioButtons());
    }
}
```

#### Поиск тогглов

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Flag.Toggle;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        Flag.Toggle toggle = inBrowser().find(toggle());
        List<Flag.Toggle> toggles = inBrowser().find(toggles());
    }
}
```

### Поиск ссылок

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Link;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        Link link = inBrowser().find(link());
        List<Link> links = inBrowser().find(links());
    }
}
```

### Поиск селектов

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Select;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        Select select = inBrowser().find(select());
        List<Select> selects = inBrowser().find(selects());
    }
}
```

### Поиск мульти-селектов

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.MultiSelect;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        MultiSelect multiSelect = inBrowser().find(multiSelect());
        List<MultiSelect> multiSelects = inBrowser().find(multiSelects());
    }
}
```

### Поиск вкладок

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Tab;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        Tab tab = inBrowser().find(tab());
        List<Tab> tabs = inBrowser().find(tabs());
    }
}
```

### Поиск текстовых полей

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TextField;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        TextField textField = inBrowser().find(textField());
        List<TextField> textFieldList = inBrowser().find(textFields());
    }
}
```

### Календари

#### Поиск календарей

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.CalendarWidget;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        CalendarWidget calendar = inBrowser().find(calendar());
        List<CalendarWidget> calendars = inBrowser().find(calendars());
    }
}
```

#### Поиск элементов ввода календарных интервалов

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.CalendarRangeWidget;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        CalendarRangeWidget calendarRange = inBrowser().find(calendarRange());
        List<CalendarRangeWidget> calendarRanges = inBrowser().find(calendarRanges());
    }
}
```

### Помск элементов таблиц

#### Поиск таблиц

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Table;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        Table table = inBrowser().find(table());
        List<Table> tables = inBrowser().find(tables());
    }
}
```

#### Поиск ячеек таблиц

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableCell;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        TableCell tableCell = inBrowser().find(tableCell());
        List<TableCell> tableCells = inBrowser().find(tableCells());
    }
}
```

#### Поиск шапок таблиц

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableHeader;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        TableHeader tableHeader = inBrowser().find(tableHeader());
        List<TableHeader> tableHeaders = inBrowser().find(tableHeaders());
    }
}
```

#### Поиск подвалов таблиц

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableFooter;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        TableFooter tableFooter = inBrowser().find(tableFooter());
        List<TableFooter> tableFooters = inBrowser().find(tableFooters());
    }
}
```

#### Поиск строк таблиц

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableRow;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        TableRow tableRow = inBrowser().find(tableRow());
        List<TableRow> tableRows = inBrowser().find(tableRows());
    }
}
```

### Поиск элементов, выводящих текст

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TextElement;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        TextElement textElement = inBrowser().find(textElement());
        List<TextElement> textElements = inBrowser().find(textElements());
    }
}
```

### Поиск форм

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Form;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        Form form = inBrowser().find(form());
        List<Form> forms = inBrowser().find(forms());
    }
}
```

### Поиск группирующих элементов

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.ElementGroup;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        ElementGroup group = inBrowser().find(group());
        List<ElementGroup> groups = inBrowser().find(groups());
    }
}
```

### Поиск картинок

```java
import java.util.List;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Image;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.*;

public class Tezzt {

    @Test
    public void tezzt() {
        Image image = inBrowser().find(image());
        List<Image> images = inBrowser().find(images());
    }
}
```
