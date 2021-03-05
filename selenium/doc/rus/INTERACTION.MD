# Интерактивные действия. Имитация работы с помощью мыши и клавиатуры

О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/IDEA.MD#Шаги,-которые-возвращают-результат).

О принципах работы шагов, которые выполняют действие, можно
прочитать [здесь](./../../../core.api/doc/rus/IDEA.MD#Шаги,-которые-выполняют-действие).

Другие полезные страницы:
- [Виджеты](./WIDGET.MD)
- [Поиск элементов](./ELEMENTS_SEARCHING.MD)

Ниже описано так можно выполнять действия, имитирующие интерактивность с помощью клавиатуры/мыши с элементами страницы.

## Работа с клавишами-модификаторами

```java
import static java.time.Duration.ofSeconds;

import static org.openqa.selenium.By.*;
import static org.openqa.selenium.Keys.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        //Нажатие на клавишу-модификатор
        inBrowser().interactive(keyDown(CONTROL)
                .pauseBefore(ofSeconds(2)) //можно указать паузу до выполнения действия
                .pauseAfter(ofSeconds(2))) //можно указать паузу после выполнения действия

                //Снятие нажатия с клавиши-модификатора               
                .interactive(keyUp(CONTROL));

        //Нажатие на клавишу модификатор c выполнением фокуса на элементе,
        //который будет найден входе выполнения действия
        inBrowser().interactive(keyDown(webElement(xpath("./some/path")), CONTROL))
                //Снятие нажатия с клавиши-модификатора c выполнением фокуса на элементе,
                //который будет найден входе выполнения действия                
                .interactive(keyUp(webElement(xpath("./some/path")), CONTROL));

        //Нажатие на клавишу модификатор c выполнением фокуса на кнопке,
        //которая будет найдена входе выполнения действия
        inBrowser().interactive(keyDown(button("Push Me"), CONTROL))
                //Снятие нажатия с клавиши-модификатора c выполнением фокуса на кнопке
                //которая будет найдена входе выполнения действия
                .interactive(keyUp(button("Push Me"), CONTROL));
        
        var element = inBrowser().find(webElement(xpath("./some/path")));
        var button = inBrowser().find(button("Push Me"));

        //Нажатие на клавишу модификатор c выполнением фокуса на элементе,
        //который уже найден
        inBrowser().interactive(keyDown(element, CONTROL))
                //Снятие нажатия с клавиши-модификатора c выполнением фокуса на элементе,
                //который уже найден               
                .interactive(keyUp(element, CONTROL));

        //Нажатие на клавишу модификатор c выполнением фокуса на кнопке,
        //которая уже найдена
        inBrowser().interactive(keyDown(button, CONTROL))
                //Снятие нажатия с клавиши-модификатора c выполнением фокуса на кнопке
                //которая уже найдена
                .interactive(keyUp(button, CONTROL));
    }
}
```

## Ввод с клавиатуры

```java
import static java.time.Duration.ofSeconds;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        //Ввод с клавиатуры, работа с активным в данный момент элементом 
        inBrowser().interactive(sendKeys("Some String")
                .pauseBefore(ofSeconds(2)) //можно указать паузу до выполнения действия
                .pauseAfter(ofSeconds(2))); //можно указать паузу после выполнения действия;

        //Ввод с клавиатуры, работа с элементом,
        //который будет найден входе выполнения действия
        inBrowser().interactive(sendKeys(webElement(xpath("./some/path")), "Some String"))
                //Ввод с клавиатуры, работа с текстовым полем
                //которое будет найдено входе выполнения действия
                .interactive(sendKeys(textField("Fill Me"), "Some String"));
        
        var element = inBrowser().find(webElement(xpath("./some/path")));
        var field = inBrowser().find(textField("Fill Me"));

        //Ввод с клавиатуры, работа с элементом,
        //который уже найден
        inBrowser().interactive(sendKeys(webElement(xpath("./some/path")), "Some String"))
                //Ввод с клавиатуры, работа с текстовым полем
                //которое уже найдено
                .interactive(sendKeys(textField("Fill Me"), "Some String"));
    }
}
```

## Ввод с мышью

### Click&Hold/Release

```java
import static java.time.Duration.ofSeconds;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        //Клик с удержанием левой кнопки мыши в точке, 
        // где(должен быть) ее курсор в данный момент
        inBrowser().interactive(clickAndHold()
                .pauseBefore(ofSeconds(2)) //можно указать паузу до выполнения действия
                .pauseAfter(ofSeconds(2))) //можно указать паузу после выполнения действия

                //Освобождение удерживаемой левой кнопки мыши в точке,  
                // где(должен быть) ее курсор в данный момент
                .interactive(release());

        //Клик с удержанием левой кнопки мыши на элементе,
        //который будет найден входе выполнения действия
        inBrowser().interactive(clickAndHold(webElement(xpath("./some/path"))))
                //Освобождение удерживаемой левой кнопки мыши c выполнением фокуса на элементе,
                //который будет найден входе выполнения действия                
                .interactive(release(webElement(xpath("./some/path"))));

        //Клик с удержанием левой кнопки мыши на кнопке,
        //которая будет найдена входе выполнения действия
        inBrowser().interactive(clickAndHold(button("Push Me")))
                //Освобождение удерживаемой левой кнопки мыши c выполнением фокуса на кнопке
                //которая будет найдена входе выполнения действия
                .interactive(release(button("Push Me")));
        
        var element = inBrowser().find(webElement(xpath("./some/path")));
        var button = inBrowser().find(button("Push Me"));

        //Клик с удержанием левой кнопки мыши на элементе,
        //который уже найден
        inBrowser().interactive(clickAndHold(element))
                //Освобождение удерживаемой левой кнопки мыши c выполнением фокуса на элементе,
                //который уже найден               
                .interactive(release(element));

        //Клик с удержанием левой кнопки мыши на кнопке,
        //которая уже найдена
        inBrowser().interactive(clickAndHold(button))
                //Освобождение удерживаемой левой кнопки мыши c выполнением фокуса на кнопке
                //которая уже найдена
                .interactive(release(button));
    }
}
```

### Клик левой кнопкой мыши

```java
import static java.time.Duration.ofSeconds;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        //Клик левой кнопки мыши в точке, 
        // где(должен быть) ее курсор в данный момент
        inBrowser().interactive(click()
                .pauseBefore(ofSeconds(2)) //можно указать паузу до выполнения действия
                .pauseAfter(ofSeconds(2))); //можно указать паузу после выполнения действия

        //Клик левой кнопки мыши на элементе,
        //который будет найден входе выполнения действия
        inBrowser().interactive(click(webElement(xpath("./some/path"))));

        //Клик левой кнопки мыши на кнопке,
        //которая будет найдена входе выполнения действия
        inBrowser().interactive(click(button("Push Me")));
        
        var element = inBrowser().find(webElement(xpath("./some/path")));
        var button = inBrowser().find(button("Push Me"));

        //Клик левой кнопки мыши на элементе,
        //который уже найден
        inBrowser().interactive(click(element));

        //Клик левой кнопки мыши на кнопке,
        //которая уже найдена
        inBrowser().interactive(click(button));
    }
}
```

### Двойной клик

```java
import static java.time.Duration.ofSeconds;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        //Двойной клик в точке, 
        // где(должен быть) ее курсор в данный момент
        inBrowser().interactive(doubleClick()
                .pauseBefore(ofSeconds(2)) //можно указать паузу до выполнения действия
                .pauseAfter(ofSeconds(2))); //можно указать паузу после выполнения действия

        //Двойной клик на элементе,
        //который будет найден входе выполнения действия
        inBrowser().interactive(doubleClick(webElement(xpath("./some/path"))));

        //Двойной клик на кнопке,
        //которая будет найдена входе выполнения действия
        inBrowser().interactive(doubleClick(button("Push Me")));
        
        var element = inBrowser().find(webElement(xpath("./some/path")));
        var button = inBrowser().find(button("Push Me"));

        //Двойной клик на элементе,
        //который уже найден
        inBrowser().interactive(doubleClick(element));

        //Двойной клик на кнопке,
        //которая уже найдена
        inBrowser().interactive(doubleClick(button));
    }
}
```

### Контекстны клик (правой кнопкой мыши)

```java
import static java.time.Duration.ofSeconds;

import static org.openqa.selenium.By.*;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.intreraction.InteractiveAction.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;

public class Tezzt {
    
    @Test
    public void test() {
        //Контекстный клик в точке, 
        // где(должен быть) ее курсор в данный момент
        inBrowser().interactive(contextClick()
                .pauseBefore(ofSeconds(2)) //можно указать паузу до выполнения действия
                .pauseAfter(ofSeconds(2))); //можно указать паузу после выполнения действия

        //Контекстный клик на элементе,
        //который будет найден входе выполнения действия
        inBrowser().interactive(contextClick(webElement(xpath("./some/path"))));

        //Контекстный клик на кнопке,
        //которая будет найдена входе выполнения действия
        inBrowser().interactive(contextClick(button("Push Me")));
        
        var element = inBrowser().find(webElement(xpath("./some/path")));
        var button = inBrowser().find(button("Push Me"));

        //Контекстный клик на элементе,
        //который уже найден
        inBrowser().interactive(contextClick(element));

        //Контекстный клик на кнопке,
        //которая уже найдена
        inBrowser().interactive(contextClick(button));
    }
}
```