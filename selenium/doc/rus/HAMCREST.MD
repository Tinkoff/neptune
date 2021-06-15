# Mатчеры

Краткая документация [hamcrest.org](http://hamcrest.org/).

Ниже примеры использования матчеров в взявке с AssertThat и модулем [check](./../../../check/doc/rus/README.MD).

## Оглавление

- [Алерты](#Алерты)
- [Проксирование/запись трафика браузера](#Проксированиезапись-трафика-браузера)
    - [Заголовки запросов и ответов](#Заголовки-запросов-и-ответов)
    - [Версия протокола запросов и ответов](#Версия-протокола-запросов-и-ответов)
    - [Тело запроса и ответа](#Тело-запроса-и-ответа)
    - [Метод http-запроса](#Метод-http-запроса)
    - [Проверка query http-запроса](#Проверка-query-http-запроса)
    - [URL http-запроса](#url-http-запроса)
    - [Код ответа на запрос](#Код-ответа-на-запрос)
- [Элементы страницы](#Элементы-страницы)
    - [Доступность / Интерактивность](#Доступность--Интерактивность)
    - [Видимость](#Видимость)
    - [Текст, атрибуты, css](#Текст-атрибуты-css)
    - [Размер и расположение](#Размер-и-расположение)
    - [Значение элемента](#Значение-элемента)
    - [Ссылка](#Ссылка)
    - [Селектор. Мультиселект. Доступные опции](#Селектор-Мультиселект-Доступные-опции)
    - [Дочерние элементы](#Дочерние-элементы)
    - [Таблицы. Строки и колонки.](#Таблицы-Строки-и-колонки)
- [Окна и вкладки](#Окна-и-вкладки)
    - [Наличие окна](#Наличие-окна)
    - [Адрес загруженной страницы](#Адрес-загруженной-страницы)
    - [Заголовок загруженной страницы](#Заголовок-загруженной-страницы)
    - [Размер и положение на экране](#Размер-и-положение-на-экране)
- [Другие матчеры, которые предоставляет Neptune](#Другие-матчеры-которые-предоставляет-neptune)

## Алерты

Основная статья: [алерты](./ALERTS.MD)

```java
package org.mypack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.GetAlertSupplier.alert;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.alert.AlertHasTextMatcher.alertHasText;

public class MyTest {

    @Test(description = "Использование assertThat")
    public void test() {
        var alert = inBrowser().get(alert());
        assertThat("Алерт",
                alert,
                alertHasText("Текст алерта")); //проверка полного соответствия
        //текста матчера

        assertThat("Алерт",
                alert,
                alertHasText(containsString("Текст"))); //проверка соответствия
        //текста алерта критерию
    }

    @Test(description = "Использование check")
    public void test2() {
        var alert = inBrowser().get(alert());

        check("Алерт",
                alert,
                match(alertHasText("Текст алерта")),
                match(alertHasText(containsString("Текст")))); //проверка соответствия
        //текста алерта критерию
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Проксирование/запись трафика браузера

Основная статья: [Проксирование/запись трафика браузера](./BROWSER_PROXY.MD)

### Заголовки запросов и ответов

```java
package org.mypack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItem;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.HarEntryHasHeaders.*;

public class MyTest {

    @Test(description = "Использование assertThat")
    public void test() {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasHeader("header", "value123"))); //содержит запрос, у которого
        // указанный заголовок с указанным значением

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasHeader("header", containsString("value")))); //содержит запрос,
        // у которого указанный заголовок со значением, которое соответствует указанному критерию

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasHeader(containsString("header"), "value123"))); //содержит запрос,
        // у которого заголовок, чье имя соответствует указанному критерию, с указанным значением

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasHeader(containsString("header"), containsString("value"))));
        //содержит запрос, у которого заголовок,  чье имя соответствует указанному критерию, со значением,
        // которое соответствует указанному критерию

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasHeader("header"))); //содержит запрос, у которого
        // указанный заголовок

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasHeader(containsString("header")))); //содержит запрос,
        // у которого заголовок, чье имя соответствует указанному критерию

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasHeaderValue("value123"))); //содержит запрос, у которого
        // заголовок с указанным значением

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasHeaderValue(containsString("value")))); //содержит запрос 
        // c заголовком, у которого значение соответствует указанному критерию

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasHeader("header", "value123"))); //содержит ответ, у которого
        // указанный заголовок с указанным значением

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasHeader("header", containsString("value")))); //содержит ответ,
        // у которого указанный заголовок со значением, которое соответствует указанному критерию

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasHeader(containsString("header"), "value123"))); //содержит ответ,
        // у которого заголовок, чье имя соответствует указанному критерию, с указанным значением

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasHeader(containsString("header"), containsString("value"))));
        //содержит ответ, у которого заголовок,  чье имя соответствует указанному критерию, со значением,
        // которое соответствует указанному критерию

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasHeader("header"))); //содержит ответ, у которого
        // указанный заголовок

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasHeader(containsString("header")))); //содержит ответ,
        // у которого заголовок, чье имя соответствует указанному критерию

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasHeaderValue("value123"))); //содержит ответ, у которого
        // заголовок с указанным значением

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasHeaderValue(containsString("value")))); //содержит ответ 
        // c заголовком, у которого значение соответствует указанному критерию
    }

    @Test(description = "Использование check")
    public void test2() {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());

        check("записанный трафик",
                httpTraffic,
                match(iterableHasItem(requestHasHeader("header", "value123"))), //содержит запрос, у которого
                // указанный заголовок с указанным значением
                match(iterableHasItem(requestHasHeader("header", containsString("value")))),//содержит запрос,
                // у которого указанный заголовок со значением, которое соответствует указанному критерию
                match(iterableHasItem(requestHasHeader(containsString("header"), "value123"))), //содержит запрос,
                // у которого заголовок, чье имя соответствует указанному критерию, с указанным значением
                match(iterableHasItem(requestHasHeader(containsString("header"), containsString("value")))), //содержит запрос,
                // у которого заголовок,  чье имя соответствует указанному критерию, со значением,
                // которое соответствует указанному критерию
                match(iterableHasItem(requestHasHeader("header"))), //содержит запрос, у которого
                // указанный заголовок
                match(iterableHasItem(requestHasHeader(containsString("header")))), //содержит запрос,
                // у которого заголовок, чье имя соответствует указанному критерию
                match(iterableHasItem(requestHasHeaderValue("value123"))), //содержит запрос, у которого
                // заголовок с указанным значением
                match(iterableHasItem(requestHasHeaderValue(containsString("value")))), //содержит запрос 
                // c заголовком, у которого значение соответствует указанному критерию
                match(iterableHasItem(responseHasHeader("header", "value123"))), //содержит ответ, у которого
                // указанный заголовок с указанным значением
                match(iterableHasItem(responseHasHeader("header", containsString("value")))), //содержит ответ,
                // у которого указанный заголовок со значением, которое соответствует указанному критерию
                match(iterableHasItem(responseHasHeader(containsString("header"), "value123"))),//содержит ответ,
                // у которого заголовок, чье имя соответствует указанному критерию, с указанным значением
                match(iterableHasItem(responseHasHeader(containsString("header"), containsString("value")))),
                //содержит ответ, у которого заголовок,  чье имя соответствует указанному критерию, со значением,
                // которое соответствует указанному критерию
                match(iterableHasItem(responseHasHeader("header"))), //содержит ответ, у которого
                // указанный заголовок
                match(iterableHasItem(responseHasHeader(containsString("header")))), //содержит ответ,
                // у которого заголовок, чье имя соответствует указанному критерию
                match(iterableHasItem(responseHasHeaderValue("value123"))), //содержит ответ, у которого
                // заголовок с указанным значением
                match(iterableHasItem(responseHasHeaderValue(containsString("value"))))); //содержит ответ
                // c заголовком, у которого значение соответствует указанному критерию
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Версия протокола запросов и ответов

