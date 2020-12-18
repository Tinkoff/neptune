# Отправка http запросов/получение ответов

В данном документе описано на примерах, как отправлять http-запросы и получать ответы от back-end'ов тестируемых приложений,
форматы передачи информации в теле запроса и форматы получения информации в теле ответа

## Простая отправка http-запроса и получение ответа. На примерах, когда используется только URI

### POST

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем POST-запрос. Endpoint указывается в виде строки полностью.
        //Запрос без тела/с пустым телом
        //Ответ вернется без тела
        http().responseOf(POST("http://my.api.com/some/path?q1=val1&q2=val2"));
    }
}

```

```java
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем POST-запрос. Endpoint указывается в виде строки полностью.
        //Запрос с телом, переданным как текст 
        //Ответ вернется без тела
        http().responseOf(POST("http://my.api.com/some/path?q1=val1&q2=val2", "String body"));
    }
}

```

```java
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем POST-запрос. Endpoint указывается в виде строки полностью.
        //Запрос без тела/с пустым телом
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(POST("http://my.api.com/some/path?q1=val1&q2=val2"), ofString());
    }
}

```

```java
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем POST-запрос. Endpoint указывается в виде строки полностью.
        //Запрос с телом, переданным как текст 
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(POST("http://my.api.com/some/path?q1=val1&q2=val2", "String body"), ofString());
    }
}

```

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем POST-запрос. Endpoint указывается в виде URI.
        //Запрос без тела/с пустым телом
        //Ответ вернется без тела
        http().responseOf(POST(endPoint));
    }
}

```

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем POST-запрос. Endpoint указывается в виде URI.
        //Запрос с телом, переданным как текст 
        //Ответ вернется без тела
        http().responseOf(POST(endPoint, "String body"));
    }
}

```

```java
import java.net.URI;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем POST-запрос. Endpoint указывается в виде URI.
        //Запрос без тела/с пустым телом
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(POST(endPoint), ofString());
    }
}

```

```java
import java.net.URI;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем POST-запрос. Endpoint указывается в виде URI.
        //Запрос с телом, переданным как текст 
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(POST(endPoint, "String body"), ofString());
    }
}

```

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() throws Exception {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем POST-запрос. Endpoint указывается в виде URL.
        //Запрос без тела/с пустым телом
        //Ответ вернется без тела
        http().responseOf(POST(endPoint));
    }
}

```

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() throws Exception {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем POST-запрос. Endpoint указывается в виде URL.
        //Запрос с телом, переданным как текст 
        //Ответ вернется без тела
        http().responseOf(POST(endPoint, "String body"));
    }
}

```

```java
import java.net.URL;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем POST-запрос. Endpoint указывается в виде URL.
        //Запрос без тела/с пустым телом
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(POST(endPoint), ofString());
    }
}

```

```java
import java.net.URL;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем POST-запрос. Endpoint указывается в виде URL.
        //Запрос с телом, переданным как текст 
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(POST(endPoint, "String body"), ofString());
    }
}

```

</details>

### GET

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем GET-запрос. Endpoint указывается в виде строки полностью.
        //Ответ вернется без тела
        http().responseOf(GET("http://my.api.com/some/path?q1=val1&q2=val2"));
    }
}

```

```java
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем GET-запрос. Endpoint указывается в виде строки полностью.
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(GET("http://my.api.com/some/path?q1=val1&q2=val2"), ofString());
    }
}

```

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем GET-запрос. Endpoint указывается в виде URI.
        //Ответ вернется без тела
        http().responseOf(GET(endPoint));
    }
}

```

```java
import java.net.URI;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем GET-запрос. Endpoint указывается в виде URI.
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(GET(endPoint), ofString());
    }
}

```

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() throws Exception {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем GET-запрос. Endpoint указывается в виде URL.
        //Ответ вернется без тела
        http().responseOf(GET(endPoint));
    }
}

```

```java
import java.net.URL;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем GET-запрос. Endpoint указывается в виде URL.
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(GET(endPoint), ofString());
    }
}

```

</details>

### PUT

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.PUT;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем PUT-запрос. Endpoint указывается в виде строки полностью.
        //Запрос без тела/с пустым телом
        //Ответ вернется без тела
        http().responseOf(PUT("http://my.api.com/some/path?q1=val1&q2=val2"));
    }
}

```

```java
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.PUT;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем PUT-запрос. Endpoint указывается в виде строки полностью.
        //Запрос с телом, переданным как текст 
        //Ответ вернется без тела
        http().responseOf(PUT("http://my.api.com/some/path?q1=val1&q2=val2", "String body"));
    }
}

```

```java
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.PUT;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем PUT-запрос. Endpoint указывается в виде строки полностью.
        //Запрос без тела/с пустым телом
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(PUT("http://my.api.com/some/path?q1=val1&q2=val2"), ofString());
    }
}

```

```java
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.PUT;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем PUT-запрос. Endpoint указывается в виде строки полностью.
        //Запрос с телом, переданным как текст 
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(PUT("http://my.api.com/some/path?q1=val1&q2=val2", "String body"), ofString());
    }
}

```

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.PUT;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем PUT-запрос. Endpoint указывается в виде URI.
        //Запрос без тела/с пустым телом
        //Ответ вернется без тела
        http().responseOf(PUT(endPoint));
    }
}

```

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.PUT;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем PUT-запрос. Endpoint указывается в виде URI.
        //Запрос с телом, переданным как текст 
        //Ответ вернется без тела
        http().responseOf(PUT(endPoint, "String body"));
    }
}

```

```java
import java.net.URI;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.PUT;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем PUT-запрос. Endpoint указывается в виде URI.
        //Запрос без тела/с пустым телом
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(PUT(endPoint), ofString());
    }
}

```

```java
import java.net.URI;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.PUT;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем PUT-запрос. Endpoint указывается в виде URI.
        //Запрос с телом, переданным как текст 
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(PUT(endPoint, "String body"), ofString());
    }
}

```

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.PUT;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() throws Exception {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем PUT-запрос. Endpoint указывается в виде URL.
        //Запрос без тела/с пустым телом
        //Ответ вернется без тела
        http().responseOf(PUT(endPoint));
    }
}

```

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.PUT;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() throws Exception {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем PUT-запрос. Endpoint указывается в виде URL.
        //Запрос с телом, переданным как текст 
        //Ответ вернется без тела
        http().responseOf(PUT(endPoint, "String body"));
    }
}

```

```java
import java.net.URL;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.PUT;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем PUT-запрос. Endpoint указывается в виде URL.
        //Запрос без тела/с пустым телом
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(PUT(endPoint), ofString());
    }
}

```

```java
import java.net.URL;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.PUT;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем PUT-запрос. Endpoint указывается в виде URL.
        //Запрос с телом, переданным как текст 
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(PUT(endPoint, "String body"), ofString());
    }
}

```

</details>

### DELETE

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.DELETE;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем DELETE-запрос. Endpoint указывается в виде строки полностью.
        //Ответ вернется без тела
        http().responseOf(DELETE("http://my.api.com/some/path?q1=val1&q2=val2"));
    }
}

```

```java
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.DELETE;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем DELETE-запрос. Endpoint указывается в виде строки полностью.
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(DELETE("http://my.api.com/some/path?q1=val1&q2=val2"), ofString());
    }
}

```

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.DELETE;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем DELETE-запрос. Endpoint указывается в виде URI.
        //Ответ вернется без тела
        http().responseOf(DELETE(endPoint));
    }
}

```

```java
import java.net.URI;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.DELETE;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем DELETE-запрос. Endpoint указывается в виде URI.
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(DELETE(endPoint), ofString());
    }
}

```

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.DELETE;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() throws Exception {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем DELETE-запрос. Endpoint указывается в виде URL.
        //Ответ вернется без тела
        http().responseOf(DELETE(endPoint));
    }
}

```

