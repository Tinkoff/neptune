## Выполнение JavaScript

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст). 
  Объект [контекста для браузера](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/SeleniumStepContext.html)
  в примерах ниже вызывается статическим методом `ru.tinkoff.qa.neptune.selenium.SeleniumStepContext#inBrowser`

- О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги,-которые-возвращают-результат).

- О критериях можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Критерии).

```java
import static java.time.Duration.*;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.javaScript;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.asynchronousJavaScript;

public class Tezzt {

    @Test
    public void test() {
        var scriptArgs = new Object[]{/**/}; //аргументы скрипта. Использовать только по необходимости 
        
        //выполнение синхронного js
        var result = inBrowser().evaluate(javaScript("js script", scriptArgs) //Скрипт и аргументы
                .criteria("Критерий ожидаемого результата", o -> {/*Предикат, описывающий критерий*/}) //при необходимости
                .timeOut(ofSeconds(5)) //время на получение ожидаемого результата. Скрипт будет выполняться пока не получится нужный результат 
                // или не кончится время. Использовать только по необходимости 
                .pollingInterval(ofMillis(500)) //Интервал времени между попытками получить ожидаемый результат в рамках времени ожидания
                .throwOnNoResult()); //Если нужный результат  не был получен, будет выброшено исключение

        //выполнение асинхронного js
        var result2 = inBrowser().evaluate(asynchronousJavaScript("js script", scriptArgs) //Скрипт и аргументы
                .criteria("Критерий ожидаемого результата", o -> {/*Предикат, описывающий критерий*/}) //при необходимости
                .timeOut(ofSeconds(5)) //время на получение ожидаемого результата. Скрипт будет выполняться пока не получится нужный результат 
                // или не кончится время. Использовать только по необходимости 
                .pollingInterval(ofMillis(500)) //Интервал времени между попытками получить ожидаемый результат в рамках времени ожидания
                .throwOnNoResult()); //Если нужный результат  не был получен, будет выброшено исключение
    }
}
```