```java
package org.mypack;

import static java.net.http.HttpClient.Version.HTTP_1_1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItem;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.HarHasVersionMatcher.requestHasVersion;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.HarHasVersionMatcher.responseHasVersion;

public class MyTest {

    @Test(description = "Использование assertThat")
    public void test() {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasVersion(HTTP_1_1))); //содержит запрос, у которого
        // указанная версия http протокола

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasVersion(notOf(equalTo(HTTP_1_1))))); //содержит запрос, у которого
        // версия http протокола соответствует критерию

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasVersion(HTTP_1_1))); //содержит ответ, у которого
        // указанная версия http протокола

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasVersion(notOf(equalTo(HTTP_1_1))))); //содержит ответ, у которого
        // версия http протокола соответствует критерию
    }

    @Test(description = "Использование check")
    public void test2() {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());
        
        check("записанный трафик",
                httpTraffic,
                match(iterableHasItem(requestHasVersion(HTTP_1_1))), //содержит запрос, у которого
                // указанная версия http протокола
                match(iterableHasItem(requestHasVersion(HTTP_1_1))), //содержит запрос, у которого
                // указанная версия http протокола
                match(iterableHasItem(responseHasVersion(HTTP_1_1))), //содержит ответ, у которого
                // указанная версия http протокола
                match(iterableHasItem(responseHasVersion(notOf(equalTo(HTTP_1_1)))))); //содержит ответ, у которого
                // версия http протокола соответствует критерию
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Тело запроса и ответа

```java
package org.mypack;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItem;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.HasContentMatcher.*;

public class MyTest {

    @Test(description = "Использование assertThat")
    public void test() {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());

