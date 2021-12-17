# Работа с WebTestClient

- О контекстах можно прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Контекст).
  Объект [контекста для работы со Spring WebTestClient](https://tinkoffcreditsystems.github.io/neptune/spring.web.testclient/ru/tinkoff/qa/neptune/spring/web/testclient/WebTestClientContext.html)
  в примерах ниже вызывается статическим методом `webTestClient`

- О принципах работы шагов, которые возвращают результат, можно
  прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-возвращают-результат).

- О принципах работы шагов, которые выполняют действие, можно
  прочитать [здесь](./../../../core.api/doc/rus/STEPS.MD#Шаги-которые-выполняют-действие).

## Оглавление

- [Отправка запроса и получение ответа](#Отправка-запроса-и-получение-ответа)
    - [Получение ответа без десериализации его тела](#Получение-ответа-без-десериализации-его-тела)
    - [Получение ответа с десериализацией его тела](#Получение-ответа-с-десериализацией-его-тела)
- [Получение данных тела ответа](#Получение-данных-тела-ответа)
    - [Получение данных тела ответа как Java-объект](#Получение-данных-тела-ответа-как-Java-объект)
    - [Получение данных тела ответа как List](#Получение-данных-тела-ответа-как-List)
    - [Получение данных тела ответа в виде массива](#Получение-данных-тела-ответа-в-виде-массива)
    - [Получение данных тела ответа как Java-объект из Iterable](#Получение-данных-тела-ответа-как-Java-объект-из-Iterable)
    - [Получение данных тела ответа как Java-объект из массива](#Получение-данных-тела-ответа-как-Java-объект-из-массива)

## Отправка запроса и получение ответа

### Получение ответа без десериализации его тела

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Autowired //Или любой другой вариант
    private WebTestClient client; //при котором данное поле будет 
    //заполнено на момент начала теста

    @Test
    public void myTest() {
        //отправка запроса с получением ответа и его проверкой
        webTestClient(send(webClient -> webClient.post() //запрос
                .uri("/some/path")) //и его параметры 
                //Далее можно указать параметры ожидаемого ответа.
                //Проверка пройдет по всем 
                // указанным ожиданиям. Какие-то из них не будут удовлетворены,
                //будет выброшено AssertError с описанием всех несоответствий
                //Так же каждое отдельное несоответствие можно будет найти в отчете,
                //логе или консоли
                .expectStatus(StatusAssertions::isOk) //можно указать ожидание статуса ответа
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN)) //можно указать ожидания для заголовков ответа
                .expectCookie(cookieAssertions -> cookieAssertions.exists("someCookie")) //можно указать ожидания для cookie ответа
                //можно описать полностью или частично ожидаемое тело ответа
                .expectBodyJson("{\"someField\"=\"someValue\"}") // в json-формате 
                .expectBodyXml("<someBody><someField>someValue</someField></someBody>") // или xml-формате 
                .expectBodyJsonPath("$.someField", jsonPah -> jsonPah.isEqualTo("someValue")) //либо ожидания в виде json-path
                .expectBodyXpath("./someField", xpath -> xpath.isEqualTo("someValue")) //или в виде xPath,
                .expectEmptyBody()); //либо указать, что ожидается пустое тело ответа
    }
}
```

Так же можно обойтись
без [механизма внедрения зависимостей Spring](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-dependencies)
или какой-либо другой предварительной инициализации полей.

```java
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
public class MyTest {

    @Test
    public void myTest() {
        var client = WebTestClient.bindToController(new TestController())
                /*указываем прочие параметры*/
                .build();

        webTestClient(send(client, //явная передача инстанса
                webClient -> webClient.post().uri("/some/path"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN)));
    }
}
```

[к оглавлению документа](#Оглавление)

### Получение ответа с десериализацией его тела

```java
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Autowired //Или любой другой вариант
    private WebTestClient client; //при котором данное поле будет 
    //заполнено на момент начала теста

    @Test
    public void myTest() {
        //отправка запроса с получением ответа и его проверкой
        webTestClient(send(webClient -> webClient.post() //запрос
                .uri("/some/path"), //и его параметры 
                Dto.class) //и в объект какого класса должно быть десериализовано тело ответа
                //Далее можно указать параметры ожидаемого ответа.
                //Проверка пройдет по всем 
                // указанным ожиданиям. Какие-то из них не будут удовлетворены,
                //будет выброшено AssertError с описанием всех несоответствий
                //Так же каждое отдельное несоответствие можно будет найти в отчете,
                //логе или консоли
                .expectStatus(StatusAssertions::isOk) //можно указать ожидание статуса ответа
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN)) //можно указать ожидания для заголовков ответа
                .expectCookie(cookieAssertions -> cookieAssertions.exists("someCookie")) //можно указать ожидания для cookie ответа
                //можно указать одно или несколько ожиданий для тела ответа с использованием матчеров,
                .expectBody(equalTo(someDtoObject)) //которые описывают критерии для десериализованного тела целиком
                //либо для полей десериализованного тела, или вычисляемых с его помощью значений
                .expectBody("Some field value", //описание поля или проверяемого значения
                        dto -> dto.getSomeField(), //как получить проверяемое значение
                        equalTo(someObject))  //матчер для проверяемого значения
        ); //либо указать, что ожидается пустое тело ответа
    }

    @Test
    public void myTest2() {
        //актуально все, что в примере выше
        webTestClient(send(webClient -> webClient.post()
                .uri("/some/path2"),
                new ParameterizedTypeRefere<List<Dto>>() { //указывается ссылка на тип, 
                })); //в объект которого должно быть десериализовано тело ответа
    }
}
```

Так же можно обойтись
без [механизма внедрения зависимостей Spring](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-dependencies)
или какой-либо другой предварительной инициализации полей.

```java
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
public class MyTest {

    @Test
    public void myTest() {
        var client = WebTestClient.bindToController(new TestController())
                /*указываем прочие параметры*/
                .build();

        webTestClient(send(client, //явная передача инстанса
                webClient -> webClient.post().uri("/some/path"),
                Dto.class)
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN)));
    }
}
```

[к оглавлению документа](#Оглавление)

## Получение данных тела ответа

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest { //когда не указано, в какие объекты должна 
//происходить десериализация тела ответа
  
    @Autowired
    private WebTestClient client;

    @Test
    public void myTest() {
        //по умолчанию тело ответа представляется как массив байтов
        byte[] body = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .thenGetBody());
    }

    @Test
    public void myTest2() {
      //так же можно вернуть тело ответа в виде строки
      String body = webTestClient(send(webClient -> webClient.post()
              .uri("/some/path"))
              .expectStatus(StatusAssertions::isOk)
              .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
              .thenGetBodyAsString());
    }
}
```

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {//случай с десериализацией тела ответа

    @Autowired
    private WebTestClient client;

    @Test
    public void myTest() {

        Dto body = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"),
                Dto.class)
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .thenGetBody());
    }
}
```

[к оглавлению документа](#Оглавление)

### Получение данных тела ответа как Java-объект

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void myTest() {

        Object value = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"),
                Dto.class)
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .thenGetValue("Value of the field 'stringValue'", //описание объекта, который следует получить
                        Dto::getSomeValue) //описание получаемого результата в виде функции
                .criteria("Описание критерия, которому должен соответствовать получаемый объект",
                        o -> /*предикат, как работает критерий*/) //можно указать один или несколько критериев, 
                // которым должен соответствовать получаемый объект
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
        // если получаемый объект не соответствует перечисленным критериям, 
        // иначе - просто вернется null
    }
}
```