```java
import java.net.URL;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.DELETE;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем DELETE-запрос. Endpoint указывается в виде URL.
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(DELETE(endPoint), ofString());
    }
}

```

</details>

### Любой метод

<details>
  <summary>Нажми, чтобы прочесть</summary>

Выше перечислены варианты использования типовых http-методов (POST, GET, PUT, DELETE). Но есть еще такие методы, как
TRACE, HEAD и т.п. Они реже используются. Чисто теоретически, можно реализовать свой собственный метод протокола http.
Так же, из собственной практики, встречаются нетипичные примеры использования методов POST, GET, PUT или DELETE.
Например, DELETE-метод с отправкой тела. Для подобных случаев предусмотрены следующие варианты

```java
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем некий SOME_METHOD-запрос. Endpoint указывается в виде строки полностью.
        //Запрос без тела/с пустым телом
        //Ответ вернется без тела
        http().responseOf(METHOD("SOME_METHOD", "http://my.api.com/some/path?q1=val1&q2=val2"));
    }
}

```

```java
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем некий SOME_METHOD-запрос. Endpoint указывается в виде строки полностью.
        //Запрос с телом, переданным как текст 
        //Ответ вернется без тела
        http().responseOf(METHOD("SOME_METHOD", "http://my.api.com/some/path?q1=val1&q2=val2", "String body"));
    }
}

```

```java
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем некий SOME_METHOD-запрос. Endpoint указывается в виде строки полностью.
        //Запрос без тела/с пустым телом
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(METHOD("SOME_METHOD", "http://my.api.com/some/path?q1=val1&q2=val2"), ofString());
    }
}

```

```java
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //Выполняем некий SOME_METHOD-запрос. Endpoint указывается в виде строки полностью.
        //Запрос с телом, переданным как текст 
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(METHOD("SOME_METHOD", "http://my.api.com/some/path?q1=val1&q2=val2", "String body"), ofString());
    }
}

```

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем некий SOME_METHOD-запрос. Endpoint указывается в виде URI.
        //Запрос без тела/с пустым телом
        //Ответ вернется без тела
        http().responseOf(METHOD("SOME_METHOD", endPoint));
    }
}

```

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем некий SOME_METHOD-запрос. Endpoint указывается в виде URI.
        //Запрос с телом, переданным как текст 
        //Ответ вернется без тела
        http().responseOf(METHOD("SOME_METHOD", endPoint, "String body"));
    }
}

```

```java
import java.net.URI;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем некий SOME_METHOD-запрос. Endpoint указывается в виде URI.
        //Запрос без тела/с пустым телом
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(METHOD("SOME_METHOD", endPoint), ofString());
    }
}

```

```java
import java.net.URI;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем некий SOME_METHOD-запрос. Endpoint указывается в виде URI.
        //Запрос с телом, переданным как текст 
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(METHOD("SOME_METHOD", endPoint, "String body"), ofString());
    }
}

```

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() throws Exception {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем некий SOME_METHOD-запрос. Endpoint указывается в виде URL.
        //Запрос без тела/с пустым телом
        //Ответ вернется без тела
        http().responseOf(METHOD("SOME_METHOD", endPoint));
    }
}

```

```java
import java.net.URL;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() throws Exception {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем некий SOME_METHOD-запрос. Endpoint указывается в виде URL.
        //Запрос с телом, переданным как текст 
        //Ответ вернется без тела
        http().responseOf(METHOD("SOME_METHOD", endPoint, "String body"));
    }
}

```

```java
import java.net.URL;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем некий SOME_METHOD-запрос. Endpoint указывается в виде URL.
        //Запрос без тела/с пустым телом
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(METHOD("SOME_METHOD", endPoint), ofString());
    }
}

```

```java
import java.net.URL;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.METHOD;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        //Выполняем некий SOME_METHOD-запрос. Endpoint указывается в виде URL.
        //Запрос с телом, переданным как текст 
        //Ответ вернется c телом, интерпретированным/прочитанным как текст
        http().responseOf(METHOD("SOME_METHOD", endPoint, "String body"), ofString());
    }
}

```

</details>

## Тело запроса. На примере POST запроса

Примеры выше демонстрируют, что некоторые http запросы (в основном POST и PUT) передают какую-то информацию в виде тела
запроса. Ниже описание того, в каком формате эта информация может быть передана.

### Массив байтов

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        var body = new byte[]{/*Какой-то контент*/};
        http().responseOf(POST(endPoint, body));
    }
}

```

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        var body = new byte[]{/*Какой-то контент*/};
        int requiredLength; //Нужная длина под-массива, который надо передать
        int offset; //Смещение относительно начала всего массива body
        http().responseOf(POST(endPoint, body, requiredLength, offset));
    }
}

```

</details>

### Файл

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.io.File;
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        var body = new File(/* путь к файлу или другие параметры конструктора*/);
        http().responseOf(POST(endPoint, body));
    }
}

```

```java
import java.net.URI;
import java.nio.file.Path;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        var body = Path.of(/*параметры вызова*/);
        http().responseOf(POST(endPoint, body));
    }
}

```

</details>

### Строка/Текст

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(POST(endPoint, "Some text"));
    }
}

```

```java
import java.net.URI;
import java.nio.charset.Charset;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        var charset = Charset.forName(/* Имя набора символов */);
        http().responseOf(POST(endPoint, body, charset));
    }
}

```

</details>

### Поток данных. InputStream.

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.io.InputStream;
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Вариант, когда отправляется уже где-то полученный InputStream
        InputStream inputStream; //как-то присваиваем значение
        http().responseOf(POST(endPoint, inputStream));
    }
}

```

```java
import java.io.InputStream;
import java.net.URI;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Вариант, с отложенным получением InputStream
        Supplier<InputStream> supplier = () -> {
            //алгоритм описания получения InputStream.
            //Сработает в момент отправки запроса
        };
        http().responseOf(POST(endPoint, supplier));
    }
}

```

</details>

### Форма. application/x-www-form-urlencoded.

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;

import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.JSON;
import static ru.tinkoff.qa.neptune.http.api.request.FormValueDelimiters.COMMA;
import static ru.tinkoff.qa.neptune.http.api.request.FormValueDelimiters.SPACE;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(POST(endPoint,
                //передается параметр формы param1, имеющий множественные значение "value1", "value2", "value3"
                //у которых зарезервированные символы (allowReserved=false) будут перекодированы
                formParameter("param1", false, "value1", "value2", "value3"),
                //передается параметр формы param2, имеющий множественные значение "value1@%&", "value2@%&"
                //у которых зарезервированные символы (allowReserved=true) не будут перекодированы
                formParameter("param2", true, "value1@%&", "value2@%&"),
                //передается параметр формы param3, имеющий множественные значение 1, true, "value3"
                //у которых зарезервированные символы (allowReserved=false) будут перекодированы,
                //и сами значения будут объединены в одну строку с пробелом (%20) в качестве 
                //разделителя, т.к. delimiter=SPACE
                formParameter("param3", SPACE, false, 1, true, "value3"),
                //передается параметр формы param4, имеющий множественные значение "value1@%&", "value2@%&"
                //у которых зарезервированные символы (allowReserved=true) не будут перекодированы,
                //и сами значения будут объединены в одну строку с запятой в качестве 
                //разделителя, т.к. delimiter=COMMA
                formParameter("param4", COMMA, true, "value1@%&", "value2@%&")),
                //передается параметр формы param5, у которого значение представлено в JSON-формате
                //без перекодировки зарезервированных символов
                formParameter("param5", new ObjectMapper(), someObject),
                //передается параметр формы param6, у которого значение представлено в JSON-формате
                //без перекодировки зарезервированных символов
                formParameter("param6", JSON, someObject));
    }
}
```

Для случая с `param6` см. [JSON DEFAULT_JSON_MAPPER](./SETTINGS.MD#JSON-DEFAULT_JSON_MAPPER)
и [XML DEFAULT_XML_MAPPER](./SETTINGS.MD#XML-DEFAULT_XML_MAPPER)

</details>

### org.w3c.dom.Document.

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import org.w3c.dom.Document;

import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        Document doc; //Считаем что значение как-то инициализируется
        http().responseOf(POST(endPoint, doc));
    }
}

```