        var someExpectedBody = new SomeObjectClass();

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasBody(SomeObjectClass.class))); //содержит запрос, чье тело может быть
        // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
        // помощью объекта new ObjectMapper()

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasBody(new TypeReference<SomeObjectClass>() {}))); //содержит запрос,
        // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
        // помощью объекта new ObjectMapper()

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasBody(SomeObjectClass.class, new ObjectMapper())));
        //содержит запрос, чье тело может быть
        // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
        // помощью переданного ObjectMapper'а

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasBody(new TypeReference<SomeObjectClass>() {}, new ObjectMapper())));
        //содержит запрос,
        // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
        // помощью переданного ObjectMapper'а

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasBody(SomeObjectClass.class, equalTo(someExpectedBody))));
        //содержит запрос, чье тело может быть
        // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
        // помощью объекта new ObjectMapper(). Ожидается, что де-сериализованное тело соответствует
        // одному или нескольким (перечисленным через запятую) критериям-матчерам

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasBody(new TypeReference<SomeObjectClass>() {}, equalTo(someExpectedBody))));
        //содержит запрос,
        // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
        // помощью объекта new ObjectMapper(). Ожидается, что де-сериализованное тело соответствует
        // одному или нескольким (перечисленным через запятую) критериям-матчерам


        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasBody(SomeObjectClass.class, new ObjectMapper(), equalTo(someExpectedBody))));
        //содержит запрос, чье тело может быть
        // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
        // помощью переданного ObjectMapper'а. Ожидается, что де-сериализованное тело соответствует
        // одному или нескольким (перечисленным через запятую) критериям-матчерам

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasBody(new TypeReference<SomeObjectClass>() {},
                        new ObjectMapper(),
                        equalTo(someExpectedBody))));
        //содержит запрос,
        // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
        // помощью переданного ObjectMapper'а. Ожидается, что де-сериализованное тело соответствует
        // одному или нескольким (перечисленным через запятую) критериям-матчерам
        
        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasBody(someExpectedBody))); //содержит запрос, чье тело равно
        // указанному объекту. Де-сериализация тела происходит с помощью объекта new ObjectMapper()

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasRawBody("String ABC"))); //содержит запрос, чье тело в строковом виде 
        // соответствует переданной строке

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasRawBody(containsString("ABC")))); //содержит запрос, 
        // чье тело в строковом виде соответствует одному или нескольким перечисленным через запятую
        //матчерам-критериям
        
        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasBody(SomeObjectClass.class))); //содержит ответ, чье тело может быть
        // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
        // помощью объекта new ObjectMapper()

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasBody(new TypeReference<SomeObjectClass>() {}))); //содержит ответ,
        // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
        // помощью объекта new ObjectMapper()

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasBody(SomeObjectClass.class, new ObjectMapper())));
        //содержит ответ, чье тело может быть
        // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
        // помощью переданного ObjectMapper'а

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasBody(new TypeReference<SomeObjectClass>() {}, new ObjectMapper())));
        //содержит ответ,
        // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
        // помощью переданного ObjectMapper'а

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasBody(SomeObjectClass.class, equalTo(someExpectedBody))));
        //содержит ответ, чье тело может быть
        // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
        // помощью объекта new ObjectMapper(). Ожидается, что де-сериализованное тело соответствует
        // одному или нескольким (перечисленным через запятую) критериям-матчерам

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasBody(new TypeReference<SomeObjectClass>() {}, equalTo(someExpectedBody))));
        //содержит ответ,
        // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
        // помощью объекта new ObjectMapper(). Ожидается, что де-сериализованное тело соответствует
        // одному или нескольким (перечисленным через запятую) критериям-матчерам


        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasBody(SomeObjectClass.class, new ObjectMapper(), equalTo(someExpectedBody))));
        //содержит ответ, чье тело может быть
        // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
        // помощью переданного ObjectMapper'а. Ожидается, что де-сериализованное тело соответствует
        // одному или нескольким (перечисленным через запятую) критериям-матчерам

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasBody(new TypeReference<SomeObjectClass>() {},
                        new ObjectMapper(),
                        equalTo(someExpectedBody))));
        //содержит ответ,
        // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
        // помощью переданного ObjectMapper'а. Ожидается, что де-сериализованное тело соответствует
        // одному или нескольким (перечисленным через запятую) критериям-матчерам

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasBody(someExpectedBody))); //содержит ответ, чье тело равно
        // указанному объекту. Де-сериализация тела происходит с помощью объекта new ObjectMapper()

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasRawBody("String ABC"))); //содержит ответ, чье тело в строковом виде 
        // соответствует переданной строке

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasRawBody(containsString("ABC")))); //содержит ответ, 
        // чье тело в строковом виде соответствует одному или нескольким перечисленным через запятую
        //матчерам-критериям
    }

    @Test(description = "Использование check")
    public void test2() {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());

        var someExpectedBody = new SomeObjectClass();

        check("записанный трафик",
                httpTraffic,
                match(iterableHasItem(requestHasBody(SomeObjectClass.class))), //содержит 
                // запрос, чье тело может быть
                // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
                // помощью объекта new ObjectMapper()
                match(iterableHasItem(requestHasBody(new TypeReference<SomeObjectClass>() {}))), //содержит запрос,
                // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
                // помощью объекта new ObjectMapper()
                match(iterableHasItem(requestHasBody(SomeObjectClass.class, new ObjectMapper()))),
                //содержит запрос, чье тело может быть
                // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
                // помощью переданного ObjectMapper'а
                match(iterableHasItem(requestHasBody(new TypeReference<SomeObjectClass>() {}, new ObjectMapper()))),
                //содержит запрос,
                // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
                // помощью переданного ObjectMapper'а
                match(iterableHasItem(requestHasBody(SomeObjectClass.class, equalTo(someExpectedBody)))),
                //содержит запрос, чье тело может быть
                // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
                // помощью объекта new ObjectMapper(). Ожидается, что де-сериализованное тело соответствует
                // одному или нескольким (перечисленным через запятую) критериям-матчерам
                match(iterableHasItem(requestHasBody(new TypeReference<SomeObjectClass>() {}, equalTo(someExpectedBody)))),
                //содержит запрос,
                // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
                // помощью объекта new ObjectMapper(). Ожидается, что де-сериализованное тело соответствует
                // одному или нескольким (перечисленным через запятую) критериям-матчерам
                match(iterableHasItem(requestHasBody(SomeObjectClass.class, new ObjectMapper(), equalTo(someExpectedBody)))),
                //содержит запрос, чье тело может быть
                // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
                // помощью переданного ObjectMapper'а. Ожидается, что де-сериализованное тело соответствует
                // одному или нескольким (перечисленным через запятую) критериям-матчерам
                match(iterableHasItem(requestHasBody(new TypeReference<SomeObjectClass>() {},
                        new ObjectMapper(),
                        equalTo(someExpectedBody)))), //содержит запрос,
                // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
                // помощью переданного ObjectMapper'а. Ожидается, что де-сериализованное тело соответствует
                // одному или нескольким (перечисленным через запятую) критериям-матчерам
                match(iterableHasItem(requestHasBody(someExpectedBody))), //содержит запрос, чье тело равно
                // указанному объекту. Де-сериализация тела происходит с помощью объекта new ObjectMapper()
                match(iterableHasItem(requestHasRawBody("String ABC"))), //содержит запрос, чье тело в строковом виде
                // соответствует переданной строке
                match(iterableHasItem(requestHasRawBody(containsString("ABC")))), //содержит запрос,
                // чье тело в строковом виде соответствует одному или нескольким перечисленным через запятую
                // матчерам-критериям
                match(iterableHasItem(responseHasBody(SomeObjectClass.class))), //содержит ответ, чье тело может быть
                // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
                // помощью объекта new ObjectMapper()
                match(iterableHasItem(responseHasBody(new TypeReference<SomeObjectClass>() {}))), //содержит ответ,
                // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
                // помощью объекта new ObjectMapper()
                match(iterableHasItem(responseHasBody(SomeObjectClass.class, new ObjectMapper()))),
                //содержит ответ, чье тело может быть
                // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
                // помощью переданного ObjectMapper'а
                match(iterableHasItem(responseHasBody(new TypeReference<SomeObjectClass>() {}, new ObjectMapper()))),
                //содержит ответ,
                // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
                // помощью переданного ObjectMapper'а
                match(iterableHasItem(responseHasBody(SomeObjectClass.class, equalTo(someExpectedBody)))),
                //содержит ответ, чье тело может быть
                // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
                // помощью объекта new ObjectMapper(). Ожидается, что де-сериализованное тело соответствует
                // одному или нескольким (перечисленным через запятую) критериям-матчерам
                match(iterableHasItem(responseHasBody(new TypeReference<SomeObjectClass>() {}, equalTo(someExpectedBody)))),
                //содержит ответ,
                // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
                // помощью объекта new ObjectMapper(). Ожидается, что де-сериализованное тело соответствует
                // одному или нескольким (перечисленным через запятую) критериям-матчерам
                match(iterableHasItem(responseHasBody(SomeObjectClass.class, new ObjectMapper(), equalTo(someExpectedBody)))),
                //содержит ответ, чье тело может быть
                // десериализовано в объект класса SomeObjectClass. Де-сериализация тела происходит с
                // помощью переданного ObjectMapper'а. Ожидается, что де-сериализованное тело соответствует
                // одному или нескольким (перечисленным через запятую) критериям-матчерам
                match(iterableHasItem(responseHasBody(new TypeReference<SomeObjectClass>() {},
                        new ObjectMapper(),
                        equalTo(someExpectedBody)))),
                //содержит ответ,
                // чье тело может быть де-сериализовано в объект типа SomeObjectClass. Де-сериализация тела происходит с
                // помощью переданного ObjectMapper'а. Ожидается, что де-сериализованное тело соответствует
                // одному или нескольким (перечисленным через запятую) критериям-матчерам
                match(iterableHasItem(responseHasBody(someExpectedBody))), //содержит ответ, чье тело равно
                // указанному объекту. Де-сериализация тела происходит с помощью объекта new ObjectMapper()
                match(iterableHasItem(responseHasRawBody("String ABC"))), //содержит ответ, чье тело в строковом виде 
                // соответствует переданной строке
                match(iterableHasItem(responseHasRawBody(containsString("ABC"))))); //содержит ответ, 
        // чье тело в строковом виде соответствует одному или нескольким перечисленным через запятую
        //матчерам-критериям
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Метод http-запроса