[к оглавлению документа](#Оглавление)

### Получение данных тела ответа как List

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import java.util.List;

import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void myTest() {

        List<Object> value = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"),
                Dto.class)
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .thenGetList("Value of the field 'listValue'", //описание списка, который следует получить
                        Dto::listValue) //описание получения списка в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                                "каждый элемент, который попадет в результирующий список",
                        o -> /*предикат, как работает критерий*/) //можно указать один или несколько критериев, 
                // которым должен соответствовать каждый элемент результирующего списка
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
        // если результирующий список пустой (не было ни одного элемента, 
        //который бы соответствовал перечисленным критериям, или исходный список пуст)  
        // иначе - просто вернется null или пустой список
    }
}
```

[к оглавлению документа](#Оглавление)

### Получение данных тела ответа в виде массива

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void myTest() {

        Object[] value = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"),
                Dto.class)
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .thenGetArray("Value of the field 'arrayValue'", //описание массива, который следует получить
                        Dto::arrayValue) //описание получения массива в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                                "каждый элемент, который попадет в результирующий массив",
                        o -> /*предикат, как работает критерий*/) //можно указать один или несколько критериев, 
                // которым должен соответствовать
                //каждый элемент результирующего массива
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
        // если результирующий массив пустой (не было ни одного элемента, 
        //который бы соответствовал перечисленным критериям, или исходный массив пуст)  
        // иначе - просто вернется null или пустой массив
    }
}
```

[к оглавлению документа](#Оглавление)

### Получение данных тела ответа как Java-объект из Iterable

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void myTest() {

        Object value = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"),
                Dto.class)
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .thenGetValueFromIterable("A value from  the field 'listValue'", //описание объекта, который следует получить
                        Dto::listValue) //описание получения списка в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                                "результирующий элемент из списка",
                        o -> /*предикат, как работает критерий*/) //можно указать один или несколько критериев, 
                // которому должен соответствовать результирующий элемент из списка
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
        // если не был получен результирующий элемент (не было ни одного элемента, 
        //который бы соответствовал перечисленным критериям, или исходный список пуст)  
        // иначе - просто вернется null
    }
}
```

[к оглавлению документа](#Оглавление)

### Получение данных тела ответа как Java-объект из массива

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.tinkoff.qa.neptune.spring.web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void myTest() {

        Object value = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"),
                Dto.class)
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .thenGetValueFromArray("A value from  the field 'arrayValue'", //описание объекта, который следует получить
                        Dto::arrayValue) //описание получения массива в виде функции
                .criteria("Описание критерия, которому должен соответствовать " +
                                "результирующий элемент из массива",
                        o -> /*предикат, как работает критерий*/) //можно указать один или несколько критериев, 
                // которому должен соответствовать результирующий элемент из массива
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
        // если не был получен результирующий элемент (не было ни одного элемента, 
        //который бы соответствовал перечисленным критериям, или исходный массив пуст)  
        // иначе - просто вернется null
    }
}
```

[к оглавлению документа](#Оглавление)