```java
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        Document doc; //Считаем что значение как-то инициализируется
        Transformer transformer; //Считаем что значение как-то инициализируется
        http().responseOf(POST(endPoint, doc, transformer));
    }
}

```

</details>

### org.jsoup.nodes.Document.

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import org.jsoup.nodes.Document;

import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        Document doc; //Считаем что значение как-то инициализируется
        http().responseOf(POST(endPoint, doc));
    }
}

```

</details>

### Сериализованные объекты

<details>
  <summary>Нажми, чтобы прочесть</summary>

Объекты, как правило, сериализуются в JSON или XML строки, чтобы быть переданными в качестве тела запроса

```java
import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Сериализация объекта в JSON-строку и передача в качестве тела
        http().responseOf(POST(endPoint, new ObjectMapper(), someObject));
    }
}
```

```java
import java.net.URI;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Сериализация объекта в XML-строку и передача в качестве тела
        http().responseOf(POST(endPoint, new XmlMapper(), someObject));
    }
}
```

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.JSON;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Сериализация объекта в JSON-строку и передача в качестве тела
        http().responseOf(POST(endPoint, JSON, someObject));
    }
}
```

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.XML;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Сериализация объекта в XML-строку и передача в качестве тела
        http().responseOf(POST(endPoint, XML, someObject));
    }
}
```

Про элементы перечисления `ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper`
см. [JSON DEFAULT_JSON_MAPPER](./SETTINGS.MD#JSON-DEFAULT_JSON_MAPPER)
и [XML DEFAULT_XML_MAPPER](./SETTINGS.MD#XML-DEFAULT_XML_MAPPER)
</details>

### Multipart

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart.bodyPart;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(POST(endPoint,
                bodyPart(new byte[]{1, 2, 3}, "testBytes"),
                bodyPart(new byte[]{4, 5, 6}, "testBytes2")));
    }
}
```

```java
import java.net.URI;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart.bodyPart;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(POST(endPoint,
                randomAlphanumeric(15), //На тот случай когда разделитель нужно указать явно
                bodyPart(new byte[]{1, 2, 3}, "testBytes"),
                bodyPart(new byte[]{4, 5, 6}, "testBytes2")));
    }
}
```

</details>

#### Форматы

- **Массив байтов**
    <details>
       <summary>Нажми, чтобы прочесть</summary>

    ```java        
        import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart.bodyPart;
        
        public class MyTest {
        
            @Test
            public void myTest() {
                //просто массив
                //по умолчанию тип контента application/octet-stream
                bodyPart(new byte[] {1, 2, 3});
                //массив и имя части
                //по умолчанию тип контента application/octet-stream  
                bodyPart(new byte[] {1, 2, 3}, "partName");
                //массив, имя части и filename части
                //по умолчанию тип контента application/octet-stream  
                bodyPart(new byte[] {1, 2, 3}, "partName", "fileName.txt");
            }
        }
    ```
    </details>

- **Поток данных. InputStream**
    <details>
       <summary>Нажми, чтобы прочесть</summary>

    ```java
    import java.io.InputStream;
    
    import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart.bodyPart;
    
    public class MyTest {
    
        @Test
        public void myTest() {
            InputStream inputStream;//представим, что значение присвоено
            //простая передача
            //по умолчанию тип контента application/octet-stream  
            bodyPart(inputStream);
            //поток данных и имя части
            //по умолчанию тип контента application/octet-stream  
            bodyPart(inputStream, "partName");
            //поток данных, имя части и filename части
            //по умолчанию тип контента application/octet-stream  
            bodyPart(inputStream, "partName", "fileName.txt");
        }
    }
    ```
  </details>

- **Файл**
    <details>
       <summary>Нажми, чтобы прочесть</summary>

    ```java
    import java.io.File;
    import java.nio.file.Path;
    
    import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart.bodyPart;
    
    public class MyTest {
    
        @Test
        public void myTest() {
            File file; //представим, что значение присвоено
            Path path; //представим, что значение присвоено
    
            //простая передача файла
            //calculateContentType=true означает, что тип контента будет вычислен
            //когда значение false - указывается значение по умолчанию - application/octet-stream  
            bodyPart(file, true);
            //передача файла с указанием имени части
            //setFileName=true означает, что имя файла будет использоваться как fileMame части,
            //когда значение false - часть уходит в запрос без fileMame
            //calculateContentType=true означает, что тип контента будет вычислен
            //когда значение false - указывается значение по умолчанию - application/octet-stream  
            bodyPart(file, "partName", true,  true);
            //передача файла с указанием имени части и со свободным указанием любого filename части
            //calculateContentType=true означает, что тип контента будет вычислен
            //когда значение false - указывается значение по умолчанию - application/octet-stream  
            bodyPart(file, "partName", "fileName.txt",  true);
    
            //простая передача файла
            //calculateContentType=true означает, что тип контента будет вычислен
            //когда значение false - указывается значение по умолчанию - application/octet-stream  
            bodyPart(path, true);
            //передача файла с указанием имени части
            //setFileName=true означает, что имя файла будет использоваться как fileMame части,
            //когда значение false - часть уходит в запрос без fileMame
            //calculateContentType=true означает, что тип контента будет вычислен
            //когда значение false - указывается значение по умолчанию - application/octet-stream  
            bodyPart(path, "partName", true,  true);
            //передача файла с указанием имени части и со свободным указанием любого filename части
            //calculateContentType=true означает, что тип контента будет вычислен
            //когда значение false - указывается значение по умолчанию - application/octet-stream  
            bodyPart(path, "partName", "fileName.txt",  true);
        }
    }
    ```
    </details>

- **Строка/текст**
    <details>
       <summary>Нажми, чтобы прочесть</summary>

    ```java        
        import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart.bodyPart;
        
        public class MyTest {
        
            @Test
            public void myTest() {
                //просто текст
                bodyPart("Some text");
                //текст и имя части
                bodyPart("Some text", "partName");
                //текст, имя части и filename части
                bodyPart("Some text", "partName", "fileName.txt");
            }
        }
    ```
    </details>