```java
package org.mypack;

import static com.browserup.harreader.model.HttpMethod.GET;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItem;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.RequestHasMethod.requestHasMethod;

public class MyTest {

    @Test(description = "Использование assertThat")
    public void test() {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasMethod(GET))); //содержит запрос, у которого
        // указанный http-метод

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasMethod(notOf(equalTo(GET))))); //содержит запрос, у которого
        // http-метод соответствует критерию
    }

    @Test(description = "Использование check")
    public void test2() {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());
        
        check("записанный трафик",
                httpTraffic,
                match(iterableHasItem(requestHasMethod(GET))), //содержит запрос, у которого
                // указанный http-метод
                match(iterableHasItem(requestHasMethod(notOf(equalTo(GET)))))); //содержит запрос, у которого
                // http-метод соответствует критерию
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Проверка query http-запроса

```java
package org.mypack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItem;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.RequestHasQueries.requestHasQueryParameter;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.RequestHasQueries.requestHasQueryParameterValue;

public class MyTest {

    @Test(description = "Использование assertThat")
    public void test() {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasQueryParameter("header", "value123"))); //содержит запрос, у которого
        // указанный query-параметр с указанным значением

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasQueryParameter("header", containsString("value")))); //содержит запрос,
        // у которого указанный query-параметр со значением, которое соответствует указанному критерию

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasQueryParameter(containsString("header"), "value123"))); //содержит запрос,
        // у которого query-параметр, чье имя соответствует указанному критерию, с указанным значением

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasQueryParameter(containsString("header"), containsString("value"))));
        //содержит запрос, у которого query-параметр, чье имя соответствует указанному критерию, со значением,
        // которое соответствует указанному критерию

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasQueryParameter("header"))); //содержит запрос, у которого
        // указанный query-параметр

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasQueryParameter(containsString("header")))); //содержит запрос,
        // у которого query-параметр, чье имя соответствует указанному критерию

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasQueryParameterValue("value123"))); //содержит запрос, у которого
        // query-параметр с указанным значением

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasQueryParameterValue(containsString("value")))); //содержит запрос, у которого
        // query-параметр со значением, которое соответствует указанному критерию
    }

    @Test(description = "Использование check")
    public void test2() {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());

        check("записанный трафик",
                httpTraffic,
                match(iterableHasItem(requestHasQueryParameter("header", "value123"))), //содержит запрос, у которого
                // указанный query-параметр с указанным значением
                match(iterableHasItem(requestHasQueryParameter("header", containsString("value")))),//содержит запрос,
                // у которого указанный query-параметр со значением, которое соответствует указанному критерию
                match(iterableHasItem(requestHasQueryParameter(containsString("header"), "value123"))), //содержит запрос,
                // у которого query-параметр, чье имя соответствует указанному критерию, с указанным значением
                match(iterableHasItem(requestHasQueryParameter(containsString("header"), containsString("value")))), //содержит запрос,
                // у которого query-параметр,  чье имя соответствует указанному критерию, со значением,
                // которое соответствует указанному критерию
                match(iterableHasItem(requestHasQueryParameter("header"))), //содержит запрос, у которого
                // указанный query-параметр
                match(iterableHasItem(requestHasQueryParameter(containsString("header")))), //содержит запрос,
                // у которого query-параметр, чье имя соответствует указанному критерию
                match(iterableHasItem(requestHasQueryParameterValue("value123"))), //содержит запрос, у которого
                // query-параметр с указанным значением
                match(iterableHasItem(requestHasQueryParameterValue(containsString("value"))))); //содержит запрос
                // c query-параметром, у которого значение соответствует указанному критерию
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### URL http-запроса

