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
- [Получение данных тела ответа](#Получение-данных-тела-ответа)
    - [Получение данных тела ответа как Java-объект](#Получение-данных-тела-ответа-как-Java-объект)
    - [Получение данных тела ответа как List](#Получение-данных-тела-ответа-как-List)
    - [Получение данных тела ответа в виде массива](#Получение-данных-тела-ответа-в-виде-массива)
    - [Получение данных тела ответа как Java-объект из Iterable](#Получение-данных-тела-ответа-как-Java-объект-из-Iterable)
    - [Получение данных тела ответа как Java-объект из массива](#Получение-данных-тела-ответа-как-Java-объект-из-массива)

## Отправка запроса и получение ответа

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
                .expectStatus(StatusAssertions::isOk) //тут можно указать параметры ожидаемого ответа
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN)) //проверка пройдет по всем 
                // указанным ожиданиям. Какие-то из них не будут удовлетворены,
                //будет выброшено AssertError с описанием всех несоответствий
                //Так же каждое отдельное несоответствие можно будет найти в отчете,
                //логе или консоли
                .hasBody()); //среди прочих ожиданий можно указать, что у ответа должно быть тело 
                // и в каком формате оно должно быть:
                // - .emptyBody() - у ответа не должно быть тела
                // - .hasBody() - у ответа должно быть тело (любое)
                // - .bodyAs(Dto.class) - тело, десериализующееся в объект указанного класса
                // - .bodyAs(new ParameterizedTypeReference<List<Dto>>() {}) - тело, десериализующееся в объект 
                // указанного типа
                // - .bodyAsListOf(Dto.class) - тело, десериализующееся в лист объектов указанного класса
                // - .bodyAsListOf(new ParameterizedTypeReference<List<Dto>>() {}) - тело, десериализующееся в лист 
                // объектов указанного типа
    }
}
```

Так же можно обойтись без [механизма внедрения зависимостей Spring](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-dependencies)
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
            webClient -> webClient.post()
            .uri("/some/path"))
            .expectStatus(StatusAssertions::isOk)
            .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN)) 
            .hasBody());
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
public class MyTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void myTest() {
        
        Object body = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .emptyBody() //указываем, что ожидается пустое тело
                .thenGetBody()); //вернется null
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
public class MyTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void myTest() {
        
        Byte[] body = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .hasBody() //указываем, что ожидается любое тело
                .thenGetBody()); //получаем его в виде бинарных данных
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
public class MyTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void myTest() {
        
        Dto body = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class) //указываем, что ожидается тело, десериализованное в объект класса
                .thenGetBody() //получаем его в виде объекта
                .criteria("Описание критерия, которому должно соответствовать тело",
                        dto -> /*предикат, как работает критерий*/) //можно указать один или несколько критериев, 
                // которым должно соответствовать
                //десериализованое тело ответа
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если десериализованое тело не соответствует перечисленным критериям, 
                // иначе - просто вернется null
    }
}
```

```java
import java.util.List;

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
        
        List<Dto> body = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(new ParameterizedTypeReference<List<Dto>>() {}) //указываем, что ожидается тело, десериализованное 
                // в объект типа
                .thenGetBody() //получаем его в виде объекта
                .criteria("Описание критерия, которому должно соответствовать тело",
                        dtoList -> /*предикат, как работает критерий*/) //можно указать один или несколько критериев, 
                // которым должно соответствовать
                //десериализованое тело ответа
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если десериализованое тело не соответствует перечисленным критериям, 
                // иначе - просто вернется null
    }
}
```

```java
import java.util.List;

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
        
        List<Dto> body = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAsListOf(Dto.class) //указываем, что ожидается тело, десериализованное 
                // в лист из объектов класса
                .thenGetBody() //получаем его в виде объекта
                .criteria("Описание критерия, которому должно соответствовать тело",
                        dtoList -> /*предикат, как работает критерий*/) //можно указать один или несколько критериев, 
                // которым должно соответствовать
                //десериализованое тело ответа
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если десериализованое тело не соответствует перечисленным критериям, 
                // иначе - просто вернется null
    }
}
```

```java
import java.util.List;

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
        
        List<List<Dto>> body = webTestClient(send(webClient -> webClient.post()
                .uri("/some/path"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAsListOf(new ParameterizedTypeReference<List<Dto>>() {}) //указываем, что ожидается тело, десериализованное 
                // в лист из объектов типа
                .thenGetBody() //получаем его в виде объекта
                .criteria("Описание критерия, которому должно соответствовать тело",
                        listOfDtoList -> /*предикат, как работает критерий*/) //можно указать один или несколько критериев, 
                // которым должно соответствовать
                //десериализованое тело ответа
                .throwOnNoResult()); //можно указать, что должно быть выброшено исключение, 
                // если десериализованое тело не соответствует перечисленным критериям, 
                // иначе - просто вернется null
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
                .uri("/some/path"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
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
              .uri("/some/path"))
              .expectStatus(StatusAssertions::isOk)
              .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
              .bodyAs(Dto.class)
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
                .uri("/some/path"))
                .expectStatus(StatusAssertions::isOk)
                .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
                .bodyAs(Dto.class)
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
              .uri("/some/path"))
              .expectStatus(StatusAssertions::isOk)
              .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
              .bodyAs(Dto.class)
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
              .uri("/some/path"))
              .expectStatus(StatusAssertions::isOk)
              .expectHeader(headerAssertions -> headerAssertions.contentType(TEXT_PLAIN))
              .bodyAs(Dto.class)
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