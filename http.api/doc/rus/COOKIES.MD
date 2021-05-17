# Работа с cookies

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст). 
  Объект [контекста для работы с http протоколом](https://tinkoffcreditsystems.github.io/neptune/http.api/ru/tinkoff/qa/neptune/http/api/HttpStepContext.html)
  в примерах ниже вызывается статическим методом `ru.tinkoff.qa.neptune.http.api.HttpStepContext#http`

- О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

- О принципах работы шагов, которые выполняют действие, можно
прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-выполняют-действие).

- О критериях можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Критерии).

- [Критерии для http cookie](https://tinkoffcreditsystems.github.io/neptune/http.api/ru/tinkoff/qa/neptune/http/api/cookies/CommonHttpCookieCriteria.html)

## Оглавление
  * [Добавление http-cookie](#Добавление-http-cookie)
  * [Получение http-cookie](#Получение-http-cookiee)
  * [Удаление http-cookie](#Удаление-http-cookie)

## Добавление http-cookie

```java

import java.net.HttpCookie;

import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class CookieTest {

    @Test
    public void test() {
        var httpCookie1 = new HttpCookie(/*Параметры создания*/);
        var httpCookie2 = new HttpCookie(/*Параметры создания*/);

        http().addCookies(httpCookie1, httpCookie2);
    }
}
```

Так же можно передать cookie в виде строки в формате http-заголовка без имени или с именами `set-cookie`, `set-cookie2`.

```java

import java.net.HttpCookie;
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class CookieTest {

    @Test
    public void test() {
        //К какому URI привязать прочитанные http-cookie
        //Так же можно передать как String или java.net.URL
        URI linkCookiesTo = URI.create("http://my.api.root.com");
        http().addCookies(linkCookiesTo, "set-cookie: cookieName=cookieValue;");
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Получение http-cookie

Ниже примеры того, как получить непросроченные http cookie.

```java

import java.net.HttpCookie;
import java.util.List;

import static ru.tinkoff.qa.neptune.http.api.cookies.CommonHttpCookieCriteria.httpCookieName;
import static ru.tinkoff.qa.neptune.http.api.cookies.CommonHttpCookieCriteria.httpCookieValue;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class CookieTest {

    @Test
    public void test() {
        //Получить все непросроченные http-cookie
        List<HttpCookie> cookieList = http().getCookies();
        
        //Получить все непросроченные http-cookie, соответствующие указанным критериям
        List<HttpCookie> cookieList2 = http().getCookies(httpCookieName("cookieName"),
                httpCookieValue("cookieValue"));

        URI linkedTo = URI.create("http://my.api.root.com");
        
        //Получить все непросроченные http-cookie, связанные с переданным URI
        //Так же linkedTo может быть передан как String или java.net.URL
        List<HttpCookie> cookieList3 = http().getCookies(linkedTo);

        //Получить все непросроченные http-cookie, связанные с переданным URI 
        //и соответствующие указанным критериям
        //Так же linkedTo может быть передан как String или java.net.URL
        List<HttpCookie> cookieList4 = http().getCookies(linkedTo,
                httpCookieName("cookieName"),
                httpCookieValue("cookieValue"));        
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Удаление http-cookie

Ниже примеры того, как очистить http cookie.


```java
import java.net.HttpCookie;
import java.util.List;

import static ru.tinkoff.qa.neptune.http.api.cookies.CommonHttpCookieCriteria.httpCookieName;
import static ru.tinkoff.qa.neptune.http.api.cookies.CommonHttpCookieCriteria.httpCookieValue;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class CookieTest {

    @Test
    public void test() {
        //Удаляем все имеющиеся непросроченные http-cookie
        http().removeCookies();

        //Удаляем все имеющиеся непросроченные http-cookie, соответствующие указанным критериям
        http().removeCookies(httpCookieName("cookieName"),
                httpCookieValue("cookieValue"));

        URI linkedTo = URI.create("http://my.api.root.com");

        //Удаляем все имеющиеся непросроченные http-cookie, связанные с переданным URI
        //Так же linkedTo может быть передан как String или java.net.URL
        http().removeCookies(linkedTo);

        //Удаляем все имеющиеся непросроченные http-cookie, связанные с переданным URI
        //и соответствующие указанным критериям
        //Так же linkedTo может быть передан как String или java.net.URL
        http().removeCookies(linkedTo,
                httpCookieName("cookieName"),
                httpCookieValue("cookieValue"));

        List<HttpCookie> cookieList;//Представим, что значение как-то присвоено
        var httpCookie1;//Представим, что значение как-то присвоено
        var httpCookie2;//Представим, что значение как-то присвоено
        
        //Прямое удаление http-cookie;
        http().removeCookies(cookieList);
        http().removeCookies(httpCookie1, httpCookie2);
        //Удаляются http cookie, переданные в виде строки в формате http-заголовка 
        // без имени или с именами `set-cookie`, `set-cookie2`
        http().removeCookies("set-cookie: cookieName=cookieValue;");        
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
