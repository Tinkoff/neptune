# WebTestClient

Данный модуль:

- является дополнением к
  стандартному [Spring WebTestClient](https://spring.getdocs.org/en-US/spring-framework-docs/docs/testing/integration-testing/webtestclient.html)
- данный модуль предоставляет фасад , который фиксирует работу _Spring WebTestClient_ в виде шагов
- данный модуль исправляет потенциальные неудобства флоу _Spring WebTestClient_

```{eval-rst}
.. include:: webtestclient_dependencies.rst
```

[API](https://tinkoff.github.io/neptune/spring.web.testclient/index.html)

## Сравнение _Neptune + WebTestClient_ с другими вариантами

Ниже небольшое сравнение того как выглядит один и тот же тест:

- с использованием WebTestClient, без реализации шагов
- с использованием WebTestClient и с реализацией шагов
- с использованием WebTestClient и Neptune

### Тест с использованием WebTestClient, без реализации шагов

```java

@SpringBootTest
@AutoConfigureWebTestClient
public class SomeTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void semeAPITest() {
        SomeResponseDto responseDto = client.post()
            .uri("/something")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(new SomeDTO()), SomeDTO.class)
            .exchange()
            //Если текущее ожидание не выполнилось, 
            //на нем тест остановится и последующие ожидания не будут
            //проверены. Хотелось бы видеть более полную картину несоответствий
            //до того, как начать багофикс
            .expectStatus().isOk()
            //хотелось бы частые ожидания иметь
            //в более доступном и коротком виде
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(SomeResponseDto.class)
            .returnResult()
            .getResponseBody();

        //дельнейшие вычисления
        //проверка поля ответа
        assertThat("Some field value",
            responseDto.getSomething(),
            is(someExpectedValue));

        var someCalculatedValue = //вычисление чего-то с использованием 
            //responseDto

            assertThat("Some calculated value",
                someCalculatedValue.getSomethingElse(),
                is(someExpeсtedValue2));

        //и т.д.
    }
}
```

### Тест с использованием WebTestClient и с реализацией шагов

Предположим, что результат интеграционного теста должен быть оформлен в отчет, описывающий по шагам,
какие действия выполняются и каков их результат. Тогда

```java

@SpringBootTest
@AutoConfigureWebTestClient
public class SomeTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient client;

    @Step("Подготовить тело запроса")
    private SomeDTO prepareRequestBody() {
        var toReturn = new SomeDTO();
        addAttachment("Request body", "application/json",
            objectMapper.writeValueAsString(toReturn));
        return toReturn;
    }

    @Step("Получаем ответ на запрос POST /something")
    private ResponseSpec getResponsePostSpec(SomeDTO body) {
        var postExchangeSpec = client.post()
            .uri("/something")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(new SomeDTO()), SomeDTO.class)
            .exchange();

        //делаем аттачи, фиксируем параметры и т.д.
        return postExchangeSpec;
    }

    @Step("Выполняем проверку ответа и возвращаем прочитанное тело")
    private SomeResponseDto getSomeResponseDto(
        ResponseSpec responseSpec) {

        SomeResponseDto bodyContent = responseSpec.expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(SomeResponseDto.class)
            .returnResult()
            .getResponseBody();

        addAttachment("Response body", "application/json",
            objectMapper.writeValueAsString(bodyContent));
        return bodyContent;
    }

    @Step("Проверить тело ответа")
    private void assertSomeDTO(SomeResponseDto toCheck, Object expected) {
        assertThat("Some field value",
            responseDto.getSomething(),
            is(expected));
    }

    @Step("Проверить полученное у тела значение")
    private void assertSomeCalculatedValue(SomeCalculatedValue toCheck, Object expected) {
        assertThat("Some calculated value",
            someCalculatedValue.getSomethingElse(),
            is(expected));
    }


    @Test
    public void semeAPITest() {
        //Тест стал короче, НО!!!!
        //У нас появился толстый слой шагов, который надо организовывать 
        //и поддерживать.
        //Может начаться дублирование кода, если аналогичные действия
        //встречаются в других тестах.
        //Организация библиотеки шагов сделает тест непрозрачным, 
        //в перспективе затруднит модификацию/рефактринг тестов

        var body = prepareRequestBody();
        var response = getResponsePostSpec(body);
        var dto = getSomeResponseDto(response);
        assertSomeDTO(dto, someExpectedValue);

        var someCalculatedValue = //вычисление чего-то с использованием 
            //responseDto
            assertSomeCalculatedValue(someCalculatedValue, someExpectedValue2);
        //и т.д.
    }
}
```

### Тест с использованием WebTestClient  и Neptune

```java

@SpringBootTest
@AutoConfigureWebTestClient
public class SomeTest {

    //Поле ниже можно не объявлять
    //@Autowired
    //private WebTestClient client;

    @Test //все описанное в тесте сформирует шаги разной вложенности
    //и автоматически сформирует аттачи
    public void semeAPITest() {
        SomeResponseDto responseDto = webTestClient(
            send(webClient -> webClient.post()
                    .uri("/something")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(new SomeDTO()), SomeDTO.class),
                Dto.class)
                // будут проверены все ожидания       
                .expectStatus(200) //проваленные будут выделены в отчете
                .expectContentType(APPLICATION_JSON)
                .thenGetBody()
        );

        check("Response body",
            responseDto,
            match("Some field value",
                SomeResponseDto::getSomething,
                is(someExpectedValue)),
            match("Some calculated value",
                dto -> {/*вычисление чего-то с использованием*/},
                is(someExpeсtedValue2)));
    }
}
```

```{toctree}
:hidden:

response.md
```