- **Сериализованный объект**

    <details>
        <summary>Нажми, чтобы прочесть</summary>

    ```java
        import com.fasterxml.jackson.databind.ObjectMapper;
        import com.fasterxml.jackson.dataformat.xml.XmlMapper;

        import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.JSON;
        import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.XML;
        import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart.bodyPart;
        
        public class MyTest {

            @Test
            public void myTest() {
                //объект, сериализованный в JSON-строку 
                bodyPart(someObject, new ObjectMapper());
                //объект, сериализованный в JSON-строку, и имя части
                bodyPart(someObject, new ObjectMapper(), "partName");
                //объект, сериализованный в JSON-строку, имя части и filename части
                bodyPart(someObject, new ObjectMapper(), "partName", "fileName.json");

                //объект, сериализованный в XML-строку 
                bodyPart(someObject, new XmlMapper());
                //объект, сериализованный в XML-строку, и имя части
                bodyPart(someObject, new XmlMapper(), "partName");
                //объект, сериализованный в XML-строку, имя части и filename части
                bodyPart(someObject, new XmlMapper(), "partName", "fileName.xml");

                //объект, сериализованный в JSON-строку 
                bodyPart(someObject, JSON);
                //объект, сериализованный в JSON-строку, и имя части
                bodyPart(someObject, JSON, "partName");
                //объект, сериализованный в JSON-строку, имя части и filename части
                bodyPart(someObject, JSON, "partName", "fileName.json");

                //объект, сериализованный в XML-строку 
                bodyPart(someObject, XML);
                //объект, сериализованный в XML-строку, и имя части
                bodyPart(someObject, XML, "partName");
                //объект, сериализованный в XML-строку, имя части и filename части
                bodyPart(someObject, XML, "partName", "fileName.xml");
            }
        }
    ```
  Про элементы перечисления `ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper`
  см. [JSON DEFAULT_JSON_MAPPER](./SETTINGS.MD#JSON-DEFAULT_JSON_MAPPER)
  и [XML DEFAULT_XML_MAPPER](./SETTINGS.MD#XML-DEFAULT_XML_MAPPER)
    </details>
- **Строковое представление объекта Java**
    <details>
       <summary>Нажми, чтобы прочесть</summary>

    ```java        
        import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart.bodyPart;
        
        public class MyTest {
        
            @Test
            public void myTest() {
                //просто объект, строковое представление которого будет использовано
                bodyPart(someObject);
                //объект, строковое представление которого будет использовано, и имя части 
                bodyPart(someObject, "partName");
                //объект, строковое представление которого будет использовано, имя части и filename части 
                bodyPart(someObject, "partName", "fileName.txt");
            }
        }
    ```
    </details>

#### Дополнительные настройки

<details>
    <summary>Нажми, чтобы прочесть</summary>

```java

import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.ContentTransferEncoding.BINARY;
import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart.bodyPart;

public class MyTest {

    @Test
    public void myTest() {
        //Продемонстрированные ниже настройки опциональны,
        //их не обязательно каждый раз указывать
        bodyPart(/*Параметры*/)
                //Указывается тип контента
                //т.е. заголовок Content-Type
                .setContentType("application/octet-stream")
                //Заголовок Content-Transfer-Encoding.
                //Возможные варианты в перечислении
                //ru.tinkoff.qa.neptune.http.api.request.body.multipart.ContentTransferEncoding
                .setContentTransferEncoding(BINARY);
    }
}
```

</details>

### Описываем свой собственный формат тела запроса

<details>
  <summary>Нажми, чтобы прочесть</summary>

Предположим, у нас есть POST запрос, который принимает в качестве тела строку определенного формата, сформированную из
даты. В принципе, задача решается просто

```java
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");

        var date = new Date(); //(1)
        var format = new SimpleDateFormat(/*Параметры конструктора*/); // 2)

        http().responseOf(POST(endPoint, format.format(date)));
    }
}
```

Предположим, действия 1 и 2 встречаются в наборе тестов очень часто, и для удобства написания кода и его поддержки нужно
описать свой собственный формат тела. Тогда

```java
package org.my.pack;

import java.net.http.HttpRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;

import static java.net.http.HttpRequest.BodyPublishers.ofString;

//Объявляем класс и наследуем его от RequestBody с указанием типа, 
//который объекты которого принимаются в качестве даты
public class DateBody extends RequestBody<Date> {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat();

    protected DateBody(Date body) {
        super(body);
    }

    //необязательно писать этот метод, можно сделать конструктор public 
    public static RequestBody<Date> dateBody(Date body) {
        return new DateBody(body);
    }

    //В этом методе наше "тело" должно превратиться в объект 
    //HttpRequest.BodyPublisher
    @Override
    public HttpRequest.BodyPublisher createPublisher() {
        return ofString(FORMAT.format(super.body()));
    }

    @Override
    public String toString() {
        return FORMAT.format(super.body());
    }
}
```

И теперь

```java
import java.net.URI;
import java.util.Date;

import static org.my.pack.DateBody.dateBody;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(POST(endPoint, dateBody(new Date())));
    }
}
```

или

```java
import java.util.Date;

import static org.my.pack.DateBody.dateBody;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        http().responseOf(POST("http://my.api.com/some/path?q1=val1&q2=val2", dateBody(new Date())));
    }
}
```

или

```java
import java.net.URL;
import java.util.Date;

import static org.my.pack.DateBody.dateBody;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() throws Exception {
        var endPoint = new URL("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(POST(endPoint, dateBody(new Date())));
    }
}
```

</details>

## Отправка настроенного http-запроса. На примере GET запроса

Как настроить запрос: указать заголовки, настроить query, версия протокола и т.п.

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;

import static java.net.http.HttpClient.Version.HTTP_2;
import static ru.tinkoff.qa.neptune.http.api.request.FormValueDelimiters.PIPE;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(GET(endPoint)
                //Все перечисленное ниже не является обязательным для каждого отравляемого запроса
                //Указываем заголовок/свойство запроса и передаем множественное значение 
                .header("header1", "some value", "one more value")
                //Указываем заголовок/свойство запроса и передаем одиночное значение 
                .header("header2", "some value")
                //Указываем дополнительный параметр query-части запроса,
                //передаем множественное значение. Точно так же можем передать и одиночное значение.
                //Зарезервированные символы в переданных значениях (allowReserved=false) будут перекодированы
                .queryParam("param1", false, "val1", 3, "Hello world")
                //Указываем дополнительный параметр query-части запроса,
                //передаем множественное значение. Точно так же можем передать и одиночное значение.
                //Зарезервированные символы в переданных значениях  (allowReserved=true) не будут перекодированы
                .queryParam("param2", true, "val1%&", 3, "Hello world")
                //Указываем дополнительный параметр query-части запроса,
                //передаем множественное значение. Точно так же можем передать и одиночное значение.
                //Зарезервированные символы в переданных значениях  (allowReserved=true) не будут перекодированы.
                //Переданные значения будут объединены в одну строку с '|' (%7C) в качестве 
                //разделителя, т.к. delimiter=PIPE
                .queryParam("param3", PIPE, true, "val1%&", 3, "Hello world")
                //Время чтобы подключиться к серверу приложения 2 секунды
                .timeout(ofSeconds(2))
                //Используемая версия http протокола
                .version(HTTP_2)
                //Ожидать от сервера приложения ответ 100 (Continue) перед полной отправкой 
                //запроса
                .expectContinue(true));
    }
}
```

О вызовах методов `timeout` и `version`. Если для всех запросов используется одна и та же версия протокола, и/или
верхняя планка времени подключения к серверу приложения одна и та же, то частого вызова этих методов можно избежать.
См.[DEFAULT_HTTP_PROTOCOL_VERSION](./SETTINGS.MD#DEFAULT_HTTP_PROTOCOL_VERSION) или
[Время подключения http-клиента к сервису по умолчанию](./SETTINGS.MD#Время-подключения-http-клиента-к-сервису-по-умолчанию)

Что если одни и те же или похожие настройки для запросов встречаются часто? Тут можно порекомендовать 2 варианта
решения.

Первый вариант

```java
package org.my.pack;

import ru.tinkoff.qa.neptune.http.api.request.RequestTuner;

public class MyRequestTuner implements RequestTuner {

    //Тут можно спрятать алгоритм настройки отправляемого http-запроса  
    @Override
    public void setUp(RequestSettings<?> requestSettings) {
        //Пример ниже демонстрирует, как настроить запрос, используя постоянные значения.
        //Точно так же можно настроить запрос, используя вычислимые в данный момент значения.
        requestSettings
                .header("header1", "some value", "one more value")
                .header("header2", "some value")
                .queryParam("param1", false, "val1", 3, "Hello world")
                .queryParam("param2", true, "val1%&", 3, "Hello world")
                .queryParam("param3", PIPE, true, "val1%&", 3, "Hello world")
                .timeout(ofSeconds(2))
                .version(HTTP_2)
                .expectContinue(true);
    }
}

```

И тогда

```java
import java.net.URI;

import org.my.pack.MyRequestTuner;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(GET(endPoint)
                //вызывается общий алгоритм настройки
                .tuneWith(new MyRequestTuner()));
    }
}
```

или

```java
import java.net.URI;

import org.my.pack.MyRequestTuner;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(GET(endPoint)
                //вызывается общий алгоритм настройки
                //Можно использовать класс, если у него не объявлено никакого конструктора,
                //или объявлен public конструктор без параметров
                .tuneWith(MyRequestTuner.class));
    }
}
```

Если надо использовать более одного алгоритма настройки, которые не конфликтуют друг с другом, тогда

```java
import java.net.URI;

