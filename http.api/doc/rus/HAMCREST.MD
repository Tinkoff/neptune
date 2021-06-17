# Mатчеры

Краткая документация [hamcrest.org](http://hamcrest.org/).

Интеграция Neptune и нативного http-клиента Java имеет свой набор матчеров для валидации ответов на http-запросы. Ниже
примеры использования матчеров в взявке с AssertThat и модулем [check](./../../../check/doc/rus/README.MD).

## Оглавление
   * [Валидация тела ответа](#Валидация-тела-ответа)
   * [Валидация заголовков ответа](#Валидация-заголовков-ответа)
   * [Валидация кода ответа](#Валидация-кода-ответа)
   * [Валидация URI ответа](#Валидация-URI-ответа)
   * [Валидация версии протокола](#Валидация-версии-протокола)
   * [Валидация промежуточного ответа](#Валидация-промежуточного-ответа)
   * [Другие матчеры, которые предоставляет Neptune](#Другие-матчеры-которые-предоставляет-Neptune)

## Валидация тела ответа


    

Используем `AssertThat`

```java

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasBody.hasBody;

public class SomeTest {

    @Test
    public void tezzt() {
        assertThat("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                hasBody("Success")); //проверка того, что тело ответа - текст "Success"

        //так же hasBody можно комбинировать с любым матчером, подходящим по типу проверяемых объектов 
        // (в данном случае матчер для String)
        assertThat("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                hasBody(containsString("Success"))); //проверка того, что тело ответа содержит текст "Success"
        //Например ответ с телом "Successfully sent" пройдет как валидный
    }

}
```

Используем `check`

```java

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasBody.hasBody;

public class SomeTest {

    @Test
    public void tezzt() {
        check("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                match(hasBody("Success"))); //проверка того, что тело ответа - текст "Success"

        //так же hasBody можно комбинировать с любым матчером, подходящим по типу проверяемых объектов 
        // (в данном случае матчер для String)
        check("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                match(hasBody(containsString("Success")))); //проверка того, что тело ответа содержит текст "Success"
        //Например ответ с телом "Successfully sent" пройдет как валидный
    }

}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


## Валидация заголовков ответа


    

Используем `AssertThat`

```java
import static java.util.List.of;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasHeaders.hasHeader;

public class SomeTest {

    @Test
    public void tezzt() {
        var response = http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString());

        assertThat("Ответ на запрос",
                response,
                hasHeader("My-Header-To-Check1", of("Value1", "Value2", "Value3"))); //проверка, что заголовок My-Header-To-Check1
        //имеет значения, строго соответствующие перечисленным

        assertThat("Ответ на запрос",
                response,
                hasHeader("My-Header-To-Check2", hasItems("Value1", "Value2", "Value3"))); //проверка, что заголовок My-Header-To-Check2
        //содержит перечисленные значения

        assertThat("Ответ на запрос",
                response,
                hasHeader("My-Header-To-Check3", contains(equalTo("Value1"),
                        containsString("Value2"),
                        not("Value3")))); //проверка, что заголовок My-Header-To-Check3
        //имеет значения, которые в строгом порядке соответствуют перечисленным критериям

        assertThat("Ответ на запрос",
                response,
                hasHeader(conainsString("My-Header-To-Check4"), of("Value1", "Value2", "Value3")));
        //проверка, что среди заголовков есть тот, чье имя соответствует указанному критерию,
        //и имеет значения, строго соответствующие перечисленным критериям

        assertThat("Ответ на запрос",
                response,
                hasHeader(conainsString("My-Header-To-Check5"), contains(equalTo("Value1"),
                        containsString("Value2"),
                        not("Value3"))));
        //проверка, что среди заголовков есть тот, чье имя соответствует указанному критерию,
        //имеет значения, которые в строгом порядке соответствуют перечисленным критериям
    }

}
```

Используем `check`

```java

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasHeaders.hasHeader;

public class SomeTest {

    @Test
    public void tezzt() {
        check("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                match(hasHeader("My-Header-To-Check1", of("Value1", "Value2", "Value3"))), //проверка, что заголовок My-Header-To-Check1
                //имеет значения, строго соответствующие перечисленным
                match(hasHeader("My-Header-To-Check2", hasItems("Value1", "Value2", "Value3"))),//проверка, что заголовок My-Header-To-Check2
                //содержит перечисленные значения
                match(hasHeader("My-Header-To-Check3", contains(equalTo("Value1"),
                        containsString("Value2"),
                        not("Value3")))), //проверка, что заголовок My-Header-To-Check3
                //имеет значения, которые в строгом порядке соответствуют перечисленным критериям
                match(hasHeader(conainsString("My-Header-To-Check4"), of("Value1", "Value2", "Value3"))),
                //проверка, что среди заголовков есть тот, чье имя соответствует указанному критерию,
                //и имеет значения, строго соответствующие перечисленным критериям
                match(hasHeader(conainsString("My-Header-To-Check5"), contains(equalTo("Value1"),
                        containsString("Value2"),
                        not("Value3"))))); //проверка, что среди заголовков есть тот, чье имя соответствует указанному критерию,
        //имеет значения, которые в строгом порядке соответствуют перечисленным критериям
    }

}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


## Валидация кода ответа


    

Используем `AssertThat`

```java

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasStatusCode.hasStatusCode;

public class SomeTest {

    @Test
    public void tezzt() {
        assertThat("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                hasStatusCode(200)); //проверка того, что ответ с кодом 200

        //так же hasStatusCode можно комбинировать с любым матчером, подходящим для Integer
        assertThat("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                hasStatusCode(not(302))); //проверка того, что ответ с любым кодом, кроме 302
    }

}
```

Используем `check`

```java

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasStatusCode.hasStatusCode;

public class SomeTest {

    @Test
    public void tezzt() {
        check("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                match(hasStatusCode(200))); //проверка того, что ответ с кодом 200

        //так же hasStatusCode можно комбинировать с любым матчером, подходящим для Integer
        check("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                match(hasStatusCode(not(302)))); //проверка того, что ответ с любым кодом, кроме 302
    }

}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


## Валидация URI ответа

Такой матчер может быть полезен тогда, когда ожидается, что ответ получен после редиректа. В таких ситуациях URI исходного запроса
может отличаться от URI запроса, который содержит в себе полученный ответ. И этот URI надо проверить.


    

Используем `AssertThat`

```java

import java.net.URI;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasURI.hasURI;

public class SomeTest {

    @Test
    public void tezzt() {
        var expected = URI.create("http://some.host.com/intermediate/path/to/end/point");

        assertThat("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                hasURI(expected)); //проверка того, что ответ получен после запроса, 
        // URI которого совпадает с ожидаемым
    }

}
```

Используем `check`

```java

import java.net.URI;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasURI.hasURI;

public class SomeTest {

    @Test
    public void tezzt() {
        var expected = URI.create("http://some.host.com/intermediate/path/to/end/point");
        
        check("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                match(hasURI(expected))); //проверка того, что ответ получен после запроса, 
        // URI которого совпадает с ожидаемым
    }

}
```

Матчер `hasURI` может комбинироваться с матчерами для `java.net.URI`, которые [предоставляет Neptune](./../../../core.api/doc/rus/HAMCREST.MD#Матчеры-для-URIURL)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


## Валидация версии протокола


    

Используем `AssertThat`

```java

import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasVersion.hasVersion;

public class SomeTest {

    @Test
    public void tezzt() {
        assertThat("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                hasVersion(HTTP_1_1)); 
    }

}
```

Используем `check`

```java
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasVersion.hasVersion;

public class SomeTest {

    @Test
    public void tezzt() {        
        check("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                match(hasVersion(HTTP_1_1)));
    }

}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)


## Валидация промежуточного ответа

Такой матчер может быть полезен тогда, когда ожидается, что ответ получен после редиректа, и целью проверки является не конечный ответ, 
а последний перед ним. Данный матчер сочетается со всем перечисленными выше матчерами.


    

Используем `AssertThat`

```java
import static java.util.List.of;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.*;

public class SomeTest {

    @Test
    public void tezzt() {
        assertThat("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                hasPreviousResponse()); //проверка, что ответ пришел с редиректа и есть ответ от редиректа

        assertThat("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                hasPreviousResponse(hasHeader("My-Header-To-Check", hasItems("Value1", "Value2", "Value3"))));
        //и т.п
    }

}
```

Используем `check`

```java

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.check.CheckActionSupplier.check;
import static ru.tinkoff.qa.neptune.check.MatchAction.match;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.*;

public class SomeTest {

    @Test
    public void tezzt() {
        check("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                match(hasPreviousResponse())); //проверка, что ответ пришел с редиректа и есть ответ от редиректа

        check("Ответ на запрос",
                http().responseOf(GET("http://some.host.com/path/to/end/point"), ofString()),
                match(hasPreviousResponse(hasHeader("My-Header-To-Check", hasItems("Value1", "Value2", "Value3")))));
        //и т.п
    }

}
```

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)

## Другие матчеры, которые предоставляет Neptune

[Матчеры общего назначения](./../../../core.api/doc/rus/HAMCREST.MD)

[Матчеры для front end вэб-приложений](./../../../selenium/doc/rus/HAMCREST.MD)

[к оглавлению документа](#Оглавление) [к списку документов](README.MD#Оглавление)
