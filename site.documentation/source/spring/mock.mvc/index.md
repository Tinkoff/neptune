# Mock MVC

Данный модуль:

- является дополнением к
  стандартному [Spring MockMvc](https://spring.getdocs.org/en-US/spring-framework-docs/docs/testing/integration-testing/spring-mvc-test-framework.html)
- данный модуль предоставляет фасад , который фиксирует работу _Spring MockMvc_ в виде шагов
- данный модуль исправляет потенциальные неудобства флоу _Spring MockMvc_

```{eval-rst}
.. include:: mockmvc_dependencies.rst
```

[API](https://tinkoff.github.io/neptune/spring.mock.mvc/index.html)

## Почему _Neptune_ ?

Ниже небольшое сравнение того как выглядит один и тот же тест:

- с использованием Mock MVC, без реализации шагов
- с использованием Mock MVC и с реализацией шагов
- с использованием Mock MVC и Neptune

### Тест с использованием Mock MVC, без реализации шагов

```java
@SpringBootTest
@AutoConfigureMockMvc
public class SomeTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void semeAPITest() {
        var bodyContent = mockMvc.perform(get("/something")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SomeDTO())))
            //Если текущее ожидание не выполнилось, 
            //на нем тест остановится и последующие ожидания не будут
            //проверены. Хотелось бы видеть более полную картину несоответствий
            //до того, как начать багофикс
            .andExpect(status().isOk()) //хотелось бы частые ожидания иметь 
            //в более доступном и коротком виде
            //
            //По умолчанию не поддерживается десериализация, 
            //тело можно провалидировать по xpath/jsonpath/текстовому контенту
            .andExpect(jsonPath("$.successOrder").value(5))
            //либо получить тело ответа как сырой текст
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        //и явно вызвать десериализацию
        SomeResponseDto responseDto
            = objectMapper.readValue(bodyContent, SomeResponseDto.class);

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

### Тест с использованием Mock MVC и с реализацией шагов

Предположим, что результат интеграционного теста должен быть оформлен в отчет, описывающий по шагам,
какие действия выполняются и каков их результат. Тогда

```java
@SpringBootTest
@AutoConfigureMockMvc
public class SomeTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Step("Подготовить тело запроса")
    private String prepareRequestBody() {
        var body = objectMapper.writeValueAsString(new SomeDTO());
        addAttachment("Request body", "application/json", body);
        return body;
    }

    @Step("Подготавливаем запрос GET /something")
    private MockHttpServletRequestBuilder prepareRequest(String body) {
        var getRequest = get("/something")
            .contentType(APPLICATION_JSON)
            .content(body);

        //делаем аттачи, фиксируем параметры и т.д.
        return getRequest;
    }

    @Step("Выполняем запрос и возвращаем прочитанное тело")
    private SomeResponseDto getSomeResponseDto(
        MockHttpServletRequestBuilder requestBuilder, int count) {

        var bodyContent = mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.successOrder").value(count))
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        addAttachment("Response body", "application/json", bodyContent);
        return objectMapper.readValue(bodyContent, SomeResponseDto.class);
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
        var request = prepareRequest(body);
        var dto = getSomeResponseDto(request, 5);
        assertSomeDTO(dto, someExpectedValue);

        var someCalculatedValue = //вычисление чего-то с использованием 
            //responseDto
        assertSomeCalculatedValue(someCalculatedValue, someExpectedValue2);
        //и т.д.
    }
}
```

### Тест с использованием Mock MVC и Neptune

```java
@SpringBootTest
@AutoConfigureMockMvc
public class SomeTest {
    
    @Autowired
    private ObjectMapper objectMapper;

    //Поле ниже можно не объявлять
    //@Autowired
    //private MockMvc mockMvc;

    @Test //все описанное в тесте сформирует шаги разной вложенности
    //и автоматически сформирует аттачи
    public void semeAPITest() {
        SomeResponseDto responseDto = mockMvcGet(
            response(get("/something")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SomeDTO())))
                // будут проверены все ожидания    
                .expectStatus(200) //проваленные будут выделены в отчете
                .expectJsonPathValue("$.successOrder", 5)
                 //предусмотрен механизм десериализации   
                .thenGetBody(SomeResponseDto.class)
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

settings.md
response.md
bodydata.md
```