```java
package org.mypack;

import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItem;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasHostMatcher.urlHasHost;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.RequestHasUrl.requestHasStringUrl;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.RequestHasUrl.requestHasUrl;

public class MyTest {

    @Test(description = "Использование assertThat")
    public void test() throws Exception {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());

        var url = new URL("https://www.google.com/search?q=how+to+write+e2e+test+cases");

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasStringUrl("https://www.google.com/search?q=how+to+write+e2e+test+cases")));
        //содержит запрос, url полностью соответствует указанной строке

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasStringUrl(containsString("how+to+write+e2e+test+cases"))));
        //содержит запрос, строка url которого соответствует указанному критерию

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasUrl(url)));
        //содержит запрос, url которого соответствует переданному объекту
        //java.net.URL

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(requestHasUrl(urlHasHost("www.google.com"))));
        //содержит запрос, url которого соответствует указанному критерию
    }

    @Test(description = "Использование check")
    public void test2() throws Exception {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());

        var url = new URL("https://www.google.com/search?q=how+to+write+e2e+test+cases");

        check("записанный трафик",
                httpTraffic,
                match(iterableHasItem(requestHasStringUrl("https://www.google.com/search?q=how+to+write+e2e+test+cases"))),
                //содержит запрос, url полностью соответствует указанной строке
                match(iterableHasItem(requestHasStringUrl(containsString("how+to+write+e2e+test+cases")))),
                //содержит запрос, строка url которого соответствует указанному критерию
                match(iterableHasItem(requestHasUrl(url))),//содержит запрос, url которого соответствует переданному объекту
                //java.net.URL
                match(iterableHasItem(requestHasUrl(urlHasHost("www.google.com"))))); //содержит запрос, url которого 
               // соответствует указанному критерию
    }
}
```

