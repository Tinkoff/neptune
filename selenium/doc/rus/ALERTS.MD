# Работа с алертами

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст). 
  Объект [контекста для браузера](https://tinkoff.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/SeleniumStepContext.html) 
  в примерах ниже вызывается статическим методом `ru.tinkoff.qa.neptune.selenium.SeleniumStepContext#inBrowser` 

- О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

- О принципах работы шагов, которые выполняют действие, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-выполняют-действие).

- О критериях можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Критерии).

- [Критерии для алерта](https://tinkoff.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/functions/target/locator/alert/AlertCriteria.html)

## Оглавление

- [Получение алерта для дальнейшей работы](#Получение-алерта-для-дальнейшей-работы)
- [Наличие или отсутствие алерта](#Наличие-или-отсутствие-алерта)
- [Переключение в алерт](#Переключение-в-алерт)
- [Подтверждение/отклонение](#Подтверждениеотклонение)

## Получение алерта для дальнейшей работы

```java
import static java.time.Duration.*;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.GetAlertSupplier.alert;

import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.AlertCriteria.*;

public class Tezzt {

    @Test
    public void test() {
        var a = inBrowser().get(alert() //получить алерт
                .criteria(alertText("Some text")) //Указываем критерий. Это не обязательно, можно использовать при необходимости
                .timeOut(ofSeconds(ofSeconds(5))) //Указываем время ожидания 5 сек. Это не обязательно, можно использовать при необходимости
                .pollingInterval(ofMillis(500))); //Интервал между попытками получить ожидаемый интервал, в рамках указанных выше 5 сек. 
        // Это не обязательно, можно использовать при необходимости
        

        //Произойдет фокусировка на полученном алерте
    }
}
```

Время ожидания алерта необязательно указывать, т.к. по умолчанию время на поиск алерта равно 1 минуте. Если этого слишком
много или мало, тогда это время можно изменить, поменяв [настройку времени ожидания алерта](./SETTINGS.MD#Ожидание-алерта).

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Наличие или отсутствие алерта

Полезная информация: [Про наличие или отсутствие чего либо](./../../../core.api/doc/rus/STEPS.MD#Присутствие-и-отсутствие)


```java
import static java.time.Duration.ofSeconds;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.GetAlertSupplier.alert;

public class Tezzt {
    
    @Test
    public void test() {
        boolean isAlertPresent = inBrowser().presenceOf(alert()); //Ожидание наличия алерта. 
        // Как и в примере выше, здесь можно указать критерии и время ожидания
        // вернется true если алерт есть, и false - если нет

        boolean isAlertPresent = inBrowser().presenceOfOrThrow(alert()); //Ожидание наличия алерта.
        // вернется true если алерт есть, если нет - 
        // будет выброшено исключение


        inBrowser().absenceOf(alert(), ofSeconds(5)); //Ожидание отсутствия алерта. 
        //Можно указать критерии. Значение времени ожидания, переданное через .timeOut, игнорируется.
        //Время ожидания, что алерта нет, или он исчез - 5 сек
        //вернется true если алерта нет или за 5 сек он пропал, и false - если он все еще здесь

        inBrowser().absenceOrThrow(alert(), ofSeconds(5)); //Ожидание отсутствия алерта. 
        //Можно указать критерии. Значение времени ожидания, переданное через .timeOut, игнорируется.
        //Время ожидания, что алерта нет, или он исчез - 5 сек
        //вернется true если алерта нет или за 5 сек он пропал.
        //Будет выброшено исключение в противном случае.
    }
}
```
[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Переключение в алерт

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.GetAlertSupplier.alert;

public class Tezzt {
    
    @Test
    public void test() {
        inBrowser().switchTo(alert()); //Переключение в алерт.
        // Как и в примере выше, здесь можно указать критерии и время ожидания.
        
        //Можно заранее получить алерт
        var a = inBrowser().get(alert());
        inBrowser().switchTo(a); //и переключиться в него, когда это потребуется
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Подтверждение/отклонение

```java
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.GetAlertSupplier.alert;

public class Tezzt {
    
    @Test
    public void test() {
        inBrowser().accept(alert()); //подтверждение
        inBrowser().dismiss(alert()); //отклонение
        //Алерт, над которым выполняется действие, указан в виде алгоритма поиска/ожидания.
        //Как и в примере выше, здесь можно указать критерии и время ожидания.
        
        //Можно заранее получить алерт
        var a = inBrowser().get(alert());
        inBrowser().accept(a); //подтверждение
        inBrowser().dismiss(a); //отклонение
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)