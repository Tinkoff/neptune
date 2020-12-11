# Отправка http запросов/получение ответов

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

Выше перечислены варианты использования типовых http-методов (POST, GET, PUT, DELETE). Но есть еще такие методы, как TRACE, HEAD и т.п.
Они реже используются. Чисто теоретически, можно реализовать свой собственный метод протокола http. Так же, из собственной практики, встречаются
нетипичные примеры использования методов POST, GET, PUT или DELETE. Например, DELETE-метод с отправкой тела.  Для подобных случаев предусмотрены
следующие варианты

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

## Тело запроса. На примере POST-запроса

Примеры выше демонстрируют, что некоторые http запросы (в основном POST и PUT) передают какую-то информацию в виде тела запроса.
Ниже описание того, в каком формате эта информация может быть передана.

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
        var body = new byte[] {/*Какой-то контент*/};
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
        var body = new byte[] {/*Какой-то контент*/};
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

Для случая с `param6` см. [JSON DEFAULT_JSON_MAPPER](./SETTINGS.MD#JSON-DEFAULT_JSON_MAPPER) и [XML DEFAULT_XML_MAPPER](./SETTINGS.MD#XML-DEFAULT_XML_MAPPER)

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

Про элементы перечисления `ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper` см. [JSON DEFAULT_JSON_MAPPER](./SETTINGS.MD#JSON-DEFAULT_JSON_MAPPER) и [XML DEFAULT_XML_MAPPER](./SETTINGS.MD#XML-DEFAULT_XML_MAPPER)
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
    Про элементы перечисления `ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper` см. [JSON DEFAULT_JSON_MAPPER](./SETTINGS.MD#JSON-DEFAULT_JSON_MAPPER) и [XML DEFAULT_XML_MAPPER](./SETTINGS.MD#XML-DEFAULT_XML_MAPPER)
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

Предположим, у нас есть POST запрос, который принимает в качестве тела строку определенного формата, сформированную из даты. 
В принципе, задача решается просто

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

## Отправка настроенного http-запроса. На примере GET-запроса

## Получение данных тела ответа на http-запрос. На примере POST-запроса