Подробнее о матчерах для URL/URI [тут](./../../../core.api/doc/rus/HAMCREST.MD#Матчеры-uriurl).

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Код ответа на запрос

```java
package org.mypack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.any.AnyMatcher.anyOne;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.iterableHasItem;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.browser.proxy.BrowserProxyGetStepSupplier.proxiedRequests;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy.ResponseHasStatusCode.responseHasStatusCode;

public class MyTest {

    @Test(description = "Использование assertThat")
    public void test() {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());
        
        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasStatusCode(200)));
        //содержит ответ с указанным кодом статуса

        assertThat("записанный трафик",
                httpTraffic,
                iterableHasItem(responseHasStatusCode(anyOne(is(200), is(201)))));
        //содержит ответ с кодом статуса, соответствующим переданному критерию
    }

    @Test(description = "Использование check")
    public void test2() {
        var httpTraffic = inBrowser()
                .resetProxyRecording()
                .get(proxiedRequests());

        check("записанный трафик",
                httpTraffic,
                match(iterableHasItem(responseHasStatusCode(200))),
                //содержит ответ с указанным кодом статуса
                match(iterableHasItem(responseHasStatusCode(anyOne(is(200), is(201))))));
                //содержит ответ с кодом статуса, соответствующим переданному критерию
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Элементы страницы

Основные статьи:

- [Поиск элементов страницы](./ELEMENTS_SEARCHING.MD)
- [Виджеты](./WIDGET.MD)

Внимание!!!!! Данные матчеры рекомендуется использовать вместе с подключенным [check](./../../../check/doc/rus/README.MD).

### Доступность / Интерактивность

```java
package org.mypack;

import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.xpath;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.IsElementEnabledMatcher.isEnabled;

public class MyTest {

    @Test(description = "Проверка доступности / интерактивности веб элемента")
    public void test() {
        check("Элемент страницы",
                inBrowser().find(webElement(xpath("./some/path"))),
                match(isEnabled())); //проверка того, что элемент доступен в данный момент

        check("Элемент страницы",
                inBrowser().find(webElement(xpath("./some/path"))),
                match(ofSeconds(5), isEnabled())); //проверка того, что элемент доступен
                //или станет доступен в течение 5 секунд
    }

    @Test(description = "Проверка доступности / интерактивности виджета")
    public void test2() {
        check("Элемент страницы",
                inBrowser().find(button("Нажми на меня")),
                match(isEnabled())); //проверка того, что кнопка доступна в данный момент

        check("Элемент страницы",
                inBrowser().find(button("Нажми на меня")),
                match(ofSeconds(5), isEnabled())); //проверка того, что кнопка доступна
        //или станет доступной в течение 5 секунд
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Видимость

Если выполнены настройки [поиска только видимых элементов](./SETTINGS.MD#FIND_ONLY_VISIBLE_ELEMENTS), то данный матчер
имеет смысл использовать, чтобы провалидировать видимость ранее найденного элемента после какого-либо события.

```java
package org.mypack;

import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.xpath;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.IsElementVisibleMatcher.isVisible;

public class MyTest {

    @Test(description = "Проверка видимости веб элемента")
    public void test() {
        var webElement = inBrowser().find(webElement(xpath("./some/path")));

        //дальнейшие действия

        check(webElement, match(isVisible())); //проверка того, что элемент видимый
        //или наоборот,
        check(webElement, match(ofSeconds(5), notOf(isVisible()))); // проверка того, что элемент
        // перестал быть видимым
    }

    @Test(description = "Проверка видимости виджета")
    public void test2() {
        var button = inBrowser().find(button("Нажми на меня"));

        //дальнейшие действия

        check(button, match(isVisible())); //проверка того, что кнопка видимая
        //или наоборот,
        check(button, match(ofSeconds(5), isVisible())); // проверка того, что кнопка
        //перестала быть видимой
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Текст, атрибуты, css

```java
package org.mypack;

import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.By.xpath;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasAttributeMatcher.hasAttribute;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasCssValueMatcher.hasCss;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasTextMatcher.hasText;

public class MyTest {

    @Test(description = "Проверка текста, атрибутов и css веб элемента")
    public void test() {
        check("Элемент страницы",
                inBrowser().find(webElement(xpath("./some/path"))),
                match(hasText("Some Text")), //проверка текста элемента на полное соответствие
                match(hasText(containsString("Text"))),//проверка текста элемента на соответствие
                // критерию-матчеру
                match(hasAttribute("someAttr", "attrValue")), //проверка значения атрибута
                // на полное соответствие
                match(hasAttribute("someAttr", containsString("Value"))), //проверка значения атрибута
                // на соответствие критерию-матчеру
                match(hasCss("someCss", "cssValue")), //проверка значения css на полное соответствие
                match(hasCss("someCss", containsString("Value")))); //проверка значения css
        // на соответствие критерию-матчеру
    }

    @Test(description = "Проверка текста, атрибутов и css виджета")
    public void test2() {
        check("Элемент страницы",
                inBrowser().find(button("Нажми на меня")),
                match(hasText("Нажми на меня")), //проверка текста элемента на полное соответствие
                match(hasText(containsString("Нажми"))), //проверка текста элемента на соответствие
                // критерию-матчеру
                match(hasAttribute("someAttr", "attrValue")), //проверка значения атрибута
                // на полное соответствие
                match(hasAttribute("someAttr", containsString("Value"))), //проверка значения атрибута
                // на соответствие критерию-матчеру
                match(hasCss("someCss", "cssValue")), //проверка значения css на полное соответствие
                match(hasCss("someCss", containsString("Value")))); //проверка значения css
        // на соответствие критерию-матчеру
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Размер и расположение

```java
package org.mypack;

import org.openqa.selenium.Point;

import static org.hamcrest.Matchers.greaterThan;
import static org.openqa.selenium.By.xpath;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasLocationMatcher.hasLocation;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasSizeMatcher.hasDimensionalSize;

public class MyTest {

    @Test(description = "Размер и расположение веб элемента")
    public void test() {
        check("Элемент страницы",
                inBrowser().find(webElement(xpath("./some/path"))),
                match(hasDimensionalSize(10, 10)), //проверка на полное соответствие размера
                match(hasDimensionalSize(10, greaterThan(5))), //проверка на полное соответствие ширины 
                // и соответствия высоты указанному критерию-матчеру, и далее комбинации точных значений и матчеров 
                match(hasDimensionalSize(greaterThan(5), 10)),
                match(hasDimensionalSize(greaterThan(5), greaterThan(5))),
                match(hasLocation(10, 10)), //проверка на полное соответствие расположения элемента 
                // относительно левого-верхнего края 
                match(hasLocation(10, greaterThan(5))),
                match(hasLocation(greaterThan(5), 10)),
                match(hasLocation(greaterThan(5), greaterThan(5))),
                match(hasLocation(10, 10, new Point(10, 10))), //расположение относительно указанной точки
                match(hasLocation(10, greaterThan(5), new Point(10, 10))),
                match(hasLocation(greaterThan(5), 10, new Point(10, 10))),
                match(hasLocation(greaterThan(5), greaterThan(5), new Point(10, 10))));
    }

    @Test(description = "Размер и расположение виджета")
    public void test2() {
        check("Элемент страницы",
                inBrowser().find(button("Нажми на меня")),
                match(hasDimensionalSize(10, 10)), //проверка на полное соответствие размера
                match(hasDimensionalSize(10, greaterThan(5))), //проверка на полное соответствие ширины 
                // и соответствия высоты указанному критерию-матчеру, и далее комбинации точных значений и матчеров 
                match(hasDimensionalSize(greaterThan(5), 10)),
                match(hasDimensionalSize(greaterThan(5), greaterThan(5))),
                match(hasLocation(10, 10)), //проверка на полное соответствие расположения элемента 
                // относительно левого-верхнего края 
                match(hasLocation(10, greaterThan(5))),
                match(hasLocation(greaterThan(5), 10)),
                match(hasLocation(greaterThan(5), greaterThan(5))),
                match(hasLocation(10, 10, new Point(10, 10))), //расположение относительно указанной точки
                match(hasLocation(10, greaterThan(5), new Point(10, 10))),
                match(hasLocation(greaterThan(5), 10, new Point(10, 10))),
                match(hasLocation(greaterThan(5), greaterThan(5), new Point(10, 10))));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


### Значение элемента

Основная статья: [Значение элементов страницы](./ELEMENT_VALUE.MD)

```java
package org.mypack;

import static org.hamcrest.Matchers.containsString;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableOf;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.api.widget.drafts.CalendarDay.calendarDay;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasValueMatcher.elementHasValue;

public class MyTest {

    @Test(description = "Проверка значения виджета")
    public void test() {
        check("Элемент страницы",
                inBrowser().find(textField()),
                match(elementHasValue("Some typed text")),
                match(elementHasValue(containsString("typed"))));

        check("Элемент страницы",
                inBrowser().find(checkbox()),
                match(elementHasValue(true)));

        check("Элемент страницы",
                inBrowser().find(multiSelect()),
                match(elementHasValue(iterableOf("value1", "value2", "value3"))));

        check("Элемент страницы",
                inBrowser().find(calendar()),
                match(elementHasValue(calendarDay().setDay("1").setMonth("янв").setYear("1970"))));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Ссылка

```java
package org.mypack;

import static org.hamcrest.Matchers.containsString;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasReference.hasReference;

public class MyTest {

    @Test(description = "Проверка ссылки")
    public void test() {
        check("Элемент страницы",
                inBrowser().find(link()),
                match(hasReference("https://www.google.com/search?q=how+to+write+e2e+test+cases")), //проверка
                //ссылки на полное соответствие
                match(hasReference(containsString("how+to+write+e2e+test+cases")))); //проверка
        //ссылки на соответствие критерию-матчеру
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Селектор. Мультиселект. Доступные опции

```java
package org.mypack;

import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableOf;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasOptionsMatcher.hasOptions;

public class MyTest {

    @Test(description = "Проверка опций")
    public void test() {
        check("Элемент страницы",
                inBrowser().find(select()),
                match(hasOptions("option1", "option2", "option3")), //проверка
                //опций селекта. Ожидается, что список опций включает только указанные опции
                //в перечисленном порядке
                match(hasOptions(iterableOf("option2", "option1", "option3")))); //проверка
                //опций селекта. Ожидается, что список опций соответствует критерию-матчеру

        check("Элемент страницы",
                inBrowser().find(multiSelect()),
                match(hasOptions("option1", "option2", "option3")), //проверка
                //опций мульти-селекта. Ожидается, что список опций включает только указанные опции
                //в перечисленном порядке
                match(hasOptions(iterableOf("option2", "option1", "option3")))); //проверка
                //опций селекта. Ожидается, что список опций соответствует критерию-матчеру
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Дочерние элементы

```java
package org.mypack;

import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.xpath;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableOf;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.buttons;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasChildElementMatcher.hasChildElement;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasChildElementsMatcher.hasChildElements;

public class MyTest {

    @Test(description = "Дочерние элементы веб элемента")
    public void test() {
        check("Элемент страницы",
                inBrowser().find(webElement(xpath("./some/path"))),
                match(hasChildElement(webElement(xpath("./some/path2")) //в данный момент есть дочерний элемент
                        //.timeOut(ofSeconds(10))) //тайм-аут игнорируется этим матчером
                )),
                match(ofSeconds(10), hasChildElement(webElement(xpath("./some/path2")))), //если подходящий дочерний 
                // элемент появляется не сразу, рекомендуется указывать таймаут в шаге проверки
                match(ofSeconds(10), hasChildElement(webElement(xpath("./some/path2"))), matcher1, matcher2), //можно 
                //уточнить, каким критериям-матчерам должен соответствовать дочерний элемент
                match(ofSeconds(10), hasChildElement(button(), matcher1, matcher2)), //аналогично для дочерних 
                // типизированных элементов
                match(hasChildElements(webElements(xpath("./some/path2")) //в данный момент есть дочерние элементы
                        //.timeOut(ofSeconds(10))) //тайм-аут игнорируется этим матчером
                )),
                match(ofSeconds(10), hasChildElements(webElements(xpath("./some/path2")))), //если подходящие дочерние 
                // элементы появляются не сразу, рекомендуется указывать таймаут в шаге проверки
                match(ofSeconds(10), hasChildElements(webElements(xpath("./some/path2"))), iterableOf(matcher1, matcher2)),
                //можно указать критерий-матчер, которому должен соответствовать набор дочерних элементов
                match(ofSeconds(10), hasChildElements(webElements(xpath("./some/path2"))), matcher1, matcher2), //или
                //перечислить, какому критерию-матчеру должен соответствовать каждый дочерний элемент в указанном порядке 
                match(ofSeconds(10), hasChildElements(buttons()))); //аналогично для дочерних 
        // типизированных элементов
    }

    @Test(description = "Дочерние элементы виджета")
    public void test2() {
        check("Элемент страницы",
                inBrowser().find(form()),
                match(hasChildElement(webElement(xpath("./some/path2")) //в данный момент есть дочерний элемент
                        //.timeOut(ofSeconds(10))) //тайм-аут игнорируется этим матчером
                )),
                match(ofSeconds(10), hasChildElement(webElement(xpath("./some/path2")))), //если подходящий дочерний 
                // элемент появляется не сразу, рекомендуется указывать таймаут в шаге проверки
                match(ofSeconds(10), hasChildElement(webElement(xpath("./some/path2"))), matcher1, matcher2), //можно 
                //уточнить, каким критериям-матчерам должен соответствовать дочерний элемент
                match(ofSeconds(10), hasChildElement(button(), matcher1, matcher2)), //аналогично для дочерних 
                // типизированных элементов
                match(hasChildElements(webElements(xpath("./some/path2")) //в данный момент есть дочерние элементы
                        //.timeOut(ofSeconds(10))) //тайм-аут игнорируется этим матчером
                )),
                match(ofSeconds(10), hasChildElements(webElements(xpath("./some/path2")))), //если подходящие дочерние 
                // элементы появляются не сразу, рекомендуется указывать таймаут в шаге проверки
                match(ofSeconds(10), hasChildElements(webElements(xpath("./some/path2"))), iterableOf(matcher1, matcher2)),
                //можно указать критерий-матчер, которому должен соответствовать набор дочерних элементов
                match(ofSeconds(10), hasChildElements(webElements(xpath("./some/path2"))), matcher1, matcher2), //или
                //перечислить, какому критерию-матчеру должен соответствовать каждый дочерний элемент в указанном порядке 
                match(ofSeconds(10), hasChildElements(buttons()))); //аналогично для дочерних 
        // типизированных элементов
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Таблицы. Строки и колонки.

```java
package org.mypack;

import static org.hamcrest.Matchers.containsString;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableOf;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasColumnMatcher.hasAColumn;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements.HasTableRowMatcher.hasARow;

public class MyTest {

    @Test(description = "Проверка строк и колонок таблицы")
    public void test2() {
        check("Элемент страницы",
                inBrowser().find(table()),
                match(hasARow(matcher)), //просто ожидается, что есть строка, соответствующая 
                // критерию-матчеру 
                match(hasARow(2, matcher)), //ожидается, что третья строка соответствует критерию-матчеру
                match(hasAColumn("Column 1", "Value 1", "Value 2", "Value 3")), //ожидается, что 
                //есть колонка, которая имеет указанный заголовок и значение которой состоит только из перечисленных 
                //строк в указанном порядке 
                match(hasAColumn(containsString("Column"), "Value 1", "Value 2", "Value 3")),
                //ожидается, что есть колонка, заголовок которой соответствует указанному критерию, значение которой 
                // состоит только из перечисленных строк в указанном порядке  
                match(hasAColumn("Column 1", iterableOf("Value 2", "Value 1", "Value 3"))), //ожидается, что 
                //есть колонка, которая имеет указанный заголовок, и значение которой соответствует указанному критерию 
                match(hasAColumn(containsString("Column"), iterableOf("Value 2", "Value 1", "Value 3"))));
                //ожидается, что есть колонка, заголовок которой соответствует указанному критерию, и значение которой 
                // соответствует указанному критерию 
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Окна и вкладки

Основные статьи:

- [Работа с окнами. Поиск и переключение](./WINDOWS.MD)

Внимание!!!!! Данные матчеры рекомендуется использовать вместе с подключенным [check](./../../../check/doc/rus/README.MD).

### Наличие окна

Бывает так, что пользовательские действия приводят к появлению новых окон / вкладок, и последующее продолжение выполнения 
операций в приложении приводят к закрытию ранее появившихся окон / вкладок. 

```java
package org.mypack;

import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window.IsWindowPresentMatcher.windowIsPresent;

public class MyTest {

    @Test(description = "Проверка того, что окно / вкладка все еще присутствует")
    public void test2() {
        var window = inBrowser().get(window());

        //дальнейшие действия

        check("Ранее появившееся окно",
                window,
                match(windowIsPresent()));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Адрес загруженной страницы

```java
package org.mypack;

import static org.hamcrest.Matchers.endsWith;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPathMatcher.urlHasPath;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.url.AtThePageMatcher.pageURL;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.url.AtThePageMatcher.url;

public class MyTest {

    @Test(description = "Проверка адреса загруженной страницы")
    public void test2() {
        //дальнейшие действия

        check("Текущее окно",
                inBrowser().get(currentWindow()),
                match(pageURL("https://www.google.com/search?q=how+to+write+e2e+test+cases")), //проверка того,
                // что адрес загруженной страницы полностью соответствует переданной строке
                match(pageURL(endsWith("/search?q=how+to+write+e2e+test+cases"))), //проверка того, что адрес загруженной 
                // страницы соответствует критерию-матчеру
                match(url(urlHasPath("/search")))); //проверка того, что адрес загруженной 
        // страницы соответствует критерию-матчеру для java.net.URL
    }
}
```

Подробнее о матчерах для URL/URI [тут](./../../../core.api/doc/rus/HAMCREST.MD#Матчеры-uriurl).

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Заголовок загруженной страницы

```java
package org.mypack;

import static org.hamcrest.Matchers.startsWith;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window.WindowHasTitleMatcher.windowHasTitle;

public class MyTest {

    @Test(description = "Проверка заголовка загруженной страницы")
    public void test2() {
        //дальнейшие действия

        check("Текущее окно",
                inBrowser().get(currentWindow()),
                match(windowHasTitle("Заголовок страницы")), //проверка того,
                // что заголовок загруженной страницы полностью соответствует переданной строке
                match(windowHasTitle(startsWith("Заголовок")))); //проверка того, что заголовок загруженной страницы 
        // соответствует критерию-матчеру
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

### Размер и положение на экране

```java
package org.mypack;

import static org.hamcrest.Matchers.greaterThan;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.inBrowser;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.currentWindow;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window.WindowHasPositionMatcher.windowHasPosition;
import static ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.window.WindowHasSizeMatcher.windowHasSize;

public class MyTest {

    @Test(description = "Проверка размера и расположения окна вкладки")
    public void test2() {
        //дальнейшие действия

        check("Текущее окно",
                inBrowser().get(currentWindow()),
                match(windowHasSize(1000, 1000)), //проверка на полное соответствие размера
                match(windowHasSize(1000, greaterThan(500))), //проверка на полное соответствие ширины 
                // и соответствия высоты указанному критерию-матчеру, и далее комбинации точных значений и матчеров 
                match(windowHasSize(greaterThan(500), 1000)),
                match(windowHasSize(greaterThan(500), greaterThan(500))),
                match(windowHasPosition(1000, 1000)), //проверка на полное соответствие расположения окна 
                // относительно левого-верхнего края экрана
                match(windowHasPosition(1000, greaterThan(500))),
                match(windowHasPosition(greaterThan(500), 1000)),
                match(windowHasPosition(greaterThan(500), greaterThan(500))));
    }
}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Другие матчеры, которые предоставляет Neptune

[Матчеры общего назначения](./../../../core.api/doc/rus/HAMCREST.MD)

[Матчеры для валидации ответов http](./../../../http.api/doc/rus/HAMCREST.MD)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

