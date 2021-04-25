# Работа с cookie браузера

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст). 
  Объект [контекста для браузера](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/SeleniumStepContext.html)
  в примерах ниже вызывается статическим методом `ru.tinkoff.qa.neptune.selenium.SeleniumStepContext#inBrowser`

- О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги,-которые-возвращают-результат).

- О принципах работы шагов, которые выполняют действие, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги,-которые-выполняют-действие).

- О критериях можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Критерии). 

- [Критерии для cookie браузера](https://tinkoffcreditsystems.github.io/neptune/selenium/ru/tinkoff/qa/neptune/selenium/functions/cookies/CommonBrowserCookieCriteria.html)

## Получение cookie браузера

```java
import static java.time.Duration.*;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.cookies.GetSeleniumCookieSupplier.cookies;
import static ru.tinkoff.qa.neptune.selenium.functions.cookies.CommonBrowserCookieCriteria.*;

public class Tezzt {

    @Test
    public void test() {
        var cookies = inBrowser().get(cookies()); //Получаем все cookie

        var cookies2 = inBrowser().get(cookies() //Получаем все cookie
                .criteria(cookieDomain("some.domain.com")) //которые соответствуют 
                .criteria(cookieIsSecure())  //указанным критериям
                .timeOut(ofSeconds(5))); //Можно указать время, в течение которого должны появится ожидаемые 
        // cookie браузера
    }
}
```

## Наличие или отсутствие cookie браузера

Полезная
информация: [Про наличие или отсутствие чего либо](./../../../core.api/doc/rus/STEPS.MD#Присутствие-и-отсутствие)

```java
import static java.time.Duration.ofSeconds;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.cookies.GetSeleniumCookieSupplier.cookies;

public class Tezzt {

    @Test
    public void test() {
        inBrowser().presenceOf(cookies()); //Ожидание наличия cookie. 
        // Как и в примере выше, здесь можно указать критерии и время ожидания
        // вернется true если cookie есть, и false - если нет

        boolean isAlertPresent = inBrowser().presenceOfOrThrow(cookies()); //Ожидание наличия cookie.
        // вернется true если cookie есть, если нет - 
        // будет выброшено исключение


        inBrowser().absenceOf(cookies(), ofSeconds(5)); //Ожидание отсутствия cookies. 
        //Можно указать критерии. Значение времени ожидания, переданное через .timeOut, игнорируется.
        //Время ожидания, что cookies нет, или они исчезли - 5 сек
        //вернется true если cookies нет или за 5 сек они пропали, и false - если они все еще здесь

        inBrowser().absenceOrThrow(cookies(), ofSeconds(5)); //Ожидание отсутствия cookies. 
        //Можно указать критерии. Значение времени ожидания, переданное через .timeOut, игнорируется.
        //Время ожидания, что cookies нет, или они исчезли - 5 сек
        //вернется true если cookies нет или за 5 сек они пропали.
        //Будет выброшено исключение, если cookies никуда не пропали
    }
}
```

## Добавление cookie браузера

```java
import org.openqa.selenium.Cookie;

import java.util.ArrayList;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;

public class Tezzt {

    @Test
    public void test() {

        Cookie cookie1; //создаем значение
        Cookie cookie2; //создаем значение

        inBrowser().addCookies(cookie1, cookie2);

        var cookies = new ArrayList<Cookie>();
        cookies.add(cookie1);
        cookies.add(cookie2);

        inBrowser().addCookies(cookies);
    }
}
```

## Удаление cookie браузера

```java
import static java.time.Duration.*;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.cookies.GetSeleniumCookieSupplier.cookies;
import static ru.tinkoff.qa.neptune.selenium.functions.cookies.CommonBrowserCookieCriteria.*;

public class Tezzt {

    @Test
    public void test() {
        inBrowser().removeCookies(); //Удалить все cookie

        seleniumSteps.removeCookies( //Удаляем все cookie
                cookieDomain("some.domain.com"), //которые соответствуют 
                cookieIsSecure());  //указанным критериям

        //Тоже самое что и пример выше, но с нюансом
        seleniumSteps.removeCookies(ofSeconds(5),//Удаляем все cookie
                cookieDomain("some.domain.com"), //которые соответствуют 
                cookieIsSecure());  //указанным критериям, 
        // если они появятся в течение 5 секунд

        //Можно заранее получить cookie
        var cookies = inBrowser().get(cookies() 
                .criteria(cookieDomain("some.domain.com")) 
                .criteria(cookieIsSecure()) 
                .timeOut(ofSeconds(5)));

        seleniumSteps.removeCookies(cookies); //и удалить когда надо
    }
}
```