import org.my.pack.MyRequestTuner;
import org.my.pack.MyRequestTuner2;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(GET(endPoint)
                //вызываются общие алгоритмы настройки
                .tuneWith(new MyRequestTuner(), new MyRequestTuner2()));
    }
}
```

или

```java
//тоже самое, что пример выше, просто более читаемо написано

import java.net.URI;

import org.my.pack.MyRequestTuner;
import org.my.pack.MyRequestTuner2;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(GET(endPoint)
                //вызываются общие алгоритмы настройки
                .tuneWith(new MyRequestTuner())
                .tuneWith(new MyRequestTuner2()));
    }
}
```

или

```java
import java.net.URI;

import org.my.pack.MyRequestTuner;
import org.my.pack.MyRequestTuner2;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(GET(endPoint)
                //вызываются общие алгоритмы настройки
                //Можно использовать классы, если у них не объявлено никаких конструкторов,
                //или объявлены public конструкторы без параметров
                .tuneWith(MyRequestTuner.class, MyRequestTuner2.class));
    }
}
```

или

```java
//тоже самое, что пример выше, просто более читаемо написано

import java.net.URI;

import org.my.pack.MyRequestTuner;
import org.my.pack.MyRequestTuner2;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        http().responseOf(GET(endPoint)
                //вызываются общие алгоритмы настройки
                //Можно использовать классы, если у них не объявлено никаких конструкторов,
                //или объявлены public конструкторы без параметров
                .tuneWith(MyRequestTuner.class)
                .tuneWith(MyRequestTuner2.class));
    }
}
```

Второй вариант подробно описан в [Мэппинг http-сервисов](./MAPPING.MD)

</details>

## Получение данных тела ответа на http-запрос

### Формат тела ответа. На примере POST запроса

Интеграция Neptune с нативным Java http клиентом предоставляет возможность свободно интерпретировать информацию, которая
возвращается в теле ответа на http-запрос. Для этого используются объекты `java.net.http.HttpResponse.BodyHandler`.

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        HttpResponse<String> response = http().responseOf(POST(endPoint, "String body"),
                //Ответ вернется c телом, интерпретированным/прочитанным как текст
                ofString());

        String body = response.body();
    }
}
```

Если нужно отправить запрос, при этом тело http-ответа значения не имеет, тогда

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        //java.net.http.HttpResponse.BodyHandler не указывается.
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //На самом деле, ответ на этот запрос может иметь тело, 
        //но в данном примере оно просто не будет прочитано,
        //и вернется ответ с пустым телом
        HttpResponse<Void> response = http().responseOf(POST(endPoint, "String body"));
    }
}
```

Способы создания объектов `java.net.http.HttpResponse.BodyHandler`, которые предоставляет Java, можно
посмотреть [тут](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpResponse.BodyHandlers.html)
.

Далее список возможностей, которые предоставляет Neptune.
</details>

#### Объекты, десериализованные из JSON-строки

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.deserialized;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        HttpResponse<SomeClass> response = http().responseOf(POST(endPoint, "String body"),
                //Предполагается, что тело ответа может быть прочитано как JSON-строка,
                //которую можно десериализовать в объект класса SomeClass
                //При этом используется настроенный и переданный явно объект класса ObjectMapper
                deserialized(SomeClass.class, new ObjectMapper()));

        SomeClass body = response.body();
    }
}
```

или

```java
import java.net.URI;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.deserialized;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        HttpResponse<SomeClass> response = http().responseOf(POST(endPoint, "String body"),
                //Предполагается, что тело ответа может быть прочитано как JSON-строка,
                //которую можно десериализовать в объект SomeClass.
                //При этом используется настроенный и переданный явно объект класса ObjectMapper
                deserialized(new TypeReference<SomeClass>() {
                }, new ObjectMapper()));

        SomeClass body = response.body();
    }
}
```

или

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        HttpResponse<SomeClass> response = http().responseOf(POST(endPoint, "String body"),
                //Предполагается, что тело ответа может быть прочитано как JSON-строка,
                //которую можно десериализовать в объект класса SomeClass
                //При этом используется настроенный алгоритм сериализации/десериализации по умолчанию
                json(SomeClass.class));

        SomeClass body = response.body();
    }
}
```

или

```java
import java.net.URI;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.type.TypeReference;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        HttpResponse<SomeClass> response = http().responseOf(POST(endPoint, "String body"),
                //Предполагается, что тело ответа может быть прочитано как JSON-строка,
                //которую можно десериализовать в объект SomeClass
                //При этом используется настроенный алгоритм сериализации/десериализации по умолчанию
                json(new TypeReference<SomeClass>() {
                }));

        SomeClass body = response.body();
    }
}
```

Про два последних способа. В качестве дополнения можно
прочитать [JSON DEFAULT_JSON_MAPPER](./SETTINGS.MD#JSON-DEFAULT_JSON_MAPPER)

</details>

#### Объекты, десериализованные из XML-строки

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.deserialized;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        HttpResponse<SomeClass> response = http().responseOf(POST(endPoint, "String body"),
                //Предполагается, что тело ответа может быть прочитано как XML-строка,
                //которую можно десериализовать в объект класса SomeClass
                //При этом используется настроенный и переданный явно объект класса XmlMapper
                deserialized(SomeClass.class, new XmlMapper()));

        SomeClass body = response.body();
    }
}
```

или

```java
import java.net.URI;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.deserialized;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        HttpResponse<SomeClass> response = http().responseOf(POST(endPoint, "String body"),
                //Предполагается, что тело ответа может быть прочитано как XML-строка,
                //которую можно десериализовать в объект SomeClass
                //При этом используется настроенный и переданный явно объект класса XmlMapper
                deserialized(new TypeReference<SomeClass>() {
                }, new XmlMapper()));

        SomeClass body = response.body();
    }
}
```

или

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.xml;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        HttpResponse<SomeClass> response = http().responseOf(POST(endPoint, "String body"),
                //Предполагается, что тело ответа может быть прочитано как XML-строка,
                //которую можно десериализовать в объект класса SomeClass
                //При этом используется настроенный алгоритм сериализации/десериализации по умолчанию
                xml(SomeClass.class));

        SomeClass body = response.body();
    }
}
```

или

```java
import java.net.URI;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.core.type.TypeReference;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.xml;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        HttpResponse<SomeClass> response = http().responseOf(POST(endPoint, "String body"),
                //Предполагается, что тело ответа может быть прочитано как XML-строка,
                //которую можно десериализовать в объект SomeClass
                //При этом используется настроенный алгоритм сериализации/десериализации по умолчанию
                xml(new TypeReference<SomeClass>() {
                }));

        SomeClass body = response.body();
    }
}
```

Про два последних способа. В качестве дополнения можно
прочитать [XML DEFAULT_XML_MAPPER](./SETTINGS.MD#XML-DEFAULT_XML_MAPPER)

</details>

#### org.w3c.dom.Document

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;
import java.net.http.HttpResponse;

import org.w3c.dom.Document;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.w3cDocument;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        HttpResponse<Document> response = http().responseOf(POST(endPoint, "String body"),
                //Предполагается, что тело ответа может быть прочитано как строка,
                //которую можно преобразовать в org.w3c.dom.Document
                w3cDocument());

        Document body = response.body();
    }
}
```

или

```java
import java.net.URI;
import java.net.http.HttpResponse;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.w3cDocument;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        DocumentBuilder builder; //Представим, что значение присвоено 

        HttpResponse<Document> response = http().responseOf(POST(endPoint, "String body"),
                //Предполагается, что тело ответа может быть прочитано как строка,
                //которую можно преобразовать в org.w3c.dom.Document
                //Объект DocumentBuilder нужен для уточнения алгоритма преобразования
                w3cDocument(builder));

        Document body = response.body();
    }
}
```

</details>

#### org.jsoup.nodes.Document

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;
import java.net.http.HttpResponse;

