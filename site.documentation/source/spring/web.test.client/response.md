# WebTestClient. Request / Response

```java
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient
    .WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Test(description = "Простая отправка запроса")
    public void myTest() {
        webTestClient(
            //описывается запрос:
            // метод (GET, POST, PUT или Delete)
            send(webClient -> webClient.post()
            .uri("/something")
            .contentType(MediaType.APPLICATION_JSON) //и
            //параметры
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(new SomeDTO()), SomeDTO.class),
            //Опциональный параметр, который можно не указывать.
            //От его наличия зависит, как будут сформированы 
            //ожидания к контенту ответа, как к dto, или 
            //как к тексту/массиву байтов
            Dto.class)
        );
    }
}
```

Ниже пример с явным использованием объекта `org.springframework.test.web.reactive.server.WebTestClient`

```java
import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.tinkoff.qa.neptune.spring
    .web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient
    .WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Autowired //Или любой другой вариант
    private WebTestClient client; //инициализации поля или переменной

    @Test(description = "Простая отправка запроса")
    public void myTest() {
        webTestClient(
            send(client, //явная передача объекта WebTestClient
            webClient -> webClient.post()
            .uri("/something")
            .contentType(MediaType.APPLICATION_JSON) //и
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(new SomeDTO()), SomeDTO.class),
            Dto.class)
        );
    }
}
```

## WebTestClient. Описание ожиданий

Методы, с помощью которых можно описать ожидаемый ответ, можно найти:

- в документации [общего описания отправки запроса. Названия методов начинаются на _
  expect_](https://tinkoff.github.io/neptune/spring.web.testclient/ru/tinkoff/qa/neptune/spring/web/testclient/SendRequestAction.html)
  .
- в документации описания отправки
  запросов, [тела ответов на которые читаются как текст/массив байтов. Названия методов начинаются на _
  expect_](https://tinkoff.github.io/neptune/spring.web.testclient/ru/tinkoff/qa/neptune/spring/web/testclient/SendRequestActionRaw.html)
  .
- в документации описания отправки запросов, [тела ответов на которые читаются как DTO. Названия методов начинаются на _
  expect_](https://tinkoff.github.io/neptune/spring.web.testclient/ru/tinkoff/qa/neptune/spring/web/testclient/SendRequestActionMapped.html)
  .

Ниже простой пример

```java
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient
    .WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Test(description = "Пример описания ожидаемого ответа")
    public void myTest() {
        webTestClient(
            send(webClient -> webClient.post()
            .uri("/something")
            .contentType(MediaType.APPLICATION_JSON) //и
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(new SomeDTO()), SomeDTO.class))
            // Часто используемые ожидания имеют  
            .expectStatus(200) //упрощенную запись
            .expectContentType(APPLICATION_JSON)
            /////////////////////////////////////////
            //
            //Так же, при необходимости, есть возможности описывать
            //ожидания в функциональном стиле
            .expectStatus(statusAssertions -> {
            //некое вычисление или алгоритм
            return statusAssertions.isOk();
            })
            .expectContent(headerAssertions -> {
            //некое вычисление или алгоритм
            return headerAssertions.contentType(APPLICATION_JSON);
            })
        );
    }
}
```

Пример описания ожидания к телу ответа, которое прочитано как текст/массив байтов

```java
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient
    .WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Test(description = "Пример описания ожидаемого ответа, " +
            "чье тело прочитано как текст/массив байтов")
    public void myTest() {
        webTestClient(
            send(webClient -> webClient.post()
            .uri("/something")
            .contentType(MediaType.APPLICATION_JSON) //и
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(new SomeDTO()), SomeDTO.class))
            //можно описывать ожидания к json/xml тексту
            //или к массиву байтов    
            .expectBodyJsonPathEquals("some.path", 1)
        );
    }
}
```

Пример описания ожидания к телу ответа, которое прочитано как DTO

```java
import static org.hamcrest.Matchers.*;

import static ru.tinkoff.qa.neptune.spring
    .web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring.web.testclient
    .WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Test(description = "Пример описания ожидаемого ответа, " +
            "чье тело прочитано как DTO")
    public void myTest() {
        webTestClient(
            send(webClient -> webClient.post()
            .uri("/something")
            .contentType(MediaType.APPLICATION_JSON) //и
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(new SomeDTO()), SomeDTO.class), Dto.class)
            //ожидание для всего тела     
            .expectBody(equalTo(new Dto()))
            //описание того, ЧТО проверяется    
            .expectBody("Some field value",
            //описываем, как получить проверяемое значение
            Dto::someField,
            equalTo(someValue))
        );
    }
}
```

## WebTestClient. Тело ответа

Важно:

- [Шаги, возвращающие объекты](../../quick_start/steps/pattern_steps/get_step/index.md)

### Прочитанное как текст / массив байтов

```java
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest { //когда не указано, в какие объекты должна 
//происходить десериализация тела ответа

    @Test
    public void myTest() {
        //по умолчанию тело ответа представляется как массив байтов
        byte[] body = webTestClient(send(/*параметры запроса*/)
            //ожидания
            .thenGetBody()
        );

        //так же можно вернуть тело ответа в виде строки
        String body2 = webTestClient(send(/*параметры запроса*/)
            //ожидания
            .thenGetBodyAsString()
        );
    }
}
```

### Прочитанное как DTO

```java
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Test
    public void myTest() {
        Dto body = webTestClient(send(/*параметры запроса*/,
            //так же можно использовать
            //org.springframework.core.ParameterizedTypeReference
            Dto.class)
            //ожидания
            .thenGetBody());

        Object value = webTestClient(send(/*параметры запроса*/,
            Dto.class)
            //ожидания
            //
            // описание объекта, который следует получить    
            .thenGetValue("Value of the field 'getSomeValue'",
            //описание получаемого результата в виде функции
            Dto::getSomeValue)
            // можно указать один или несколько критериев,  
            // которым должен соответствовать получаемый объект.
            // Так же доступны criteriaOr(criteria...), 
            // criteriaOnlyOne(criteria...)
            // criteriaNot(criteria...)    
            .criteria("Описание критерия, " +
            "которому должен соответствовать " +
            "получаемый объект", dto -> {
            /*предикат, как работает критерий*/
            })
            //можно указать, что должно быть выброшено исключение,  
            // если получаемый объект пустой / null / не соответствует
            // перечисленным критериям    
            .throwOnNoResult()
        );
    }
}
```

### List

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.spring
    .web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Test
    public void myTest() {

        List<Object> value = webTestClient(send(/*параметры запроса*/,
            //так же можно использовать
            //org.springframework.core.ParameterizedTypeReference
            Dto.class)
            //ожидания
            //
            //описание листа, который следует получить
            .thenGetList("Value of the field 'listValue'",
            //описание получения списка в виде функции
            Dto::listValue)
            //дополнительные уточняющие параметры    
        );
    }
}
```

```{eval-rst}
.. include:: ../../shared_docs/steps_return_list_optiotal_parameters_sync.rst
```

### Массив

```java
import java.util.List;

import static ru.tinkoff.qa.neptune.spring
    .web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Test
    public void myTest() {

        List<Object> value = webTestClient(send(/*параметры запроса*/,
            //так же можно использовать
            //org.springframework.core.ParameterizedTypeReference
            Dto.class)
            //ожидания
            //
            //описание листа, который следует получить
            .thenGetArray("Value of the field 'arrayValue'",
            //описание получения списка в виде функции
            Dto::arrayValue)
            //дополнительные уточняющие параметры            
        );
    }
}
```

```{eval-rst}
.. include:: ../../shared_docs/steps_return_array_optiotal_parameters_sync.rst
```

### Элемент Iterable

```java
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Test
    public void myTest() {

        Object value = webTestClient(send(/*параметры запроса*/,
            //так же можно использовать
            //org.springframework.core.ParameterizedTypeReference
            Dto.class)
            //ожидания
            //
            //описание объекта, который следует получить
            .thenGetValueFromIterable("A value from " +
            "the field 'listValue'",
            //описание получения списка в виде функции
            Dto::listValue)
            //дополнительные уточняющие параметры     
        );
    }
}
```

```{eval-rst}
.. include:: ../../shared_docs/steps_return_iterable_item_optiotal_parameters_sync.rst
```

### Элемент массива

```java
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.SendRequestAction.send;
import static ru.tinkoff.qa.neptune.spring
    .web.testclient.WebTestClientContext.webTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class MyTest {

    @Test
    public void myTest() {

        Object value = webTestClient(send(/*параметры запроса*/,
            //так же можно использовать
            //org.springframework.core.ParameterizedTypeReference
            Dto.class)
            //ожидания
            //
            //описание объекта, который следует получить
            .thenGetValueFromArray("A value from  " +
            "the field 'arrayValue'",
            //описание получения списка в виде функции
            Dto::arrayValue)
            //дополнительные уточняющие параметры     
        );
    }
}
```

```{eval-rst}
.. include:: ../../shared_docs/steps_return_array_item_optiotal_parameters_sync.rst
```