import org.jsoup.nodes.Document;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.jsoupDocument;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        HttpResponse<Document> response = http().responseOf(POST(endPoint, "String body"),
                //Предполагается, что тело ответа может быть прочитано как строка,
                //которую можно преобразовать в org.jsoup.nodes.Document
                jsoupDocument());

        Document body = response.body();
    }
}
```

</details>

#### Описание своего формата

<details>
  <summary>Нажми, чтобы прочесть</summary>

Предположим, что при выполнении http-запроса возвращается ответ с телом, которое можно десериализовать в объект
следующего класса

```java
import com.fasterxml.jackson.annotation.JsonProperty;

public class BodyObject {

    @JsonProperty("A")
    private String a;

    @JsonProperty("B")
    private Integer b;

    @JsonProperty("C")
    private Boolean c;

    public String getA() {
        return a;
    }

    public BodyObject setA(String a) {
        this.a = a;
        return this;
    }


    public Integer getB() {
        return b;
    }

    public BodyObject setB(Integer b) {
        this.b = b;
        return this;
    }

    public Boolean getC() {
        return c;
    }

    public BodyObject setC(Boolean c) {
        this.c = c;
        return this;
    }
}
```

И нужно сделать как, как будто тело включает в себя не весь объект, а только значение поля `a`. Тогда

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.mapped;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        DocumentBuilder builder; //Представим, что значение присвоено 

        HttpResponse<String> response = http().responseOf(POST(endPoint, "String body"),
                //Тело ответа десериализуется в BodyObject, потом берется только значение поля `a`
                //Выглядит результат так, будто тело ответа включает в себя только значение поля `a`,
                //а не объект целиком
                mapped(json(BodyObject.class), BodyObject::getA));

        String body = response.body();
    }
}
```

Если похожее преобразование встречается часто, то можно

```java
package org.my.packeage;

import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler;

public class BodyFieldA() extends MappedBodyHandler<BodyObject, String> {

    //Используем конструктор базового класса
    protected BodyFieldA() {
        super(json(BodyObject.class), BodyObject::getA);
    }

    //Это делать необязательно. можно вызывать через конструктор, но он должен быть public
    public static BodyFieldA bodyFieldA() {
        return new BodyFieldA();
    }
}
```

и тогда

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static org.my.packeage.BodyFieldA.bodyFieldA;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        DocumentBuilder builder; //Представим, что значение присвоено 

        HttpResponse<String> response = http().responseOf(POST(endPoint, "String body"),
                bodyFieldA());

        String body = response.body();
    }
}
```

</details>

### Варианты получения данных тела ответа. На примере GET запроса

О принципах работы шагов, которые возвращают результат, можно
прочитать [здесь](./../../../core.api/doc/rus/IDEA.MD#Шаги,-которые-возвращают-результат).

В примерах выше был показан один из вариантов решения. Для этого нато получить ответ на http-запрос и взять его тело - `response.body()`. Но этого не всегда достаточно, т.к. бывают ситуации, когда:

- необходимо извлечь данные тела ответа, соответствующие определенным критериям
- необходимо извлечь данные тела ответа, который соответствует определенным критериям
- нужный ответ может быть получен не зразу, а спустя некоторое время (тестируемый back-end работает не синхронно с
  тестирующим его кодом)

Предположим, что GET-запрос возвращает ответ с телом, которое можно десериализовать в объект следующего класса

```java
import java.util.List;

public class BodyObject {

    private Map<String, String> a;

    private List<Integer> b;

    private Integer[] c;

    public Map<String, String> getA() {
        return a;
    }

    public BodyObject setA(Map<String, String> a) {
        this.a = a;
        return this;
    }


    public List<Integer> getB() {
        return b;
    }

    public BodyObject setB(List<Integer> b) {
        this.b = b;
        return this;
    }

    public Integer[] getC() {
        return c;
    }

    public BodyObject setC(Integer[] c) {
        this.c = c;
        return this;
    }
}
```

Примеры решений ниже

#### Получение данных тела ответа как Java-объект

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.asIs;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно

        //Извлекается тело ответа на запрос, при условии что ответ и его тело подходят под указанные 
        //критерии. Ответ на начало выполнения еще не получен, указывается тот запрос, который следует отправить.
        BodyObject o = http().bodyData(asIs(GET(endPoint), json(BodyObject.class))
                //указываем критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //указываем критерий, которому должно соответствовать тело 
                .criteria("Поле A содержит ключ 'foo' со значением 'bar'", bodyObject -> bodyObject.getA.get("foo").equals("bar"))
                //Время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и с телом подходящим под критерий
                .retryTimeOut(ofSeconds(10))
                //интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.asObject;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно

        //Извлекается фрагмент тела ответа на запрос, при условии что ответ и извлекаемые данные подходят под указанные 
        //критерии. Ответ на начало выполнения еще не получен, указывается тот запрос, который следует отправить.
        Map<String, String> o = http().bodyData(asObject("Значение поля A", //Описываем, что извлекается. Пойдет в лог/отчет
                GET(endPoint), //запрос и его параметры
                json(BodyObject.class), //в каком формате прочитать тело
                BodyObject::getA) //как извлечь из тела ответа нужные данные
                //указываем критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //указываем критерий, которому должны результирующие данные
                .criteria("Содержит ключ 'foo' со значением 'bar'", map -> map.get("foo").equals("bar"))
                //Время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и с телом подходящим под критерий
                .retryTimeOut(ofSeconds(10))
                //интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.asIs;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно
        HttpResponse<BodyObject> response = http().responseOf(GET(endPoint), json(BodyObject.class));

        //Извлекается тело ответа на запрос, при условии что оно подходит под указанные 
        //критерии. Ответ на начало выполнения уже получен
        BodyObject o = http().bodyData(asIs(response) //полученный выше ответ
                //указываем критерий, которому должно соответствовать тело 
                .criteria("Поле A содержит ключ 'foo' со значением 'bar'", bodyObject -> bodyObject.getA.get("foo").equals("bar"))
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.asObject;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно
        HttpResponse<BodyObject> response = http().responseOf(GET(endPoint), json(BodyObject.class));

        //Извлекается фрагмент тела ответа на запрос, при условии что извлекаемые данные подходят под указанные 
        //критерии. Ответ на начало выполнения уже получен
        Map<String, String> o = http().bodyData(asObject("Значение поля A", //Описываем, что извлекается. Пойдет в лог/отчет
                response, //полученный выше ответ
                BodyObject::getA) //как извлечь из тела ответа нужные данные
                //указываем критерий, которому должны результирующие данные
                .criteria("Содержит ключ 'foo' со значением 'bar'", map -> map.get("foo").equals("bar"))
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```
</details>

#### Получение данных тела ответа как Java-объект из Iterable

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromIterableBodyStepSupplier.asOneOfIterable;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.mapped;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно

        //Извлекается фрагмент тела ответа на запрос. Тело является коллекцией или объектом Iterable.
        //Данные извлекаются при условии, что ответ подходит под указанные 
        //критерии, и в коллекции-теле есть хотя бы один подходящий элемент.
        //Из коллекции вернется первый подходящий элемент.
        // 
        //Ответ на начало выполнения еще не получен, указывается тот запрос, который следует отправить.
        Integer o = http().bodyData(asOneOfIterable("Число, полученное из ответа", //Описываем, что извлекается. Пойдет в лог/отчет
                GET(endPoint),
                mapped(json(BodyObject.class), BodyObject::getB)) //представляем, будто в теле вернулась коллекция
                //указываем критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //указываем критерий, которому должен соответствовать получаемый элемент 
                .criteria("Положительное число", i -> i.intValue() > 0)
                //Время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и с хотя бы одним положительным элементом в теле
                .retryTimeOut(ofSeconds(10))
                //интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromIterableBodyStepSupplier.asOneOfIterable;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно

        //Извлекается фрагмент тела ответа на запрос. Тело не является коллекцией или объектом Iterable,
        //промежуточная коллекция или Iterable получаются в ходе дополнительных вычислений.
        //Данные извлекаются при условии, что ответ подходит под указанные 
        //критерии, и в коллекции есть хотя бы один подходящий элемент.
        //Из коллекции вернется первый подходящий элемент.
        // 
        //Ответ на начало выполнения еще не получен, указывается тот запрос, который следует отправить.
        Integer o = http().bodyData(asOneOfIterable("Значение из поля B", //Описываем, что извлекается. Пойдет в лог/отчет
                GET(endPoint), //запрос и его параметры
                json(BodyObject.class), //в каком формате прочитать тело
                BodyObject::getB) //как извлечь из тела ответа нужную промежуточную коллекцию
                //указываем критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //указываем критерий, которому должен соответствовать получаемый элемент 
                .criteria("Положительное число", i -> i.intValue() > 0)
                //Время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и с хотя бы одним положительным элементом из поля B 
                .retryTimeOut(ofSeconds(10))
                //интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromIterableBodyStepSupplier.asOneOfIterable;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.mapped;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно
        HttpResponse<List<Integer>> response = http().responseOf(GET(endPoint),
                mapped(json(BodyObject.class), BodyObject::getB)); //представляем, будто в теле вернулась коллекция

        //Извлекается фрагмент тела ответа на запрос. Тело является коллекцией или объектом Iterable.
        //Данные извлекаются при условии, что в коллекции-теле есть хотя бы один подходящий элемент.
        //Из коллекции вернется первый подходящий элемент.
        // 
        //Ответ на начало выполнения уже получен
        Integer o = http().bodyData(asOneOfIterable("Число, полученное из ответа", //Описываем, что извлекается. Пойдет в лог/отчет
                response) //полученный выше ответ
                //указываем критерий, которому должен соответствовать получаемый элемент 
                .criteria("Положительное число", i -> i.intValue() > 0)
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromIterableBodyStepSupplier.asOneOfIterable;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно
        HttpResponse<BodyObject> response = http().responseOf(GET(endPoint), json(BodyObject.class));

        //Извлекается фрагмент тела ответа на запрос. Тело не является коллекцией или объектом Iterable,
        //промежуточная коллекция или Iterable получаются в ходе дополнительных вычислений.
        //Данные извлекаются при условии, что в коллекции есть хотя бы один подходящий элемент.
        //Из коллекции вернется первый подходящий элемент.
        // 
        //Ответ на начало выполнения уже получен
        Integer o = http().bodyData(asOneOfIterable("Значение из поля B", //Описываем, что извлекается. Пойдет в лог/отчет
                response, //полученный выше ответ
                BodyObject::getB) //как извлечь из тела ответа нужные данные
                //указываем критерий, которому должен соответствовать получаемый элемент 
                .criteria("Положительное число", i -> i.intValue() > 0)
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```
</details>

#### Получение данных тела ответа как Java-объект из массива

<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromArrayBodyStepSupplier.asOneOfArray;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.mapped;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно

        //Извлекается фрагмент тела ответа на запрос. Тело является массивом.
        //Данные извлекаются при условии, что ответ подходит под указанные 
        //критерии, и в массиве-теле есть хотя бы один подходящий элемент.
        //Из массива вернется первый подходящий элемент.
        // 
        //Ответ на начало выполнения еще не получен, указывается тот запрос, который следует отправить.
        Integer o = http().bodyData(asOneOfArray("Число, полученное из ответа", //Описываем, что извлекается. Пойдет в лог/отчет
                GET(endPoint),
                mapped(json(BodyObject.class), BodyObject::getС)) //представляем, будто в теле вернулся массив
                //указываем критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //указываем критерий, которому должен соответствовать получаемый элемент 
                .criteria("Положительное число", i -> i.intValue() > 0)
                //Время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и с хотя бы одним положительным элементом в теле
                .retryTimeOut(ofSeconds(10))
                //интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromArrayBodyStepSupplier.asOneOfArray;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно

        //Извлекается фрагмент тела ответа на запрос. Тело не является массивом,
        //промежуточный массив получается в ходе дополнительных вычислений.
        //Данные извлекаются при условии, что ответ подходит под указанные 
        //критерии, и в массиве есть хотя бы один подходящий элемент.
        //Из массива вернется первый подходящий элемент.
        // 
        //Ответ на начало выполнения еще не получен, указывается тот запрос, который следует отправить.
        Integer o = http().bodyData(asOneOfArray("Значение из поля C", //Описываем, что извлекается. Пойдет в лог/отчет
                GET(endPoint), //запрос и его параметры
                json(BodyObject.class), //в каком формате прочитать тело
                BodyObject::getC) //как извлечь из тела ответа нужный промежуточный массив
                //указываем критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //указываем критерий, которому должен соответствовать получаемый элемент 
                .criteria("Положительное число", i -> i.intValue() > 0)
                //Время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и с хотя бы одним положительным элементом из поля С
                .retryTimeOut(ofSeconds(10))
                //интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromArrayBodyStepSupplier.asOneOfArray;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.mapped;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно
        HttpResponse<Integer[]> response = http().responseOf(GET(endPoint),
                mapped(json(BodyObject.class), BodyObject::getС)); //представляем, будто в теле вернулся массив

        //Извлекается фрагмент тела ответа на запрос. Тело является массивом.
        //Данные извлекаются при условии, что в массиве-теле есть хотя бы один подходящий элемент.
        //Из массива вернется первый подходящий элемент.
        // 
        //Ответ на начало выполнения уже получен
        Integer o = http().bodyData(asOneOfArray("Число, полученное из ответа", //Описываем, что извлекается. Пойдет в лог/отчет
                response) //полученный выше ответ
                //указываем критерий, которому должен соответствовать получаемый элемент 
                .criteria("Положительное число", i -> i.intValue() > 0)
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromArrayBodyStepSupplier.asOneOfArray;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно
        HttpResponse<BodyObject> response = http().responseOf(GET(endPoint), json(BodyObject.class));

        //Извлекается фрагмент тела ответа на запрос. Тело не является массивом,
        //промежуточный массив получается в ходе дополнительных вычислений.
        //Данные извлекаются при условии, что в массиве есть хотя бы один подходящий элемент.
        //Из массива вернется первый подходящий элемент.
        // 
        //Ответ на начало выполнения уже получен
        Integer o = http().bodyData(asOneOfArray("Значение из поля C", //Описываем, что извлекается. Пойдет в лог/отчет
                response, //полученный выше ответ
                BodyObject::getС) //как извлечь из тела ответа нужные данные
                //указываем критерий, которому должен соответствовать получаемый элемент 
                .criteria("Положительное число", i -> i.intValue() > 0)
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```
</details>

#### Получение данных тела ответа как Iterable
<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromIterableBodyStepSupplier.asIterable;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.mapped;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно

        //Извлекается фрагмент тела ответа на запрос. Тело является коллекцией или объектом Iterable.
        //Данные извлекаются при условии, что ответ подходит под указанные 
        //критерии, и в коллекции-теле есть хотя бы один подходящий элемент.
        //Из коллекции вернется под-коллекция подходящих элементов.
        // 
        //Ответ на начало выполнения еще не получен, указывается тот запрос, который следует отправить.
        List<Integer> o = http().bodyData(asIterable("Числа, полученные из ответа", //Описываем, что извлекается. Пойдет в лог/отчет
                GET(endPoint),
                mapped(json(BodyObject.class), BodyObject::getB)) //представляем, будто в теле вернулась коллекция
                //указываем критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //указываем критерий, которому должен соответствовать каждый элемент результирующей под-коллекции
                .criteria("Положительное число", i -> i.intValue() > 0)
                //Время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и с хотя бы одним положительным элементом в теле
                .retryTimeOut(ofSeconds(10))
                //интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromIterableBodyStepSupplier.asIterable;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно

        //Извлекается фрагмент тела ответа на запрос. Тело не является коллекцией или объектом Iterable,
        //промежуточная коллекция или Iterable получаются в ходе дополнительных вычислений.
        //Данные извлекаются при условии, что ответ подходит под указанные 
        //критерии, и в коллекции есть хотя бы один подходящий элемент.
        //Из коллекции вернется под-коллекция подходящих элементов.
        // 
        //Ответ на начало выполнения еще не получен, указывается тот запрос, который следует отправить.
        Integer o = http().bodyData(asIterable("Значения из поля B", //Описываем, что извлекается. Пойдет в лог/отчет
                GET(endPoint), //запрос и его параметры
                json(BodyObject.class), //в каком формате прочитать тело
                BodyObject::getB) //как извлечь из тела ответа нужную промежуточную коллекцию
                //указываем критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //указываем критерий, которому должен соответствовать каждый элемент результирующей под-коллекции
                .criteria("Положительное число", i -> i.intValue() > 0)
                //Время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и с хотя бы одним положительным элементом из поля B 
                .retryTimeOut(ofSeconds(10))
                //интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromIterableBodyStepSupplier.asIterable;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.mapped;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно
        HttpResponse<List<Integer>> response = http().responseOf(GET(endPoint),
                mapped(json(BodyObject.class), BodyObject::getB)); //представляем, будто в теле вернулась коллекция

        //Извлекается фрагмент тела ответа на запрос. Тело является коллекцией или объектом Iterable.
        //Данные извлекаются при условии, что в коллекции-теле есть хотя бы один подходящий элемент.
        //Из коллекции вернется под-коллекция подходящих элементов.
        // 
        //Ответ на начало выполнения уже получен
        Integer o = http().bodyData(asIterable("Числа, полученные из ответа", //Описываем, что извлекается. Пойдет в лог/отчет
                response) //полученный выше ответ
                //указываем критерий, которому должен соответствовать каждый элемент результирующей под-коллекции
                .criteria("Положительное число", i -> i.intValue() > 0)
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromIterableBodyStepSupplier.asIterable;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно
        HttpResponse<BodyObject> response = http().responseOf(GET(endPoint), json(BodyObject.class));

        //Извлекается фрагмент тела ответа на запрос. Тело не является коллекцией или объектом Iterable,
        //промежуточная коллекция или Iterable получаются в ходе дополнительных вычислений.
        //Данные извлекаются при условии, что в коллекции есть хотя бы один подходящий элемент.
        //Из коллекции вернется под-коллекция подходящих элементов.
        // 
        //Ответ на начало выполнения уже получен
        Integer o = http().bodyData(asIterable("Значения из поля B", //Описываем, что извлекается. Пойдет в лог/отчет
                response, //полученный выше ответ
                BodyObject::getB) //как извлечь из тела ответа нужные данные
                //указываем критерий, которому должен соответствовать каждый элемент результирующей под-коллекции
                .criteria("Положительное число", i -> i.intValue() > 0)
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```
</details>

#### Получение данных тела ответа как массив
<details>
  <summary>Нажми, чтобы прочесть</summary>

```java
import java.net.URI;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromArrayBodyStepSupplier.asArray;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.mapped;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно

        //Извлекается фрагмент тела ответа на запрос. Тело является массивом.
        //Данные извлекаются при условии, что ответ подходит под указанные 
        //критерии, и в массиве-теле есть хотя бы один подходящий элемент.
        //Из массива вернется под-массив подходящих элементов.
        // 
        //Ответ на начало выполнения еще не получен, указывается тот запрос, который следует отправить.
        Integer o = http().bodyData(asArray("Числа, полученные из ответа", //Описываем, что извлекается. Пойдет в лог/отчет
                GET(endPoint),
                mapped(json(BodyObject.class), BodyObject::getС)) //представляем, будто в теле вернулся массив
                //указываем критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //указываем критерий, которому должен соответствовать каждый элемент результирующего под-массива
                .criteria("Положительное число", i -> i.intValue() > 0)
                //Время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и с хотя бы одним положительным элементом в теле
                .retryTimeOut(ofSeconds(10))
                //интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromArrayBodyStepSupplier.asArray;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно

        //Извлекается фрагмент тела ответа на запрос. Тело не является массивом,
        //промежуточный массив получается в ходе дополнительных вычислений.
        //Данные извлекаются при условии, что ответ подходит под указанные 
        //критерии, и в массиве есть хотя бы один подходящий элемент.
        //Из массива вернется под-массив подходящих элементов.
        // 
        //Ответ на начало выполнения еще не получен, указывается тот запрос, который следует отправить.
        Integer o = http().bodyData(asArray("Значения из поля C", //Описываем, что извлекается. Пойдет в лог/отчет
                GET(endPoint), //запрос и его параметры
                json(BodyObject.class), //в каком формате прочитать тело
                BodyObject::getC) //как извлечь из тела ответа нужный промежуточный массив
                //указываем критерий ожидаемого ответа, т.е. код статуса должен быть 200
                .responseCriteria(statusCode(200))
                //указываем критерий, которому должен соответствовать каждый элемент результирующего под-массива
                .criteria("Положительное число", i -> i.intValue() > 0)
                //Время на получение ожидаемого результата = 10 секунд
                //В течение этих 10 секунд запрос будет отправляться до тех пор,
                //пока не вернется ожидаемый ответ с ожидаемыми данными
                //т.е. со статусом=200 и с хотя бы одним положительным элементом из поля С
                .retryTimeOut(ofSeconds(10))
                //интервал между попытками получить результат в пределах указанных 10 сек
                .pollingInterval(ofMillis(500))
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromArrayBodyStepSupplier.asArray;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.json;
import static ru.tinkoff.qa.neptune.http.api.response.body.data.MappedBodyHandler.mapped;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно
        HttpResponse<Integer[]> response = http().responseOf(GET(endPoint),
                mapped(json(BodyObject.class), BodyObject::getС)); //представляем, будто в теле вернулся массив

        //Извлекается фрагмент тела ответа на запрос. Тело является массивом.
        //Данные извлекаются при условии, что в массиве-теле есть хотя бы один подходящий элемент.
        //Из массива вернется под-массив подходящих элементов.
        // 
        //Ответ на начало выполнения уже получен
        Integer o = http().bodyData(asArray("Числа, полученные из ответа", //Описываем, что извлекается. Пойдет в лог/отчет
                response) //полученный выше ответ
                //указываем критерий, которому должен соответствовать каждый элемент результирующего под-массива 
                .criteria("Положительное число", i -> i.intValue() > 0)
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```

```java
import java.net.URI;
import java.net.http.HttpResponse;

import static java.time.Duration.ofSeconds;
import static java.time.Duration.ofMillis;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.GET;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectsFromArrayBodyStepSupplier.asArray;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseCriteria.statusCode;

public class MyTest {

    @Test
    public void myTest() {
        var endPoint = URI.create("http://my.api.com/some/path?q1=val1&q2=val2");
        //Все продемонстрированное ниже использовать в полном объеме не обязательно
        HttpResponse<BodyObject> response = http().responseOf(GET(endPoint), json(BodyObject.class));

        //Извлекается фрагмент тела ответа на запрос. Тело не является массивом,
        //промежуточный массив получается в ходе дополнительных вычислений.
        //Данные извлекаются при условии, что в массиве есть хотя бы один подходящий элемент.
        //Из массива вернется под-массив подходящих элементов.
        // 
        //Ответ на начало выполнения уже получен
        Integer o = http().bodyData(asArray("Значения из поля C", //Описываем, что извлекается. Пойдет в лог/отчет
                response, //полученный выше ответ
                BodyObject::getС) //как извлечь из тела ответа нужные данные
                //указываем критерий, которому должен соответствовать каждый элемент результирующего под-массива
                .criteria("Положительное число", i -> i.intValue() > 0)
                //текст ошибки, которая должна быть выброшена, если результат не был получен
                .throwIfNoDesiredDataReceived("Data has not been received"));
    }
}
```
</details>
